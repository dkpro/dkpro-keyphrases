package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.ExternalResourceFactory.bindResource;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.InvalidXMLException;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.BookIndexPhraseAggregationAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.aggregation.aggregationstrategy.MaximumAggregation;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.pipeline.SegmentProcessingPipeline;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation.PseudoSentenceSegmentAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation.SymmetricSegmentAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.KeyphraseMerger;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.StopwordFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.StructureFilter;

/**
 * Base implementation for book index phrase extraction.
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class BookIndexPhraseExtractor_BaseImpl
	implements BookIndexPhraseExtractor
{

	private int segmentationN = 10;

	public BookIndexPhraseExtractor_BaseImpl()
	{
		super();
	}

	public final static String LF = System.getProperty("line.separator");

	public enum SegmentationType
	{
		TOKENS, SENTENCES, SEGMENTS
	}

	private String language = "en";
	private CandidateSet candidateSet = new TokenCandidateSet();
	private boolean mergePhrases = false;
	private int minPhraseLength = 1;
	private int maxPhraseLength = 8;
	private boolean convertToLowercase = true;
	private SegmentationType segmentationType = SegmentationType.TOKENS;

	/**
	 * Processes an input text and returns a list of strings sorted descending by
	 * their score.
	 *
	 * @param textFile
	 * @return a list of bookindex-phrases
	 *
	 * @throws ResourceInitializationException
	 * @throws AnalysisEngineProcessException
	 * @throws IOException
	 * @throws CASRuntimeException
	 */
	@Override
	public List<String> extract(File textFile)
	{
		AnalysisEngine ae;

		try {
			ae = AnalysisEngineFactory.createAggregate(createAggregate());

			JCas jcas = ae.newJCas();

			jcas.setDocumentText(FileUtils.readFileToString(textFile));
			DocumentMetaData docMetaData = new DocumentMetaData(jcas);
			docMetaData.addToIndexes();

			// since the segmentProcessingAE contains a demultiplier it produces a
			// new jcas which we retrieve
			jcas = ae.processAndOutputNewCASes(jcas).next();

			return getBookIndexPhrases(jcas);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private AnalysisEngineDescription createAggregate()
		throws ResourceInitializationException
	{
		AnalysisEngineDescription preSegmentProcessing = createAggregateDescription(
				candidateSet.createPreprocessingComponents(language), createSegmenter());

		AnalysisEngineDescription segmentProcessing = createAggregateDescription(
				createRanker(), createMergerFilter());

		AnalysisEngineDescription postSegmentProcessing = createAggregator();

		return SegmentProcessingPipeline.createSegmentProcessingAggregate(
				preSegmentProcessing, segmentProcessing, postSegmentProcessing);
	}

	protected AnalysisEngineDescription createSegmenter()
		throws ResourceInitializationException
	{
		switch (segmentationType) {
		case TOKENS:
			return createPrimitiveDescription(
					PseudoSentenceSegmentAnnotator.class,
					PseudoSentenceSegmentAnnotator.PARAM_NUMBER_OF_TOKENS_PER_SEGMENT, segmentationN);
//		case SENTENCES:
//			return createPrimitiveDescription(
//					SimpleSegmenter.class,
//					SimpleSegmenter.PARAM_NUMBER_OF_SENTENCES_PER_SEGMENT, segmentationN);
		case SEGMENTS:
			return createPrimitiveDescription(
					SymmetricSegmentAnnotator.class,
					SymmetricSegmentAnnotator.PARAM_SEGMENTATION_FACTOR, segmentationN);
		default:
			throw new IllegalStateException();
		}
	}

	protected abstract AnalysisEngineDescription createRanker()
		throws ResourceInitializationException;

	protected AnalysisEngineDescription createMergerFilter()
		throws ResourceInitializationException
	{
		AggregateBuilder b = new AggregateBuilder();

		if (mergePhrases) {
			b.add(createPrimitiveDescription(KeyphraseMerger.class,
					KeyphraseMerger.PARAM_MAX_LENGTH, maxPhraseLength));
		}
		b.add(createStopwordFilter(getLanguage()));
		b.add(createPrimitiveDescription(StructureFilter.class,
				StructureFilter.PARAM_MIN_TOKENS, minPhraseLength,
				StructureFilter.PARAM_MAX_TOKENS, maxPhraseLength,
				StructureFilter.PARAM_POS_PATTERNS, false));

		return b.createAggregateDescription();
	}

	/**
	 * @param language
	 *          default implementation recognizes: en, de
	 * @return a stopword filter
	 * @throws ResourceInitializationException
	 */
	protected AnalysisEngineDescription createStopwordFilter(String language)
		throws ResourceInitializationException
	{
		if (language.equals("en")) {
			return createPrimitiveDescription(StopwordFilter.class,
					StopwordFilter.PARAM_STOPWORD_LIST,
					"classpath:/stopwords/english_stopwords.txt");
		}
		else if (language.equals("de")) {
			return createPrimitiveDescription(StopwordFilter.class,
					StopwordFilter.PARAM_STOPWORD_LIST,
					"classpath:/stopwords/german_stopwords.txt");
		}
		else {
			throw new ResourceInitializationException(new Throwable(
					"No stopwordlist available for language: " + language));
		}
	}

	protected AnalysisEngineDescription createAggregator()
		throws ResourceInitializationException
	{
		AnalysisEngineDescription aed = createPrimitiveDescription(
				BookIndexPhraseAggregationAnnotator.class,
				BookIndexPhraseAggregationAnnotator.PARAM_LOWERCASE, convertToLowercase
				);
		try {
			bindResource(aed,
					BookIndexPhraseAggregationAnnotator.AGGREGATION_STRATEGY,
					MaximumAggregation.class);
		}
		catch (InvalidXMLException e) {
			throw new ResourceInitializationException(e);
		}
		return aed;
	}

	/**
	 * @param jcas
	 *          containing some annotation representing
	 *          bookindex-phrase-annotations. The default implementation expects
	 *          {@link BookIndexPhrase}-annotations.
	 * @return A list of string representing bookindex-phrases. The default
	 *         implementation returns just the phrases without the score in
	 *         descending order (by score).
	 */
	protected List<String> getBookIndexPhrases(JCas jcas)
	{
		List<BookIndexPhrase> bookIndexPhrases = new ArrayList<BookIndexPhrase>();
		for (BookIndexPhrase bookIndexPhrase : JCasUtil.select(jcas,
				BookIndexPhrase.class)) {
			bookIndexPhrases.add(bookIndexPhrase);
		}
		Collections.sort(bookIndexPhrases, new BookIndexPhraseComparator());
		List<String> result = new ArrayList<String>();
		for (BookIndexPhrase bookIndexPhrase : bookIndexPhrases)
			result.add(bookIndexPhrase.getPhrase());
		return result;
	}

	/**
	 * @return the candidate
	 */
	public CandidateSet getCandidateSet()
	{
		return this.candidateSet;
	}

	public void setCandidateSet(CandidateSet candidateSet)
	{
		this.candidateSet = candidateSet;
	}

	/**
	 * Merges adjacent phrases until the given maximum phrase length. Does not
	 * merge phrases which length would exceed the specified maximum phrase
	 * length.
	 *
	 * @param mergePhrases
	 */
	public void setMergePhrases(boolean mergePhrases)
	{
		this.mergePhrases = mergePhrases;
	}

	/**
	 * Phrases that are shorter then the given minimum length or longer then the
	 * given maximum length will be removed.
	 *
	 * @param minPhraseLength
	 * @param maxPhraseLength
	 */
	public void setPhraseLengthRange(int minPhraseLength, int maxPhraseLength)
	{
		if (minPhraseLength > maxPhraseLength)
			throw new IllegalArgumentException(
					"maxPhraseLength must be greater or equals then minPhraseLength!");
		this.minPhraseLength = minPhraseLength;
		this.maxPhraseLength = maxPhraseLength;
	}

	/**
	 * Sorts {@link BookIndexPhrase}s by score (descending).
	 */
	public static class BookIndexPhraseComparator
		implements Comparator<BookIndexPhrase>, Serializable
	{

		private static final long serialVersionUID = 6271903112402098451L;

		@Override
		public int compare(BookIndexPhrase o1, BookIndexPhrase o2)
		{
			return -1 * Double.compare(o1.getScore(), o2.getScore());
		}
	}

	public boolean isConvertToLowercase()
	{
		return convertToLowercase;
	}

	public void setConvertToLowercase(boolean convertToLowercase)
	{
		this.convertToLowercase = convertToLowercase;
	}

	public String getLanguage()
	{
		return language;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("BookIndexPhraseExtractor: ");
		sb.append(this.getClass().getSimpleName()).append(LF);
		sb.append("Language: ").append(language).append(LF);
		sb.append(candidateSet.toString());
		sb.append("Segmentation: ").append(getSegmentationConfiguration()).append(
				LF);
		sb.append("Merge phrases: ").append(mergePhrases).append(LF);
		sb.append("Phrase length: ").append(minPhraseLength).append("-").append(maxPhraseLength).append(LF);
		return sb.toString();
	}

	/**
	 * @return human readable representation of segmentation configuration
	 */
	private String getSegmentationConfiguration()
	{

		switch (segmentationType) {
		case SEGMENTS:
			return segmentationN + " parts of same length";
		case TOKENS:
			return "Each " + segmentationN + " tokens";
		case SENTENCES:
			return "Each " + segmentationN + " sentences";
		default:
			throw new IllegalStateException();
		}

	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	/**
	 * Configures the segmentation that will be applied to the text document.
	 * <ul>
	 * <li>TOKENS: Each segment will consist of n tokens.</li>
	 * <li>SENTENCES: Each segment will consist of n sentences.</li>
	 * <li>SEGMENTS: The text document will be partitioned to n segments.</li>
	 * </ul>
	 *
	 * @param segmentationType
	 */
	public void setSegmentation(SegmentationType segmentationType, int n)
	{
		this.segmentationType = segmentationType;
		this.segmentationN = n;
	}

}

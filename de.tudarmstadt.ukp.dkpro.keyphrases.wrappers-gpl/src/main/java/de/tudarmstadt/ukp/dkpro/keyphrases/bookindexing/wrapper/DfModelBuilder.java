package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createDescription;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfConsumer;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.LemmaCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.StemCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * Creates document frequency models used by the {@link TfidfAnnotator}.
 *
 * @author Mateusz Parzonka
 *
 */

public class DfModelBuilder
{

	private String language;
	private File inputDir;
	private String suffix;

	/**
	 * Creates a builder for df models which consumes the documents ending with
	 * the given suffix contained in the given directory in the given language.
	 *
	 * @param language
	 *          assumes the consumed documents are in the given language
	 * @param inputDirectory
	 *          documents contained in this directory are consumed
	 * @param suffix
	 *          only documents with this suffix are consumed
	 * @return the model builder
	 */
	public DfModelBuilder(String language, File inputDir, String suffix)
	{
		super();
		this.language = language;
		this.inputDir = inputDir;
		this.suffix = suffix;
	}

	/**
	 * Creates a builder for tf.idf models which consumes the documents contained
	 * in the given directory in the given language.
	 *
	 * @param language
	 *          assumes the consumed documents are in the given language
	 * @param inputDirectory
	 *          all documents contained in this directory are consumed
	 * @return the model builder
	 */
	public DfModelBuilder(String language, File inputDir)
	{
		this(language, inputDir, "*");
	}

	/**
	 * Creates a model based on stems and writes it as the file specified.
	 *
	 * @param outputFile
	 */
	public void buildStemModel(File outputFile)
	{
		buildModel(new StemCandidateSet(), outputFile);
	}

	/**
	 * Creates a model based on tokens and writes it as the file specified.
	 *
	 * @param outputFile
	 */
	public void buildTokenModel(File outputFile)
	{
		buildModel(new TokenCandidateSet(), outputFile);
	}

	/**
	 * Creates a model based on lemmas and writes it as the file specified.
	 *
	 * @param outputFile
	 */
	public void buildLemmaModel(File outputFile)
	{
		buildModel(new LemmaCandidateSet(), outputFile);
	}

	/**
	 * Creates a model based on the given candidate set and writes it as the file
	 * specified. The
	 *
	 * @param outputFile
	 */
	public void buildKeyphraseCandidateModel(CandidateSet candidateSet,
			File outputFile)
	{
		buildModel(candidateSet, KeyphraseCandidate.class.getName(), outputFile);
	}

	/**
	 * Builds a pipeline using the feature path to the string representation
	 * specified in the candidateFactory.
	 *
	 * @param candidateSet
	 * @param outputFile
	 */
	private void buildModel(final CandidateSet candidateSet, final File outputFile) {
		buildModel(candidateSet, candidateSet.getFeaturePath(), outputFile);
	}

	/**
	 * Builds a model using the preprocessing specified in the candidate and given
	 * feature path. The model is written as a single file to the path as
	 * specified.
	 *
	 * @param candidateSet
	 * @param featurePath
	 * @param outputFile
	 */
	private void buildModel(CandidateSet candidateSet, String featurePath,
			final File outputFile)
	{

		CollectionReaderDescription reader;
		try {
			reader = createDescription(
					TextReader.class, TextReader.PARAM_PATH,
					inputDir.getAbsolutePath(),
					TextReader.PARAM_PATTERNS,
					new String[] { ResourceCollectionReaderBase.INCLUDE_PREFIX + "*."
							+ suffix });

		AnalysisEngineDescription ae = createAggregateDescription(
				candidateSet.createPreprocessingComponents(language),
				createPrimitiveDescription(
						TfidfConsumer.class,
						TfidfConsumer.PARAM_FEATURE_PATH, candidateSet.getFeaturePath(),
						TfidfConsumer.PARAM_LOWERCASE, true,
						TfidfConsumer.PARAM_OUTPUT_PATH, outputFile.getAbsolutePath()));

		SimplePipeline.runPipeline(reader, ae);
		}

		catch (ResourceInitializationException e) {
			throw new RuntimeException(e);
		}
		catch (UIMAException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

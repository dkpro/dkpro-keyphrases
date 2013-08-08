package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;
import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.ngrams.NGramAnnotator;
import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordNamedEntityRecognizer;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerChunkerTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerPosLemmaTT4J;
import de.tudarmstadt.ukp.dkpro.core.treetagger.TreeTaggerTT4JBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.KeyphraseMerger;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.StopwordFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.StructureFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.util.KeyphraseScoreComparator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;

// TODO for certain candidates, we need to run other preprocessing before.
// Probably we should simply run all necessary preprocessing all the time.
// Other possibility would be to change aggregate according to which candidate is selected.

// TODO some extractors need to build models before starting the extraction process. How to handle that?

// TODO add means for xmi serialization after preprocessing?
public abstract class KeyphraseExtractor_ImplBase
    implements KeyphraseExtractor, KeyphraseConstants
{

    /**
     * Default is English ("en").
     */
    private String language = "en";

	/**
     * Which candidate should be used in the keyphrase extractor.
     * For example, it could be set to "Token" with a POS filter keeping only nouns and adjectives.
     * Default is NounPhrase.
     */
    private Candidate candidate = new Candidate(CandidateType.NounPhrase);

    /**
     * The minimum length of keyphrases.
     * Default is 1.
     */
    private int minKeyphraseLength = 1;

    /**
     * The maximum length of keyphrases.
     * Default is 4.
     */
    private int maxKeyphraseLength = 4;

    private AnalysisEngine keyphraseEngine = null;

    public List<Keyphrase> extract(String inputText) throws IOException {

        JCas jcas = null;
        try {
            jcas = getKeyphraseEngine().newJCas();
            jcas.setDocumentText(inputText);

            getKeyphraseEngine().process(jcas);
        }
        catch (ResourceInitializationException e) {
            throw new RuntimeException(e);
        }
        catch (AnalysisEngineProcessException e) {
            throw new RuntimeException(e);
        }

        List<Keyphrase> resultKeyphrases = new ArrayList<Keyphrase>();
        for (Keyphrase keyphrase : select(jcas, Keyphrase.class)) {
            resultKeyphrases.add(keyphrase);
        }

        Collections.sort(resultKeyphrases, new KeyphraseScoreComparator());

        return resultKeyphrases;
    }

    protected abstract AnalysisEngineDescription createKeyphraseExtractorAggregate() throws ResourceInitializationException;

    protected AnalysisEngineDescription createPreprocessingComponents(CandidateType type) throws ResourceInitializationException {

        List<AnalysisEngineDescription> list = new ArrayList<AnalysisEngineDescription>();

        // add general components
        list.add(createPrimitiveDescription(BreakIteratorSegmenter.class));

        list.add(
                createTagger(language)  // we always need lemmatization for filtering stopwords
        );



        // add special components needed for certain types
        switch (type) {
            case Stem :
                list.add(
                        createPrimitiveDescription(
                                SnowballStemmer.class,
                                SnowballStemmer.PARAM_LANGUAGE, language
                        )
                    );
                break;
            case NamedEntity :
                list.add(
                        createPrimitiveDescription(
                                StanfordNamedEntityRecognizer.class,
                                StanfordNamedEntityRecognizer.PARAM_MODEL_LOCATION, "resource/StanfordNLP/ner-eng-ie.crf-3-all2008.ser.gz"
                        )
                    );
                break;
            case NounPhrase :
                list.add( createChunker(language) );
                break;
            case NGram :
                list.add(
                        createPrimitiveDescription(
                            NGramAnnotator.class,
                            NGramAnnotator.PARAM_N, 5
                        )
                    );
                break;
            default :
                break;
        }

        // always add the candidate annotator at last component here
        list.add(
                getCandidate().getCandidateAnnotator()
        );


        return createAggregateDescription(
                list.toArray(new AnalysisEngineDescription[list.size()])
        );
    }

    protected AnalysisEngineDescription createPostprocessingComponents() throws ResourceInitializationException {

        List<AnalysisEngineDescription> list = new ArrayList<AnalysisEngineDescription>();

        list.add(
            createPrimitiveDescription(
                    KeyphraseMerger.class,
                    KeyphraseMerger.PARAM_MAX_LENGTH, getMaxKeyphraseLength()
            )
        );

        list.add( createStopwordFilter(getLanguage()) );

        list.add(
            createPrimitiveDescription(
                    StructureFilter.class,
                    StructureFilter.PARAM_MIN_TOKENS, getMinKeyphraseLength(),
                    StructureFilter.PARAM_MAX_TOKENS, getMaxKeyphraseLength(),
                    StructureFilter.PARAM_POS_PATTERNS, false
            )
        );

        return createAggregateDescription(
                list.toArray(new AnalysisEngineDescription[list.size()])
        );
    }

    // TODO TreeTagger is now available for more languages
    private AnalysisEngineDescription createTagger(String language) throws ResourceInitializationException {
        if (language.equals("en") || language.equals("de") || language.equals("ru")) {
            return createPrimitiveDescription(
                    TreeTaggerPosLemmaTT4J.class,
                    TreeTaggerTT4JBase.PARAM_LANGUAGE, getLanguage()
            );
        }
        else {
            throw new ResourceInitializationException(new Throwable("No tagger available for language: " + language));
        }
    }

    private AnalysisEngineDescription createChunker(String language) throws ResourceInitializationException {
        if (language.equals("en") || language.equals("de") || language.equals("ru")) {
            return createPrimitiveDescription(
                    TreeTaggerChunkerTT4J.class,
                    TreeTaggerChunkerTT4J.PARAM_LANGUAGE, getLanguage()
            );
        }
        else {
            throw new ResourceInitializationException(new Throwable("No tagger available for language: " + language));
        }
    }

    private AnalysisEngineDescription createStopwordFilter(String language) throws ResourceInitializationException {
        if (language.equals("en")) {
            return createPrimitiveDescription(
                    StopwordFilter.class,
                    StopwordFilter.PARAM_STOPWORD_LIST, "classpath:/stopwords/english_stopwords.txt"
            );
        }
        else if (language.equals("de")) {
            return createPrimitiveDescription(
                    StopwordFilter.class,
                    StopwordFilter.PARAM_STOPWORD_LIST, "classpath:/stopwords/german_stopwords.txt"
            );
        }
        else {
            throw new ResourceInitializationException(new Throwable("No stopwordlist for available for language: " + language));
        }
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public AnalysisEngine getKeyphraseEngine()
        throws IOException
    {
        if (keyphraseEngine == null) {
            try {
                keyphraseEngine = AnalysisEngineFactory.createAggregate(createKeyphraseExtractorAggregate());
            }
            catch (ResourceInitializationException e) {
                throw new IOException(e);
            }
        }

        return keyphraseEngine;
    }

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setMinKeyphraseLength(int minKeyphraseLength) {
		this.minKeyphraseLength = minKeyphraseLength;
	}

	public int getMinKeyphraseLength() {
		return minKeyphraseLength;
	}

	public void setMaxKeyphraseLength(int maxKeyphraseLength) {
		this.maxKeyphraseLength = maxKeyphraseLength;
	}

	public int getMaxKeyphraseLength() {
		return maxKeyphraseLength;
	}

	public String getConfigurationDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append("Extractor: " + this.getName()); sb.append(LF);
		sb.append("Minimum Keyphrase Length: " + this.getMinKeyphraseLength()); sb.append(LF);
		sb.append("Maximum Keyphrase Length: " + this.getMaxKeyphraseLength()); sb.append(LF);
		sb.append("Language: " + this.getLanguage()); sb.append(LF);
		sb.append("Candidate: "); sb.append(LF);
		sb.append(this.getCandidate()); sb.append(LF);
		sb.append(LF);
		return sb.toString();
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}

package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import kea.stemmers.PorterStemmer;
import kea.stopwords.Stopwords;
import kea.stopwords.StopwordsGerman;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util.FileStopwords;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util.KEAKeyphraseExtractor;

/**
 * Keyword annotator based on KEA
 *
 * @author Florian Schwager, zesch
 *
 */
// TODO get rid of property files
// Why are they needed anyway?
public class KeaKeyphraseExtractorWrapper extends JCasAnnotator_ImplBase {

    public static final String PARAM_LANG = "Language";
    @ConfigurationParameter(name=PARAM_LANG, mandatory=true)
    private String language;

    public static final String PARAM_NUMBER_PHRASES = "NumberKeyphrases";
    @ConfigurationParameter(name=PARAM_NUMBER_PHRASES, mandatory=true)
    private int nrofKeyphrases;

    public static final String PARAM_MAX_PHRASE_LENGTH = "MaxKeyphraseLength";
    @ConfigurationParameter(name=PARAM_MAX_PHRASE_LENGTH, mandatory=true)
    private int maxKeyphraseLength;

    public static final String PARAM_MODEL = "KeyphraseModel";
    @ConfigurationParameter(name=PARAM_MODEL, mandatory=true)
    private String keyphraseModel;

    public static final String PARAM_KEA_PROPERTIES = "KeaPropertiesFile";
    @ConfigurationParameter(name=PARAM_KEA_PROPERTIES, mandatory=true)
    private String keaPropertiesFile;

	private JCas jcas;

	private final KEAKeyphraseExtractor kea = new KEAKeyphraseExtractor();

	private final String stopwords_location = "src/main/resources/Kea/stopwords/stopwords_";
	
    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context);

        getContext().getLogger().log(Level.CONFIG, "Initializing " + this.getClass().getSimpleName());

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(new File(keaPropertiesFile)));

            kea.setModelName(keyphraseModel);           // parameter overrides properties
            kea.setMaxPhraseLength(maxKeyphraseLength); // parameter overrides properties
            kea.setDocumentLanguage(language);          // parameter overrides properties
            kea.setNumPhrases(nrofKeyphrases);          // parameter overrides properties
            kea.setVocabulary(props.getProperty("Vocabulary"));
            kea.setVocabularyFormat(props.getProperty("VocabularyFormat"));
            kea.setEncoding(props.getProperty("Encoding"));
            kea.setBuildGlobal(false);
            if (Boolean.parseBoolean(props.getProperty("Stemmer"))) {
                kea.setStemmer(new PorterStemmer());
            }
            Stopwords sw = null;
            if (Boolean.parseBoolean(props.getProperty("Stopwords"))) {
                if ("de".equals(language)) {
                    sw = new StopwordsGerman();
                }
                else {
                    sw = new FileStopwords(stopwords_location + language + ".txt");
                }
            }
            kea.setStopwords(sw);

            kea.loadModel();
        } catch (Exception e) {
            getContext().getLogger().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            throw new ResourceInitializationException(e);
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        this.jcas = jcas;

        getContext().getLogger().log(Level.INFO, "Entering " + this.getClass().getSimpleName());

        try {
            List<String> phrases = kea.extractKeyphrases(jcas.getDocumentText());
            int i=1;
            for (String phrase : phrases) {
                getContext().getLogger().log(Level.FINE, "Kea keyphrase: " + phrase);
                addExactKeyphrase(phrase,i);
                i++;
            }
        } catch (Exception e) {
            getContext().getLogger().log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            throw new AnalysisEngineProcessException();
        }
    }

    private void addExactKeyphrase(String phrase, int rank) throws AnalysisEngineProcessException {
        String documentText = jcas.getDocumentText();
        if (documentText.contains(phrase)) {
          int offset = documentText.indexOf(phrase);

          double score = 1.00 - rank * 0.0001;  // pseudo score that scores the keyphrases in the order they are returned by KEA
          Keyphrase keyphrase = new Keyphrase(jcas);
          keyphrase.setBegin(offset);
          keyphrase.setEnd(offset+phrase.length());
          keyphrase.setKeyphrase(phrase);
          keyphrase.setScore(score);
          keyphrase.addToIndexes();

        }
        else {
            getContext().getLogger().log(Level.SEVERE, "Phrase '" + phrase + " not found in text: " + documentText);
        }

    }
}
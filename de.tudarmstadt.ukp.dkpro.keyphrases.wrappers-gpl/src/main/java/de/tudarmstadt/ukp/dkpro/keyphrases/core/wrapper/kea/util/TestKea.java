package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util;


import java.io.File;
import java.io.FileReader;

import kea.stemmers.PorterStemmer;

/**
 * Test environment for the KEA key-phrase extraction.
 * 
 * @author Florian Schwager
 * 
 */
public class TestKea {

    /**
     * Main method.
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        TestKea test = new TestKea();

        System.out.println("Creating the model... ");
        test.setOptionsTraining();
        test.createModel();

        System.out.println("Extracting keyphrases from test documents... ");
        test.setOptionsTesting();
        test.extractKeyphrases();
    }

    private KEAModelBuilder km;

    private KEAKeyphraseExtractor ke;

    /**
     * Creates the model for the KEA algorithm.
     */
    private void createModel() {
        try {
            km.buildModel(km.findDocumentIds());
            km.saveModel();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Extracts key-phrases.
     */
    private void extractKeyphrases() {
        try {
            ke.loadModel();
            StringBuffer buffer = new StringBuffer();
            FileReader fr = new FileReader(new File("src/main/resources/Kea/testdocs/en/test/bostid_b12sae.txt"));
            while (fr.ready()) {
                buffer.append((char) fr.read());
            }
            ke.extractKeyphrases(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    /**
     * Sets options for the key-phrase extraction.
     */
    private void setOptionsTesting() {
        ke = new KEAKeyphraseExtractor();
        ke.setModelName("src/main/resources/Kea/test.model");
        ke.setVocabulary("none");
        ke.setVocabularyFormat("");
        ke.setEncoding("UTF-8");
        ke.setDocumentLanguage("en");
        ke.setStemmer(new PorterStemmer());
        ke.setStopwords(new FileStopwords("src/main/resources/Kea/stopwords/stopwords_en.txt"));
        ke.setNumPhrases(10);
        ke.setBuildGlobal(false);
        ke.setDebug(true);
    }

    /**
     * Sets options for the model creation.
     */
    private void setOptionsTraining() {
        km = new KEAModelBuilder();
        km.setDirName("src/main/resources/Kea/testdocs/en/train");
        km.setModelName("src/main/resources/Kea/test.model");
        km.setVocabulary("none");
        km.setVocabularyFormat("");
        km.setEncoding("UTF-8");
        km.setDocumentLanguage("en");
        km.setStemmer(new PorterStemmer());
        km.setStopwords(new FileStopwords("src/main/resources/Kea/stopwords/stopwords_en.txt"));
        km.setMaxPhraseLength(5);
        km.setMinPhraseLength(1);
        km.setMinNumOccur(2);
        km.setDebug(true);
    }
}
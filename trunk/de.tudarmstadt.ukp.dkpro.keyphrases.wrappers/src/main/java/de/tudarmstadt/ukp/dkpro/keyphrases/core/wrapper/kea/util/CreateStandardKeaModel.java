package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util;

import kea.stemmers.PorterStemmer;

public class CreateStandardKeaModel {
	
    final static String KEA_DIR = "src/main/resources/Kea/";
	final static String TRAIN_DIR_EN = KEA_DIR + "testdocs/en/";
    final static String TRAIN_DIR_ES = KEA_DIR + "testdocs/es/";
    final static String TRAIN_DIR_FR = KEA_DIR + "testdocs/fr/";
	final static String MODEL_SUFFIX = ".model";

	/**
	 * This method builds all models
	 * @param args Name of model or name of training directory and model name 
	 */
	public static void main(String[] args) {
		CreateStandardKeaModel builder = new CreateStandardKeaModel();
		
		if (args.length == 3) {
			builder.buildModel(args[0], args[1], args[2]);
		}
		else{
			builder.buildModel(
		        TRAIN_DIR_EN + "train",
		        KEA_DIR + "models/kea_en" + MODEL_SUFFIX,
		        KEA_DIR + "stopwords/stopwords_en.txt"
			);
            builder.buildModel(
                TRAIN_DIR_ES + "train",
                KEA_DIR + "models/kea_es" + MODEL_SUFFIX,
                KEA_DIR + "stopwords/stopwords_es.txt"
            );
            builder.buildModel(
                TRAIN_DIR_FR + "train",
                KEA_DIR + "models/kea_fr" + MODEL_SUFFIX,
                KEA_DIR + "stopwords/stopwords_fr.txt"
            );
		}
	}
	
	/**
	 * generic method to build a model
	 * @param trainingName Full path of training directory
	 * @param modelName Name and full path of model
	 */
	public void buildModel(String trainingName, String modelName, String stopwords){
        KEAModelBuilder km = new KEAModelBuilder();
        km.setDirName(trainingName);
        km.setModelName(modelName);
        km.setVocabulary("none");
        km.setVocabularyFormat("");
        km.setEncoding("UTF-8");
        km.setDocumentLanguage("en");
        km.setStemmer(new PorterStemmer());
        km.setStopwords(new FileStopwords(stopwords));
        km.setMaxPhraseLength(8);
        km.setMinPhraseLength(1);
        km.setMinNumOccur(2);
        km.setDebug(false);

        try {
            km.buildModel(km.findDocumentIds());
            km.saveModel();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }	

}

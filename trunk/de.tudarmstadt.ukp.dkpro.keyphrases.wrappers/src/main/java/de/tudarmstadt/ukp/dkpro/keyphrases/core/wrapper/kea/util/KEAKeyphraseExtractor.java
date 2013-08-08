package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import kea.stemmers.Stemmer;
import kea.stopwords.Stopwords;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;

/**
 * Extracts keyphrases from the documents in a given directory. Assumes that the
 * file names for the documents end with ".txt". Puts extracted keyphrases into
 * corresponding files ending with ".key" (if those are not already present).
 * Optionally an encoding for the documents/keyphrases can be defined (e.g. for
 * Chinese text). Documents for which ".key" exists, are used for evaluation.
 * 
 * Valid options are:
 * <p>
 * 
 * -l "directory name"<br>
 * Specifies name of directory.
 * <p>
 * 
 * -m "model name"<br>
 * Specifies name of model.
 * <p>
 * 
 * -v "vocabulary name"<br>
 * Specifies name of vocabulary.
 * <p>
 * 
 * -f "vocabulary format"<br>
 * Specifies format of vocabulary (text or skos).
 * <p>
 * 
 * -i "document language" <br>
 * Specifies document language (en, es, de, fr).
 * <p>
 * 
 * -e "encoding"<br>
 * Specifies encoding.
 * <p>
 * 
 * -n <br>
 * Specifies number of phrases to be output (default: 5).
 * <p>
 * 
 * -t "name of class implementing stemmer"<br>
 * Sets stemmer to use (default: SremovalStemmer).
 * <p>
 * 
 * -s "name of class implementing stopwords"<br>
 * Sets stemmer to use (default: StopwordsEnglish).
 * <p>
 * 
 * -d<br>
 * Turns debugging mode on.
 * <p>
 * 
 * -g<br>
 * Build global dictionaries from the test set.
 * <p>
 * 
 * -a<br>
 * Also write stemmed phrase and score into ".key" file.
 * <p>
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version 1.0
 */
public class KEAKeyphraseExtractor implements OptionHandler {

	/** Name of model */
	String m_modelName = null;

	/** Name of vocabulary */
	String m_vocabulary = null;

	/** Vocabulary format */
	String m_vocabularyFormat = null;

	/** Document language */
	String m_documentLanguage = "en";

	/** Encoding */
	String m_encoding = "default";

	/** Debugging mode? */
	boolean m_debug = false;

	/** The KEA filter object */
	KEAFilter m_KEAFilter = null;

	/** The number of phrases to extract. */
	int m_numPhrases = 10;

    /** The maximum length of a phrase. */
    int m_MaxPhraseLength = 10;

    /** The stemmer to be used */
	private Stemmer m_Stemmer = null;// new SremovalStemmer();

	/** The list of stop words to be used */
	private Stopwords m_Stopwords = null;

	/** Also write stemmed phrase and score into .key file. */
	boolean m_AdditionalInfo = false;

	/** Build global dictionaries from the test set. */
	boolean m_buildGlobal = false;

	public List<String> extractKeyphrases(String text) throws Exception {
		m_KEAFilter.setNumPhrases(m_numPhrases);
		m_KEAFilter.setVocabulary(m_vocabulary);
		m_KEAFilter.setVocabularyFormat(m_vocabularyFormat);
		m_KEAFilter.setDocumentLanguage(getDocumentLanguage());
		m_KEAFilter.setStemmer(m_Stemmer);
		m_KEAFilter.setStopwords(m_Stopwords);
        m_KEAFilter.setMaxPhraseLength(m_MaxPhraseLength);

		if (getVocabulary().equals("none")) {
			m_KEAFilter.m_NODEfeature = false;
		} else {
			m_KEAFilter.loadThesaurus(m_Stemmer, m_Stopwords);
		}

		FastVector atts = new FastVector(3);
		atts.addElement(new Attribute("doc", (FastVector) null));
		atts.addElement(new Attribute("keyphrases", (FastVector) null));
		atts.addElement(new Attribute("filename", (String) null));
		Instances data = new Instances("keyphrase_training_data", atts, 0);

		if (m_KEAFilter.m_Dictionary == null) {
			throw new Exception("No dictionary specified in KEA model file.");
		}

		double[] newInst = new double[2];

		if (text != null) {
			newInst[0] = data.attribute(0).addStringValue(text);
		} else {

			newInst[0] = Instance.missingValue();
		}

		newInst[1] = Instance.missingValue();

		data.add(new Instance(1.0, newInst));

		m_KEAFilter.input(data.instance(0));

		data = data.stringFreeStructure();

		Instance[] topRankedInstances = new Instance[m_numPhrases];
		Instance inst;

		// Iterating over all extracted keyphrases (inst)
		while ((inst = m_KEAFilter.output()) != null) {
			int index = (int) inst.value(m_KEAFilter.getRankIndex()) - 1;
			if (index < m_numPhrases) {
				topRankedInstances[index] = inst;
			}
		}

		if (m_debug) {
			System.err.println("-- Keyphrases and feature values:");
		}
		List<String> phrases = new ArrayList<String>();
		for (int i = 0; i < m_numPhrases; i++) {
			Instance cur = topRankedInstances[i];
			if (cur != null) {
				phrases.add(cur.stringValue(m_KEAFilter
						.getUnstemmedPhraseIndex()));
			}
		}
		return phrases;
		// m_KEAFilter.batchFinished();
	}

	/**
	 * Get the value of AdditionalInfo.
	 * 
	 * @return Value of AdditionalInfo.
	 */
	public boolean getAdditionalInfo() {
		return m_AdditionalInfo;
	}

	/**
	 * Get the value of BuildGlobal.
	 * 
	 * @return Value of BuildGlobal.
	 */
	public boolean getBuildGlobal() {
		return m_buildGlobal;
	}

	/**
	 * Get the value of debug.
	 * 
	 * @return Value of debug.
	 */
	public boolean getDebug() {
		return m_debug;
	}

	/**
	 * Get the value of document language.
	 * 
	 * @return Value of document language.
	 */
	public String getDocumentLanguage() {
		return m_documentLanguage;
	}

	/**
	 * Get the value of encoding.
	 * 
	 * @return Value of encoding.
	 */
	public String getEncoding() {
		return m_encoding;
	}

	/**
	 * Get the value of modelName.
	 * 
	 * @return Value of modelName.
	 */
	public String getModelName() {
		return m_modelName;
	}

	/**
	 * Get the value of numPhrases.
	 * 
	 * @return Value of numPhrases.
	 */
	public int getNumPhrases() {
		return m_numPhrases;
	}

	/**
	 * Gets the current option settings.
	 * 
	 * @return an array of strings suitable for passing to setOptions
	 */
	public String[] getOptions() {
		String[] options = new String[21];
		int current = 0;

		options[current++] = "-m";
		options[current++] = "" + (getModelName());
		options[current++] = "-v";
		options[current++] = "" + (getVocabulary());
		options[current++] = "-f";
		options[current++] = "" + (getVocabularyFormat());
		options[current++] = "-e";
		options[current++] = "" + (getEncoding());
		options[current++] = "-i";
		options[current++] = "" + (getDocumentLanguage());
		options[current++] = "-n";
		options[current++] = "" + (getNumPhrases());
		options[current++] = "-t";
		options[current++] = "" + (getStemmer().getClass().getName());
		options[current++] = "-s";
		options[current++] = "" + (getStopwords().getClass().getName());

		if (getDebug()) {
			options[current++] = "-d";
		}

		if (getBuildGlobal()) {
			options[current++] = "-b";
		}

		if (getAdditionalInfo()) {
			options[current++] = "-a";
		}

		while (current < options.length) {
			options[current++] = "";
		}
		return options;
	}

	/**
	 * Get the Stemmer value.
	 * 
	 * @return the Stemmer value.
	 */
	public Stemmer getStemmer() {
		return m_Stemmer;
	}

	/**
	 * Get the Stopwords value.
	 * 
	 * @return the Stopwords value.
	 */
	public Stopwords getStopwords() {
		return m_Stopwords;
	}

	/**
	 * Get the value of vocabulary name.
	 * 
	 * @return Value of vocabulary name.
	 */
	public String getVocabulary() {
		return m_vocabulary;
	}

	/**
	 * Get the value of vocabulary format.
	 * 
	 * @return Value of vocabulary format.
	 */
	public String getVocabularyFormat() {
		return m_vocabularyFormat;
	}

    public int getMaxPhraseLength() {
        return m_MaxPhraseLength;
    }
    
	/**
	 * Returns an enumeration describing the available options.
	 * 
	 * @return an enumeration of all the available options
	 */
	public Enumeration<Option> listOptions() {
		Vector<Option> newVector = new Vector<Option>(13);

		newVector.addElement(new Option("\tSpecifies name of directory.", "l",
				1, "-l <directory name>"));
		newVector.addElement(new Option("\tSpecifies name of model.", "m", 1,
				"-m <model name>"));
		newVector.addElement(new Option("\tSpecifies vocabulary name.", "v", 1,
				"-v <vocabulary name>"));
		newVector.addElement(new Option("\tSpecifies vocabulary format.", "f",
				1, "-f <vocabulary format>"));
		newVector.addElement(new Option("\tSpecifies encoding.", "e", 1,
				"-e <encoding>"));
		newVector.addElement(new Option(
				"\tSpecifies document language (en (default), es, de, fr).",
				"i", 1, "-i <document language>"));
		newVector.addElement(new Option(
				"\tSpecifies number of phrases to be output (default: 5).",
				"n", 1, "-n"));
		newVector.addElement(new Option(
				"\tSet the stemmer to use (default: SremovalStemmer).", "t", 1,
				"-t <name of stemmer class>"));
		newVector
				.addElement(new Option(
						"\tSet the stopwords class to use (default: EnglishStopwords).",
						"s", 1, "-s <name of stopwords class>"));
		newVector.addElement(new Option("\tTurns debugging mode on.", "d", 0,
				"-d"));
		newVector
				.addElement(new Option(
						"\tBuilds global dictionaries for computing TFIDF from the test collection.",
						"b", 0, "-b"));
		newVector.addElement(new Option(
				"\tAlso write stemmed phrase and score into \".key\" file.",
				"a", 0, "-a"));

		return newVector.elements();
	}

	/**
	 * Loads the extraction model from the file.
	 */
	public void loadModel() throws Exception {
	    File modelFile = new File(m_modelName);
	    if (!modelFile.exists()) {
	        throw new FileNotFoundException(modelFile.getAbsolutePath());
	    }
	    
		BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(m_modelName));
		ObjectInputStream in = new ObjectInputStream(inStream);
		m_KEAFilter = (KEAFilter) in.readObject();

		// If TFxIDF values are to be computed from the test corpus
		if (m_buildGlobal == true) {
			if (m_debug) {
				System.err.println("-- The global dictionaries will be built from this test collection..");
			}
			m_KEAFilter.m_Dictionary = null;
		}
		in.close();
	}

	/**
	 * Set the value of AdditionalInfo.
	 * 
	 * @param newAdditionalInfo
	 *            Value to assign to AdditionalInfo.
	 */
	public void setAdditionalInfo(boolean newAdditionalInfo) {
		m_AdditionalInfo = newAdditionalInfo;
	}

	/**
	 * Set the value of BuildGlobal.
	 * 
	 * @param newBuildGlobal
	 *            Value to assign to BuildGlobal.
	 */
	public void setBuildGlobal(boolean newBuildGlobal) {
		m_buildGlobal = newBuildGlobal;
	}

	/**
	 * Set the value of debug.
	 * 
	 * @param newdebug
	 *            Value to assign to debug.
	 */
	public void setDebug(boolean newdebug) {
		m_debug = newdebug;
	}

	/**
	 * Set the value of document language.
	 * 
	 * @param newdocumentLanguage
	 *            Value to assign to document language.
	 */
	public void setDocumentLanguage(String newdocumentLanguage) {
		m_documentLanguage = newdocumentLanguage;
	}

	/**
	 * Set the value of encoding.
	 * 
	 * @param newencoding
	 *            Value to assign to encoding.
	 */
	public void setEncoding(String newencoding) {
		m_encoding = newencoding;
	}

	/**
	 * Set the value of modelName.
	 * 
	 * @param newmodelName
	 *            Value to assign to modelName.
	 */
	public void setModelName(String newmodelName) {
		m_modelName = newmodelName;
	}

	/**
	 * Set the value of numPhrases.
	 * 
	 * @param newnumPhrases
	 *            Value to assign to numPhrases.
	 */
	public void setNumPhrases(int newnumPhrases) {
		m_numPhrases = newnumPhrases;
	}

	/**
	 * Parses a given list of options controlling the behaviour of this object.
	 * Valid options are:
	 * <p>
	 * 
	 * -m "model name"<br>
	 * Specifies name of model.
	 * <p>
	 * 
	 * -v "vocabulary name"<br>
	 * Specifies vocabulary name.
	 * <p>
	 * 
	 * -f "vocabulary format"<br>
	 * Specifies vocabulary format.
	 * <p>
	 * 
	 * -i "document language" <br>
	 * Specifies document language.
	 * <p>
	 * 
	 * -e "encoding"<br>
	 * Specifies encoding.
	 * <p>
	 * 
	 * -n<br>
	 * Specifies number of phrases to be output (default: 5).
	 * <p>
	 * 
	 * -d<br>
	 * Turns debugging mode on.
	 * <p>
	 * 
	 * -b<br>
	 * Builds global dictionaries for computing TFxIDF from the test collection.
	 * <p>
	 * 
	 * -a<br>
	 * Also write stemmed phrase and score into ".key" file.
	 * <p>
	 * 
	 * @param options
	 *            the list of options as an array of strings
	 * @exception Exception
	 *                if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {
		String modelName = Utils.getOption('m', options);
		if (modelName.length() > 0) {
			setModelName(modelName);
		} else {
			setModelName(null);
			throw new Exception("Name of model required argument.");
		}

		String vocabularyName = Utils.getOption('v', options);
		if (vocabularyName.length() > 0) {
			setVocabulary(vocabularyName);
		} else {
			setVocabulary(null);
			throw new Exception("Name of vocabulary required argument.");
		}

		String vocabularyFormat = Utils.getOption('f', options);

		if (!getVocabulary().equals("none")) {
			if (vocabularyFormat.length() > 0) {
				if (vocabularyFormat.equals("skos")
						|| vocabularyFormat.equals("text")) {
					setVocabularyFormat(vocabularyFormat);
				} else {
					throw new Exception(
							"Unsupported format of vocabulary. It should be either \"skos\" or \"text\".");
				}
			} else {
				setVocabularyFormat(null);
				throw new Exception(
						"If a controlled vocabulary is used, format of vocabulary required argument (skos or text).");
			}
		} else {
			setVocabularyFormat(null);
		}

		String encoding = Utils.getOption('e', options);
		if (encoding.length() > 0) {
			setEncoding(encoding);
		} else {
			setEncoding("default");
		}

		String documentLanguage = Utils.getOption('i', options);
		if (documentLanguage.length() > 0) {
			setDocumentLanguage(documentLanguage);
		} else {
			setDocumentLanguage("en");
		}

		String numPhrases = Utils.getOption('n', options);
		if (numPhrases.length() > 0) {
			setNumPhrases(Integer.parseInt(numPhrases));
		} else {
			setNumPhrases(5);
		}

		String stemmerString = Utils.getOption('t', options);
		if (stemmerString.length() > 0) {
			stemmerString = "kea.stemmers.".concat(stemmerString);
			setStemmer((Stemmer) Class.forName(stemmerString).newInstance());
		}

		String stopwordsString = Utils.getOption('s', options);
		if (stopwordsString.length() > 0) {
			stopwordsString = "kea.stopwords.".concat(stopwordsString);
			setStopwords((Stopwords) Class.forName(stopwordsString)
					.newInstance());
		}

		setDebug(Utils.getFlag('d', options));
		setBuildGlobal(Utils.getFlag('b', options));
		setAdditionalInfo(Utils.getFlag('a', options));
		Utils.checkForRemainingOptions(options);
	}

	/**
	 * Set the Stemmer value.
	 * 
	 * @param newStemmer
	 *            The new Stemmer value.
	 */
	public void setStemmer(Stemmer newStemmer) {
		this.m_Stemmer = newStemmer;
	}

	/**
	 * Set the Stopwords value.
	 * 
	 * @param newStopwords
	 *            The new Stopwords value.
	 */
	public void setStopwords(Stopwords newStopwords) {
		this.m_Stopwords = newStopwords;
	}

	/**
	 * Set the value of vocabulary name.
	 * 
	 * @param newvocabulary
	 *            Value to assign to vocabulary name.
	 */
	public void setVocabulary(String newvocabulary) {
		m_vocabulary = newvocabulary;
	}

	/**
	 * Set the value of vocabulary format.
	 * 
	 * @param newvocabularyFormat
	 *            Value to assign to vocabularyFormat .
	 */
	public void setVocabularyFormat(String newvocabularyFormat) {
		m_vocabularyFormat = newvocabularyFormat;
	}

    public void setMaxPhraseLength(int maxPhraseLength) {
        m_MaxPhraseLength = maxPhraseLength;
    }
}

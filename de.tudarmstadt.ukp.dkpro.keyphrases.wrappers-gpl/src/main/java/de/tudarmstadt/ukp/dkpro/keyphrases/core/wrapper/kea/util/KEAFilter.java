/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import kea.filters.KEAPhraseFilter;
import kea.filters.NumbersFilter;
import kea.stemmers.SremovalStemmer;
import kea.stemmers.Stemmer;
import kea.stopwords.Stopwords;
import kea.util.Counter;
import kea.vocab.Vocabulary;
import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.RegressionByDiscretization;
import weka.core.Attribute;
import weka.core.Capabilities;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.core.Capabilities.Capability;
import weka.filters.Filter;
import weka.filters.supervised.attribute.Discretize;

/**
 * This filter converts the incoming data into data appropriate for keyphrase
 * classification. It assumes that the dataset contains two string attributes.
 * The first attribute should contain the text of a document. The second
 * attribute should contain the keyphrases associated with that document (if
 * present).
 * 
 * The filter converts every instance (i.e. document) into a set of instances,
 * one for each word-based n-gram in the document. The string attribute
 * representing the document is replaced by some numeric features, the estimated
 * probability of each n-gram being a keyphrase, and the rank of this phrase in
 * the document according to the probability. Each new instances also has a
 * class value associated with it. The class is "true" if the n-gram is a true
 * keyphrase, and "false" otherwise. Of course, if the input document doesn't
 * come with author-assigned keyphrases, the class values for that document will
 * be missing.
 * 
 * @author Eibe Frank (eibe@cs.waikato.ac.nz), Olena Medelyan
 *         (olena@cs.waikato.ac.nz)
 * @version 2.0
 */
public class KEAFilter extends Filter implements OptionHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The Vocabulary object */
	public static Vocabulary m_Vocabulary;

	/**
	 * Joins an array of strings to a single string.
	 */
	private static String join(String[] str) {
		String result = "";
		for (int i = 0; i < str.length; i++) {
			if (result != "") {
				result = result + " " + str[i];
			} else {
				result = str[i];
			}
		}
		return result;
	}

	/**
	 * Main method for testing this class.
	 * 
	 * @param argv
	 *            should contain arguments to the filter: use -h for help
	 */
	public static void main(String[] argv) {

		try {
			if (Utils.getFlag('b', argv)) {
				Filter.batchFilterFile(new KEAFilter(), argv);
			} else {
				Filter.filterFile(new KEAFilter(), argv);
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * Sorts an array of Strings into alphabetic order
	 * 
	 */
	public static String[] sort(String[] a) {

		// rename firstAt to reflect new role in alphabetic sorting
		int i, j, firstAt;

		for (i = 0; i < a.length - 1; i++) {
			firstAt = i;
			for (j = i + 1; j < a.length; j++) {
				// modify to preserve ordering of a String that starts with
				// upper case preceding the otherwise identical String that
				// has only lower case letters
				if (a[j].toUpperCase().compareTo(a[firstAt].toUpperCase()) < 0) {
					// reset firstAt
					firstAt = j;
				}
				// if identical when converted to all same case
				if (a[j].toUpperCase().compareTo(a[firstAt].toUpperCase()) == 0) {
					// but a[j] precedes when not converted
					if (a[j].compareTo(a[firstAt]) < 0) {
						// reset firstAt
						firstAt = j;
					}
				}
			}
			if (firstAt != i) {
				swap(i, firstAt, a);
			}
		}
		return a;
	} // end method selectionSort

	/**
	 * Splits a string at given character into an array (ALY)
	 */
	@SuppressWarnings("unchecked")
	private static String[] split(String str, String separator) {

		ArrayList lst = new ArrayList();
		String word = "";

		for (int i = 0; i < str.length(); i++) {
			int j = i + 1;
			String letter = str.substring(i, j);
			if (!letter.equalsIgnoreCase(separator)) {
				word = word + str.charAt(i);
			} else {
				lst.add(word);
				word = "";
			}
		}
		if (word != "") {
			lst.add(word);
		}
		String[] result = (String[]) lst.toArray(new String[lst.size()]);
		return result;
	}

	/**
	 * overloaded swap method: exchange 2 locations in an array of Strings.
	 */
	public static void swap(int loc1, int loc2, String[] a) {
		String temp = a[loc1];
		a[loc1] = a[loc2];
		a[loc2] = temp;
	} // end swap

	/** Index of attribute containing the documents */
	private int m_DocumentAtt = 0;

	/** Index of attribute containing the keyphrases */
	private int m_KeyphrasesAtt = 1;

	/** The maximum length of phrases */
	private int m_MaxPhraseLength = 5;

	/** The minimum length of phrases */
	private int m_MinPhraseLength = 1;

	/** The number of phrases to extract. */
	private int m_numPhrases = 10;

	/**
	 * Experimental! Number of human indexers (times a keyphrase appears in the
	 * keyphrase set)
	 */
	// adjust manually for >1 indexer
	private final int m_Indexers = 1;

	// end. Don't use these features with m_KFused or adjust indicies below.

	/** Should non-descriptors be replaced by corresponding descriptors? */
	private final boolean m_DESCRreplace = true;

	/** Is the node degree (number of related terms in candidate set) being used? */
	public boolean m_NODEfeature = true;

	/** Is the length of a phrase in words being used? */
	private final boolean m_LENGTHfeature = true;

	/**
	 * Experimental feature! If m_STDEVused = true, should the standard
	 * deviation of position of phrase occurrences be considered? If set to
	 * true, the indicies of features need to be adjusted in the code manually!
	 * 
	 */
	private final boolean m_STDEVfeature = false;
	/**
	 * Experimental feature! Is keyphrase frequency attribute being used? If set
	 * to true, adjust the indicies in the code!
	 */
	private boolean m_KFused = false;

	/** Flag for debugging mode */
	private boolean m_Debug = false;
	/** Determines whether internal periods are allowed */
	private boolean m_DisallowInternalPeriods = false;
	/** The minimum number of occurences of a phrase */
	private int m_MinNumOccur = 2;
	/** The number of features describing a phrase */
	private int m_NumFeatures = 2;

	/** Indices of attributes in m_ClassifierData */
	private final int m_TfidfIndex = 0;

	private final int m_FirstOccurIndex = 1;

	/** Indicies of attributes for new features */

	private final int m_LengthIndex = 2;// adjust!!

	private final int m_NodeIndex = 3; // decrease if removing the above value

	private final int m_STDEVIndex = 4; // adjust!!

	private final int m_KeyFreqIndex = 3;

	/** The punctuation filter used by this filter */
	private KEAPhraseFilter m_PunctFilter = null;

	/** The numbers filter used by this filter */
	private NumbersFilter m_NumbersFilter = null;

	/** The actual classifier used to compute probabilities */
	private Classifier m_Classifier = null;

	/** The dictionary containing the document frequencies */
	@SuppressWarnings("unchecked")
	public HashMap m_Dictionary = null;

	/** The dictionary containing the keyphrases */
	@SuppressWarnings("unchecked")
	private HashMap m_KeyphraseDictionary = null;

	/** The number of documents in the global frequencies corpus */
	private int m_NumDocs = 0;

	/** Template for the classifier data */
	private Instances m_ClassifierData = null;

	/** The default stemmer to be used */
	private Stemmer m_Stemmer = new SremovalStemmer();

	/** The list of stop words to be used */
	private Stopwords m_Stopwords = null;

	/** The default language to be used */
	private String m_documentLanguage = "en";

	/** The Vocabulary name */
	private String m_vocabulary = "agrovoc";

	/** The Vocabulary format */
	private String m_vocabularyFormat = "skos";

	/** Determines whether check for proper nouns is performed */
	private boolean m_CheckForProperNouns = true;

	/**
	 * Signify that this batch of input to the filter is finished. If the filter
	 * requires all instances prior to filtering, output() may now be called to
	 * retrieve the filtered instances.
	 * 
	 * @return true if there are instances pending output
	 * @exception Exception
	 *                if no input structure has been defined
	 */
	@Override
    public boolean batchFinished() throws Exception {

		if (getInputFormat() == null) {
			throw new Exception("No input instance format defined");
		}

		if (m_Dictionary == null) {
			buildGlobalDictionaries();
			buildClassifier();
			convertPendingInstances();
		}
		flushInput();
		m_NewBatch = true;
		return (numPendingOutput() != 0);
	}

	/**
	 * Builds the classifier.
	 */
	// aly: The main function, where everything important happens
	@SuppressWarnings("unchecked")
	private void buildClassifier() throws Exception {
		// Generate input format for classifier
		FastVector atts = new FastVector();
		for (int i = 0; i < getInputFormat().numAttributes(); i++) {
			if (i == m_DocumentAtt) {
				atts.addElement(new Attribute("TFxIDF"));
				atts.addElement(new Attribute("First_occurrence"));
				if (m_KFused) {
					atts.addElement(new Attribute("Keyphrase_frequency"));
				}
				if (m_STDEVfeature) {
					atts.addElement(new Attribute("Standard_deviation"));
				}
				if (m_NODEfeature) {
					atts.addElement(new Attribute("Relations_number"));
				}
				if (m_LENGTHfeature) {
					atts.addElement(new Attribute("Phrase_length"));
				}
			} else if (i == m_KeyphrasesAtt) {
				FastVector vals = new FastVector(2);
				vals.addElement("False");
				vals.addElement("True");
				// atts.addElement(new Attribute("Keyphrase?", vals));
				atts.addElement(new Attribute("Keyphrase?"));
			}
		}
		m_ClassifierData = new Instances("ClassifierData", atts, 0);
		m_ClassifierData.setClassIndex(m_NumFeatures);

		if (m_Debug) {
			System.err.println("--- Converting instances for classifier");
		}
		// Convert pending input instances into data for classifier
		for (int i = 0; i < getInputFormat().numInstances(); i++) {
			Instance current = getInputFormat().instance(i);

			// Get the key phrases for the document
			String keyphrases = current.stringValue(m_KeyphrasesAtt);
			HashMap hashKeyphrases = getGivenKeyphrases(keyphrases, false);
			HashMap hashKeysEval = getGivenKeyphrases(keyphrases, true);

			// Get the phrases for the document
			HashMap hash = new HashMap();
			int length = getPhrases(hash, current.stringValue(m_DocumentAtt));
			// hash = getComposits(hash);

			// Compute the feature values for each phrase and
			// add the instance to the data for the classifier

			Iterator it = hash.keySet().iterator();
			while (it.hasNext()) {
				String phrase = (String) it.next();
				FastVector phraseInfo = (FastVector) hash.get(phrase);

				double[] vals = featVals(phrase, phraseInfo, true,
						hashKeysEval, hashKeyphrases, length, hash);
				// System.err.println(vals);
				Instance inst = new Instance(current.weight(), vals);
				// .err.println(phrase + "\t" + inst.toString());
				m_ClassifierData.add(inst);
			}
		}

		if (m_Debug) {
			System.err.println("--- Building classifier");
		}

		// Build classifier

		// Uncomment if you want to use a different classifier
		// Caution: Other places in the code will have to be adjusted!!
		/*
		 * I. Naive Bayes: FilteredClassifier fclass = new FilteredClassifier();
		 * fclass.setClassifier(new weka.classifiers.bayes.NaiveBayesSimple());
		 * fclass.setFilter(new Discretize()); m_Classifier = fclass;
		 */

		// NaiveBayes nb = new NaiveBayes();
		// nb.setUseSupervisedDiscretization(true);
		// m_Classifier = nb;
		/*
		 * II. Linear Regression: LinearRegression lr = new LinearRegression();
		 * lr.setAttributeSelectionMethod(new weka.core.SelectedTag(1,
		 * LinearRegression.TAGS_SELECTION));
		 * lr.setEliminateColinearAttributes(false); lr.setDebug(false);
		 * 
		 * m_Classifier = lr;
		 */

		/*
		 * III. Bagging with REPTrees Bagging bagging = new Bagging();
		 * 
		 * String[] ops_bagging = { new String("-P"), new String("100"), new
		 * String("-S"), new String("1"), new String("-I"), new String("50")};
		 * 
		 */

		/*
		 * REPTree rept = new REPTree(); //results are worse!
		 * rept.setNoPruning(true); String[] ops_rept = { new String("-M"), new
		 * String("2"), new String("-V"), new String("0.0010"), new
		 * String("-N"), new String("3"), new String("-S"), new String("1"), new
		 * String("-L"), new String("1"),};
		 * 
		 * rept.setOptions(ops_rept); bagging.setClassifier(rept);
		 */

		// bagging.setOptions(ops_bagging);
		// FilteredClassifier fclass = new FilteredClassifier();
		// fclass.setClassifier(new REPTree());
		// fclass.setFilter(new Discretize());
		// bagging.setClassifier(fclass);
		// m_Classifier = bagging;
		RegressionByDiscretization rvd = new RegressionByDiscretization();
		FilteredClassifier fclass = new FilteredClassifier();
		fclass.setClassifier(new weka.classifiers.bayes.NaiveBayesSimple());
		fclass.setFilter(new Discretize());

		rvd.setClassifier(fclass);
		rvd.setNumBins(m_Indexers + 1);
		m_Classifier = rvd;

		// System.out.print(m_ClassifierData);
		// System.exit(1);
		m_Classifier.buildClassifier(m_ClassifierData);

		if (m_Debug) {
			System.err.println(m_Classifier);
		}

		// Save space
		m_ClassifierData = new Instances(m_ClassifierData, 0);
	}

	/**
	 * Builds the global dictionaries.
	 */
	@SuppressWarnings("unchecked")
	public void buildGlobalDictionaries() throws Exception {
		if (m_Debug) {
			System.err.println("--- Building global dictionaries");
		}

		// Build dictionary of n-grams with associated
		// document frequencies
		m_Dictionary = new HashMap();
		for (int i = 0; i < getInputFormat().numInstances(); i++) {
			String str = getInputFormat().instance(i)
					.stringValue(m_DocumentAtt);
			HashMap hash = getPhrasesForDictionary(str);
			Iterator it = hash.keySet().iterator();
			while (it.hasNext()) {
				String phrase = (String) it.next();
				Counter counter = (Counter) m_Dictionary.get(phrase);
				if (counter == null) {
					m_Dictionary.put(phrase, new Counter());
				} else {
					counter.increment();
				}
			}
		}

		if (m_KFused) {
			if (m_Debug) {
				System.out.println("KF_used feature");
			}

			// Build dictionary of n-grams that occur as keyphrases
			// with associated keyphrase frequencies
			m_KeyphraseDictionary = new HashMap();
			for (int i = 0; i < getInputFormat().numInstances(); i++) {
				String str = getInputFormat().instance(i).stringValue(
						m_KeyphrasesAtt);
				HashMap hash = getGivenKeyphrases(str, false);
				if (hash != null) {
					Iterator it = hash.keySet().iterator();
					while (it.hasNext()) {
						String phrase = (String) it.next();
						Counter counter = (Counter) m_KeyphraseDictionary
								.get(phrase);
						if (counter == null) {
							m_KeyphraseDictionary.put(phrase, new Counter());
						} else {
							counter.increment();
						}
					}
				}
			}
		} else {
			m_KeyphraseDictionary = null;
		}

		// Set the number of documents in the global corpus
		m_NumDocs = getInputFormat().numInstances();
	}

	/**
	 * Converts an instance.
	 */
	@SuppressWarnings("unchecked")
	private FastVector convertInstance(Instance instance, boolean training)
			throws Exception {

		FastVector vector = new FastVector();

		if (m_Debug) {
			System.err.println("-- Converting instance");
		}

		// Get the key phrases for the document
		HashMap hashKeyphrases = null;
		HashMap hashKeysEval = null;
		if (!instance.isMissing(m_KeyphrasesAtt)) {
			String keyphrases = instance.stringValue(m_KeyphrasesAtt);
			hashKeyphrases = getGivenKeyphrases(keyphrases, false);
			hashKeysEval = getGivenKeyphrases(keyphrases, true);
		}

		// Get the phrases for the document
		HashMap hash = new HashMap();
		int length = getPhrases(hash, instance.stringValue(m_DocumentAtt));
		// hash = getComposits(hash);

		/*
		 * Experimental: To compute how many of the manual keyphrases appear in
		 * the documents:
		 * 
		 * System.err.println("Doc phrases found " + hash.size());
		 * System.err.println("Manual keyphrases: "); Iterator iter =
		 * hashKeyphrases.keySet().iterator(); int count = 0; while
		 * (iter.hasNext()) { String id = (String)iter.next(); if
		 * (hash.containsKey(id)) { count++; } }
		 * 
		 * double max_recall = (double)count/(double)hashKeyphrases.size();
		 * 
		 * 
		 * m_max_recall += max_recall; doc++; double avg_m_max_recall =
		 * m_max_recall/(double)doc;
		 * 
		 * String file = instance.stringValue(2); System.err.println(count + "
		 * out of " + hashKeyphrases.size() + " are in the document ");
		 * System.err.println("Max recall : " + avg_m_max_recall + " on " + doc + "
		 * documents ");
		 */

		// Compute number of extra attributes
		int numFeatures = 5;
		if (m_Debug) {
			if (m_KFused) {
				numFeatures = numFeatures + 1;
			}
		}
		if (m_STDEVfeature) {
			numFeatures = numFeatures + 1;
		}
		if (m_NODEfeature) {
			numFeatures = numFeatures + 1;
		}
		if (m_LENGTHfeature) {
			numFeatures = numFeatures + 1;
		}

		// Set indices of key attributes
		// int phraseAttIndex = m_DocumentAtt;
		int tfidfAttIndex = m_DocumentAtt + 2;
		int distAttIndex = m_DocumentAtt + 3;
		int probsAttIndex = m_DocumentAtt + numFeatures - 1;
		// int classAttIndex = numFeatures;

		// Go through the phrases and convert them into instances
		Iterator it = hash.keySet().iterator();
		while (it.hasNext()) {
			String id = (String) it.next();
			FastVector phraseInfo = (FastVector) hash.get(id);

			double[] vals = featVals(id, phraseInfo, training, hashKeysEval,
					hashKeyphrases, length, hash);

			Instance inst = new Instance(instance.weight(), vals);

			inst.setDataset(m_ClassifierData);

			// Get probability of a phrase being key phrase
			double[] probs = m_Classifier.distributionForInstance(inst);

			// If simple Naive Bayes used, change here to
			// double prob = probs[1];
			double prob = probs[0];

			// Compute attribute values for final instance
			double[] newInst = new double[instance.numAttributes()
					+ numFeatures];
			int pos = 0;
			for (int i = 0; i < instance.numAttributes(); i++) {
				if (i == m_DocumentAtt) {

					// output of values for a given phrase:

					// Add phrase
					int index = outputFormatPeek().attribute(pos)
							.addStringValue(id);
					newInst[pos++] = index;

					// Add original version
					String orig = (String) phraseInfo.elementAt(2);

					if (orig != null) {
						index = outputFormatPeek().attribute(pos)
								.addStringValue(orig);
					} else {
						index = outputFormatPeek().attribute(pos)
								.addStringValue(id);
					}
					newInst[pos++] = index;

					// Add TFxIDF
					newInst[pos++] = inst.value(m_TfidfIndex);

					// Add distance
					newInst[pos++] = inst.value(m_FirstOccurIndex);

					// Add other features
					if (m_Debug) {
						if (m_KFused) {
							newInst[pos++] = inst.value(m_KeyFreqIndex);
						}
					}
					if (m_STDEVfeature) {
						newInst[pos++] = inst.value(m_STDEVIndex);
					}
					if (m_NODEfeature) {
						newInst[pos++] = inst.value(m_NodeIndex);
					}
					if (m_LENGTHfeature) {
						newInst[pos++] = inst.value(m_LengthIndex);
					}

					// Add probability
					probsAttIndex = pos;
					newInst[pos++] = prob;

					// Set rank to missing (computed below)
					newInst[pos++] = Instance.missingValue();

				} else if (i == m_KeyphrasesAtt) {
					newInst[pos++] = inst.classValue();
				} else {
					newInst[pos++] = instance.value(i);
				}
			}
			Instance ins = new Instance(instance.weight(), newInst);
			ins.setDataset(outputFormatPeek());
			vector.addElement(ins);
		}

		// Add dummy instances for keyphrases that don't occur
		// in the document
		if (hashKeysEval != null) {
			Iterator phrases = hashKeysEval.keySet().iterator();
			while (phrases.hasNext()) {
				String phrase = (String) phrases.next();
				double[] newInst = new double[instance.numAttributes()
						+ numFeatures];
				int pos = 0;
				for (int i = 0; i < instance.numAttributes(); i++) {
					if (i == m_DocumentAtt) {
						// System.out.println("Here: " + phrase);
						// Add phrase
						int index = outputFormatPeek().attribute(pos)
								.addStringValue(phrase);
						newInst[pos++] = index;

						// Add original version
						index = outputFormatPeek().attribute(pos)
								.addStringValue(phrase);
						newInst[pos++] = index;

						// Add TFxIDF
						newInst[pos++] = Instance.missingValue();

						// Add distance
						newInst[pos++] = Instance.missingValue();

						// Add other features
						if (m_Debug) {
							if (m_KFused) {
								newInst[pos++] = Instance.missingValue();
							}
						}
						if (m_STDEVfeature) {
							newInst[pos++] = Instance.missingValue();
						}
						if (m_NODEfeature) {
							newInst[pos++] = Instance.missingValue();
						}
						if (m_LENGTHfeature) {
							newInst[pos++] = Instance.missingValue();
						}

						// Add probability and rank
						newInst[pos++] = -Double.MAX_VALUE;
						// newInst[pos++] = Instance.missingValue();
					} else if (i == m_KeyphrasesAtt) {
						newInst[pos++] = 1; // Keyphrase
					} else {
						newInst[pos++] = instance.value(i);
					}

					Instance inst = new Instance(instance.weight(), newInst);
					inst.setDataset(outputFormatPeek());
					vector.addElement(inst);
				}

			}
		}

		// Sort phrases according to their distance (stable sort)
		double[] vals = new double[vector.size()];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = ((Instance) vector.elementAt(i)).value(distAttIndex);
		}
		FastVector newVector = new FastVector(vector.size());
		int[] sortedIndices = Utils.stableSort(vals);
		for (int i = 0; i < vals.length; i++) {
			newVector.addElement(vector.elementAt(sortedIndices[i]));
		}
		vector = newVector;

		// Sort phrases according to their tfxidf value (stable sort)
		for (int i = 0; i < vals.length; i++) {
			vals[i] = -((Instance) vector.elementAt(i)).value(tfidfAttIndex);
		}
		newVector = new FastVector(vector.size());
		sortedIndices = Utils.stableSort(vals);
		for (int i = 0; i < vals.length; i++) {
			newVector.addElement(vector.elementAt(sortedIndices[i]));
		}
		vector = newVector;

		// Sort phrases according to their probability (stable sort)
		for (int i = 0; i < vals.length; i++) {
			vals[i] = 1 - ((Instance) vector.elementAt(i)).value(probsAttIndex);
		}
		newVector = new FastVector(vector.size());
		sortedIndices = Utils.stableSort(vals);
		for (int i = 0; i < vals.length; i++) {
			newVector.addElement(vector.elementAt(sortedIndices[i]));
		}
		vector = newVector;

		// Compute rank of phrases. Check for subphrases that are ranked
		// lower than superphrases and assign probability -1 and set the
		// rank to Integer.MAX_VALUE
		int rank = 1;
		for (int i = 0; i < vals.length; i++) {
			Instance currentInstance = (Instance) vector.elementAt(i);
			// Short cut: if phrase very unlikely make rank very low and
			// continue
			if (Utils.grOrEq(vals[i], 1.0)) {
				currentInstance.setValue(probsAttIndex + 1, Integer.MAX_VALUE);
				continue;
			}

			// Otherwise look for super phrase starting with first phrase
			// in list that has same probability, TFxIDF value, and distance as
			// current phrase. We do this to catch all superphrases
			// that have same probability, TFxIDF value and distance as current
			// phrase.
			int startInd = i;
			while (startInd < vals.length) {
				Instance inst = (Instance) vector.elementAt(startInd);
				if ((inst.value(tfidfAttIndex) != currentInstance
						.value(tfidfAttIndex))
						|| (inst.value(probsAttIndex) != currentInstance
								.value(probsAttIndex))
						|| (inst.value(distAttIndex) != currentInstance
								.value(distAttIndex))) {
					break;
				}
				startInd++;
			}
			currentInstance.setValue(probsAttIndex + 1, rank++);

		}
		return vector;
	}

	/**
	 * Sets output format and converts pending input instances.
	 */
	@SuppressWarnings("unchecked")
	private void convertPendingInstances() throws Exception {

		if (m_Debug) {
			System.err.println("--- Converting pending instances");
		}

		// Create output format for filter
		FastVector atts = new FastVector();
		for (int i = 0; i < getInputFormat().numAttributes(); i++) {
			if (i == m_DocumentAtt) {
				// string attributes
				atts.addElement(new Attribute("N-gram", (FastVector) null));
				atts.addElement(new Attribute("N-gram-original",
						(FastVector) null));
				// numeric attributes
				atts.addElement(new Attribute("TFxIDF"));
				atts.addElement(new Attribute("First_occurrence"));
				// optional attributes
				if (m_Debug) {
					if (m_KFused) {
						atts.addElement(new Attribute("Keyphrase_frequency"));
					}
				}
				if (m_STDEVfeature) {
					// FastVector rvals = new FastVector(2);
					// rvals.addElement("False");
					// rvals.addElement("True");
					atts.addElement(new Attribute("Standard_deviation"));
				}
				if (m_NODEfeature) {
					atts.addElement(new Attribute("Relations_number"));
				}
				if (m_LENGTHfeature) {
					atts.addElement(new Attribute("Phrase_length"));
				}

				atts.addElement(new Attribute("Probability"));
				atts.addElement(new Attribute("Rank"));
			} else if (i == m_KeyphrasesAtt) {
				FastVector vals = new FastVector(2);
				vals.addElement("False");
				vals.addElement("True");
				// atts.addElement(new Attribute("Keyphrase?", vals));
				atts.addElement(new Attribute("Keyphrase?"));
			} else {
				atts.addElement(getInputFormat().attribute(i));
			}
		}
		Instances outFormat = new Instances("KEAdata", atts, 0);
		setOutputFormat(outFormat);

		// Convert pending input instances into output data
		for (int i = 0; i < getInputFormat().numInstances(); i++) {
			Instance current = getInputFormat().instance(i);
			FastVector vector = convertInstance(current, true);
			Enumeration en = vector.elements();
			while (en.hasMoreElements()) {
				Instance inst = (Instance) en.nextElement();
				push(inst);
			}
		}
	}

	/**
	 * Conmputes the feature values for a given phrase.
	 */
	@SuppressWarnings("unchecked")
	private double[] featVals(String id, FastVector phraseInfo,
			boolean training, HashMap hashKeysEval, HashMap hashKeyphrases,
			int length, HashMap hash) {

		// Compute feature values
		Counter counterLocal = (Counter) phraseInfo.elementAt(1);
		double[] newInst = new double[m_NumFeatures + 1];

		// Compute TFxIDF
		Counter counterGlobal = (Counter) m_Dictionary.get(id);
		double localVal = counterLocal.value(), globalVal = 0;
		if (counterGlobal != null) {
			globalVal = counterGlobal.value();
			if (training) {
				globalVal = globalVal - 1;
			}
		}

		// Just devide by length to get approximation of probability
		// that phrase in document is our phrase
		// newInst[m_TfidfIndex] = (localVal / ((double)length));
		newInst[m_TfidfIndex] = (localVal / (length))
				* (-Math.log((globalVal + 1) / ((double) m_NumDocs + 1)));

		// Compute first occurrence
		Counter counterFirst = (Counter) phraseInfo.elementAt(0);
		newInst[m_FirstOccurIndex] = (double) counterFirst.value()
				/ (double) length;

		// Is keyphrase frequency attribute being used?
		if (m_KFused) {
			Counter keyphraseC = (Counter) m_KeyphraseDictionary.get(id);
			if ((training) && (hashKeyphrases != null)
					&& (hashKeyphrases.containsKey(id))) {
				newInst[m_KeyFreqIndex] = keyphraseC.value() - 1;
			} else {
				if (keyphraseC != null) {
					newInst[m_KeyFreqIndex] = keyphraseC.value();
				} else {
					newInst[m_KeyFreqIndex] = 0;
				}
			}
		}

		// Is term appearance attribute being used?
		if (m_STDEVfeature) {
			FastVector app = (FastVector) phraseInfo.elementAt(3);

			double[] vals = new double[app.size()];
			for (int i = 0; i < vals.length; i++) {
				vals[i] = ((Counter) app.elementAt(i)).value()
						/ (double) length;
				;
			}

			double mean = Utils.mean(vals);
			double summ = 0.0;
			for (int i = 0; i < vals.length; i++) {
				double a = vals[i];
				// System.err.println("Appearence " + i + " is at " + a);
				summ += (a - mean) * (a - mean);
			}
			double stdev = Math.sqrt(summ / app.size());

			newInst[m_STDEVIndex] = stdev;

			/*
			 * Using instead of STDEV feature a thesaurus based feature
			 * (experiment) if (m_Vocabulary.getRelated(id,"compositeOf") !=
			 * null) { //System.err.println(m_Vocabulary.getOrig(id) + " is a
			 * composite!"); newInst[m_STDEVIndex] = 1.0; } else {
			 * newInst[m_STDEVIndex] = 0.0; }
			 */

		}

		// Is node degree attribute being used?
		if (m_NODEfeature) {

			Vector idsRT = m_Vocabulary.getRelated(id);

			int intern = 0;
			if (idsRT != null) {
				for (int d = 0; d < idsRT.size(); d++) {
					if (hash.get(idsRT.elementAt(d)) != null) {
						intern++;
					}
				}
			}
			// System.out.println("Node feature for " + m_Vocabulary.getOrig(id)
			// + " = " + intern);

			newInst[m_NodeIndex] = intern;

		}

		// Is term length attribute being used?
		if (m_LENGTHfeature) {
			String original;
			if (m_vocabulary.equals("none")) {
				original = id;
			} else {
				original = m_Vocabulary.getOrig(id);
			}
			if (original == null) {
				System.err.println("problem with id " + id);
				newInst[m_LengthIndex] = 1.0;
			} else {
				String[] words = split(original, " ");
				newInst[m_LengthIndex] = words.length;
			}

		}

		// Compute class value

		if (hashKeysEval == null) { // no author-assigned keyphrases
			newInst[m_NumFeatures] = Instance.missingValue();
		} else if (!hashKeysEval.containsKey(id)) {

			newInst[m_NumFeatures] = 0; // Not a keyphrase

			// Experiment with giving phrases related to manually chosen one
			// higher values than to unrelated ones
			/*
			 * Vector related = (Vector)m_Vocabulary.getRelated(id); // if this
			 * id is related to one of the keyphrases, set its class value to
			 * 0.5 if (related != null) { Enumeration en = related.elements();
			 * while (en.hasMoreElements()) { String relID =
			 * (String)en.nextElement(); if (hashKeysEval.containsKey(relID)) {
			 * newInst[m_NumFeatures] = 1; // Keyphrase } } }
			 */

		} else {
			// hashKeysEval.remove(id);
			// newInst[m_NumFeatures] = 1; // Keyphrase

			// Learning from multiple-indexer's data
			// System.err.println(m_Indexers);
			// System.err.println("Calculating class value with m_Indexers = " +
			// m_Indexers);

			double c = (double) ((Counter) hashKeysEval.get(id)).value()
					/ m_Indexers;
			newInst[m_NumFeatures] = c; // Keyphrase

			// Or simple learning from 1 indexer:
			// newInst[m_NumFeatures] = 1.0; // Keyphrase
		}
		return newInst;
	}

	/**
	 * Returns the Capabilities of this filter.
	 * 
	 * @return the capabilities of this object
	 * @see Capabilities
	 */
	@Override
    public Capabilities getCapabilities() {
		Capabilities result = super.getCapabilities();

		// attributes
		result.enableAllAttributes();
		result.enable(Capability.MISSING_VALUES);

		// class
		result.enable(Capability.NOMINAL_CLASS);
		result.enable(Capability.NO_CLASS);
		result.enableAllClasses();

		// result.or(new LinearRegression().getCapabilities());

		return result;
	}

	/**
	 * Get the M_CheckProperNouns value.
	 * 
	 * @return the M_CheckProperNouns value.
	 */
	public boolean getCheckForProperNouns() {
		return m_CheckForProperNouns;
	}

	/**
	 * Get the value of Debug.
	 * 
	 * @return Value of Debug.
	 */
	public boolean getDebug() {
		return m_Debug;
	}

	/**
	 * Get whether the supplied columns are to be processed
	 * 
	 * @return true if the supplied columns won't be processed
	 */
	public boolean getDisallowInternalPeriods() {
		return m_DisallowInternalPeriods;
	}

	/**
	 * Get the value of DocumentAtt.
	 * 
	 * @return Value of DocumentAtt.
	 */
	public int getDocumentAtt() {
		return m_DocumentAtt;
	}

	/**
	 * Get the M_documentLanguage value.
	 * 
	 * @return the M_documentLanguage value.
	 */
	public String getDocumentLanguage() {
		return m_documentLanguage;
	}

	/**
	 * Gets all the phrases in the given string and puts them into the
	 * hashtable. Also stores the original version of the stemmed phrase in the
	 * hash table.
	 */
	@SuppressWarnings("unchecked")
	private HashMap getGivenKeyphrases(String str, boolean forEval) {

		HashMap hash = new HashMap();
		// m_Indexers = 1;

		StringTokenizer tok = new StringTokenizer(str, "\n");
		while (tok.hasMoreTokens()) {
			String orig = tok.nextToken();
			orig = orig.trim();

			// This is often the case with Mesh Terms,
			// where a term is accompanied by another specifying term
			// e.g. Monocytes/*immunology/microbiology
			// we ignore everything after the "/" symbol.
			if (orig.matches(".+?/.+?")) {
				String[] elements = orig.split("/");
				orig = elements[0];
			}

			orig = pseudoPhrase(orig);
			// System.err.println(orig);
			if (orig.length() > 0) {

				String id;
				if (m_vocabulary.equals("none")) {
					id = orig;
				} else {
					id = m_Vocabulary.getID(orig);
				}
				if (id != null) {
					// System.err.println("\t" + id);
					if (!hash.containsKey(id)) {
						hash.put(id, new Counter());
					} else {
						Counter c = (Counter) hash.get(id);
						c.increment();
						hash.put(id, c);
						if (forEval && m_Debug) {
							System.err
									.println("Skipping the phrase "
											+ orig
											+ ", which appears twice in the author-assigned keyphrase set.");
						}
					}
				}
			}
		}
		if (hash.size() == 0) {
			return null;
		} else {
			return hash;
		}
	}

	/**
	 * Get the value of KeyphraseAtt.
	 * 
	 * @return Value of KeyphraseAtt.
	 */
	public int getKeyphrasesAtt() {
		return m_KeyphrasesAtt;
	}

	/**
	 * Gets whether keyphrase frequency attribute is used.
	 */
	public boolean getKFused() {
		return m_KFused;
	}

	/**
	 * Get the value of MaxPhraseLength.
	 * 
	 * @return Value of MaxPhraseLength.
	 */
	public int getMaxPhraseLength() {
		return m_MaxPhraseLength;
	}

	/**
	 * Get the value of MinNumOccur.
	 * 
	 * @return Value of MinNumOccur.
	 */
	public int getMinNumOccur() {
		return m_MinNumOccur;
	}

	/**
	 * Get the value of MinPhraseLength.
	 * 
	 * @return Value of MinPhraseLength.
	 */
	public int getMinPhraseLength() {
		return m_MinPhraseLength;
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
	 * Gets the current settings of the filter.
	 * 
	 * @return an array of strings suitable for passing to setOptions
	 */
	public String[] getOptions() {

		String[] options = new String[13];
		int current = 0;

		if (getKFused()) {
			options[current++] = "-K";
		}
		if (getDebug()) {
			options[current++] = "-D";
		}
		options[current++] = "-I";
		options[current++] = "" + (getDocumentAtt() + 1);
		options[current++] = "-J";
		options[current++] = "" + (getKeyphrasesAtt() + 1);
		options[current++] = "-M";
		options[current++] = "" + (getMaxPhraseLength());
		options[current++] = "-L";
		options[current++] = "" + (getMinPhraseLength());
		options[current++] = "-O";
		options[current++] = "" + (getMinNumOccur());

		if (getDisallowInternalPeriods()) {
			options[current++] = "-P";
		}

		while (current < options.length) {
			options[current++] = "";
		}
		return options;
	}

	/**
	 * Expects an empty hashtable. Fills the hashtable with the stemmed n-grams
	 * occuring in the given string (as keys). Stores the position, the number
	 * of occurences, and the most commonly occurring orgininal version of each
	 * n-gram.
	 * 
	 * N-grams that occur less than m_MinNumOccur are not used.
	 * 
	 * Returns the total number of words (!) in the string.
	 */
	@SuppressWarnings("unchecked")
	private int getPhrases(HashMap hash, String str) {

		// FileOutputStream out = new FileOutputStream("candidates_kea41.txt");
		// PrintWriter printer = new PrintWriter(new OutputStreamWriter(out));

		// hash = table to store all the information about phrases extracted
		// from "str"
		// str = the content of the document, separated by newlines in sentences

		String[] buffer = new String[m_MaxPhraseLength];

		// Extracting strings of a predefined length from "str":

		StringTokenizer tok = new StringTokenizer(str, "\n");
		int pos = 1;

		while (tok.hasMoreTokens()) {
			String phrase = tok.nextToken();
			int numSeen = 0;
			StringTokenizer wordTok = new StringTokenizer(phrase, " ");
			while (wordTok.hasMoreTokens()) {
				String word = wordTok.nextToken();

				// Store word in buffer
				for (int i = 0; i < m_MaxPhraseLength - 1; i++) {
					buffer[i] = buffer[i + 1];
				}
				buffer[m_MaxPhraseLength - 1] = word;

				// How many are buffered?
				numSeen++;
				if (numSeen > m_MaxPhraseLength) {
					numSeen = m_MaxPhraseLength;
				}

				// Don't consider phrases that end with a stop word
				if (m_Stopwords.isStopword(buffer[m_MaxPhraseLength - 1])) {
					pos++;
					continue;
				}

				// Loop through buffer and add phrases to hashtable
				StringBuffer phraseBuffer = new StringBuffer();
				for (int i = 1; i <= numSeen; i++) {
					if (i > 1) {
						phraseBuffer.insert(0, ' ');
					}
					phraseBuffer.insert(0, buffer[m_MaxPhraseLength - i]);

					// Don't consider phrases that begin with a stop word
					if ((i > 1)
							&& (m_Stopwords.isStopword(buffer[m_MaxPhraseLength
									- i]))) {
						continue;
					}

					// Final restriction:
					// Only consider phrases with minimum length
					if (i >= m_MinPhraseLength) {

						// orig = each detected phase in its original spelling
						String orig = phraseBuffer.toString();

						// Create internal representation:
						// either a stemmed version or a pseudo phrase:

						String id;
						if (m_vocabulary.equals("none")) {
							String pseudo = pseudoPhrase(orig);
							id = pseudo;
						} else {
							// Match against the Vocabulary
							id = m_Vocabulary.getID(orig);
						}

						// System.out.println(orig + "\t" + pseudo + " \t " +
						// id);

						if (id != null) {

							// if Vocabulary is used, derive the correct
							// spelling
							// of the descriptor, else use one of the spellings
							// as in the document
							if (!m_vocabulary.equals("none")) {
								orig = m_Vocabulary.getOrig(id);
							}

							// Get the vector of the current phrase from the
							// hash table.
							// If it was already extracted from "str", the
							// values will be
							// updated in next steps, if not a new vector will
							// be created.

							FastVector vec = (FastVector) hash.get(id);

							if (vec == null) {

								// Specifying the size of the vector
								// According to additional selected features:

								if (m_STDEVfeature) {
									vec = new FastVector(3);
								} else {
									vec = new FastVector(2);
								}

								// Update hashtable with all the info
								vec.addElement(new Counter(pos + 1 - i)); // 0
								vec.addElement(new Counter()); // 1
								vec.addElement(orig); // 2

								if (m_STDEVfeature) {
									FastVector app = new FastVector();
									app.addElement(new Counter(pos + 1 - i));
									vec.addElement(app);
								}
								hash.put(id, vec);
							} else {

								// If the phrase already was identified,
								// update its values in the old vector

								// Update number of occurrences
								((Counter) (vec).elementAt(1))
										.increment();

								if (m_STDEVfeature) {

									FastVector app = (FastVector) vec
											.elementAt(3);
									app.addElement(new Counter(pos + 1 - i));
									vec.addElement(app);
								}

							}
						}
					}
				}
				pos++;
			}
		}

		// Replace secondary hashtables with most commonly occurring
		// version of each phrase (canonical) form. Delete all words
		// that are proper nouns.
		Iterator phrases = hash.keySet().iterator();

		while (phrases.hasNext()) {
			String phrase = (String) phrases.next();
			FastVector info = (FastVector) hash.get(phrase);

			// Occurring less than m_MinNumOccur? //m_MinNumOccur
			if (((Counter) (info).elementAt(1)).value() < m_MinNumOccur) {
				phrases.remove();
				continue;
			}
		}
		return pos;
	}

	/**
	 * Returns a hashtable. Fills the hashtable with the stemmed n-grams
	 * occuring in the given string (as keys) and the number of times it occurs.
	 */
	@SuppressWarnings("unchecked")
	public HashMap getPhrasesForDictionary(String str) {

		String[] buffer = new String[m_MaxPhraseLength];
		HashMap hash = new HashMap();

		StringTokenizer tok = new StringTokenizer(str, "\n");
		while (tok.hasMoreTokens()) {
			String phrase = tok.nextToken();
			// System.err.println("Sentence " + phrase);
			int numSeen = 0;
			StringTokenizer wordTok = new StringTokenizer(phrase, " ");
			while (wordTok.hasMoreTokens()) {
				String word = wordTok.nextToken();
				// System.err.println(word);
				// Store word in buffer
				for (int i = 0; i < m_MaxPhraseLength - 1; i++) {
					buffer[i] = buffer[i + 1];
				}
				buffer[m_MaxPhraseLength - 1] = word;

				// How many are buffered?
				numSeen++;
				if (numSeen > m_MaxPhraseLength) {
					numSeen = m_MaxPhraseLength;
				}

				// Don't consider phrases that end with a stop word
				if (m_Stopwords.isStopword(buffer[m_MaxPhraseLength - 1])) {
					continue;
				}

				// Loop through buffer and add phrases to hashtable
				StringBuffer phraseBuffer = new StringBuffer();
				for (int i = 1; i <= numSeen; i++) {
					if (i > 1) {
						phraseBuffer.insert(0, ' ');
					}
					phraseBuffer.insert(0, buffer[m_MaxPhraseLength - i]);

					// Don't consider phrases that begin with a stop word
					if ((i > 1)
							&& (m_Stopwords.isStopword(buffer[m_MaxPhraseLength
									- i]))) {
						continue;
					}

					// Only consider phrases with minimum length
					if (i >= m_MinPhraseLength) {

						// Match against the Vocabulary
						String orig = phraseBuffer.toString();

						// Create internal representation:
						// either a stemmed version or a pseudo phrase:
						String pseudo = pseudoPhrase(orig);
						// System.err.println("Checking " + orig + " -- " +
						// pseudo);

						String id;
						if (m_vocabulary.equals("none")) {
							// String pseudo = pseudoPhrase(orig);
							id = pseudo;
						} else {
							id = m_Vocabulary.getID(orig);
						}

						if (id != null) {
							Counter count = (Counter) hash.get(id);
							if (count == null) {
								hash.put(id, new Counter());
							} else {
								count.increment();
							}
							// System.err.println(orig + "\t" + id);
						}
					}
				}
			}
		}
		return hash;
	}

	/**
	 * Returns the index of the phrases' probabilities in the output ARFF file.
	 */
	public int getProbabilityIndex() {
		int index = m_DocumentAtt + 4;

		if (m_Debug) {
			if (m_KFused) {
				index++;
			}
		}
		if (m_STDEVfeature) {
			index++;
		}
		if (m_NODEfeature) {
			index++;
		}
		if (m_LENGTHfeature) {
			index++;
		}

		return index;
	}

	/**
	 * Returns the index of the phrases' ranks in the output ARFF file.
	 */
	public int getRankIndex() {
		return getProbabilityIndex() + 1;
	}

	/**
	 * Returns the index of the stemmed phrases in the output ARFF file.
	 */
	public int getStemmedPhraseIndex() {
		return m_DocumentAtt;
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
	 * Get the M_Stopwords value.
	 * 
	 * @return the M_Stopwords value.
	 */
	public Stopwords getStopwords() {
		return m_Stopwords;
	}

	/**
	 * Returns the index of the unstemmed phrases in the output ARFF file.
	 */
	public int getUnstemmedPhraseIndex() {
		return m_DocumentAtt + 1;
	}

	/**
	 * Get the M_Vocabulary value.
	 * 
	 * @return the M_Vocabulary value.
	 */
	public String getVocabulary() {
		return m_vocabulary;
	}

	/**
	 * Get the M_VocabularyFormat value.
	 * 
	 * @return the M_VocabularyFormat value.
	 */
	public String getVocabularyFormat() {
		return m_vocabularyFormat;
	}

	/**
	 * Returns a string describing this filter
	 * 
	 * @return a description of the filter suitable for displaying in the
	 *         explorer/experimenter gui
	 */
	public String globalInfo() {
		return "Converts incoming data into data appropriate for "
				+ "keyphrase classification.";
	}

	/**
	 * Input an instance for filtering. Ordinarily the instance is processed and
	 * made available for output immediately. Some filters require all instances
	 * be read before producing output.
	 * 
	 * @param instance
	 *            the input instance
	 * @return true if the filtered instance may now be collected with output().
	 * @exception Exception
	 *                if the input instance was not of the correct format or if
	 *                there was a problem with the filtering.
	 */
	@Override
    @SuppressWarnings("unchecked")
	public boolean input(Instance instance) throws Exception {
		if (getInputFormat() == null) {
			throw new Exception("No input instance format defined");
		}
		if (m_NewBatch) {
			resetQueue();
			m_NewBatch = false;
		}

		if (m_Debug) {
			System.err.println("-- Reading instance");
		}

		m_PunctFilter.input(instance);
		m_PunctFilter.batchFinished();
		instance = m_PunctFilter.output();

		if (m_vocabulary.equals("none")) {
			m_NumbersFilter.input(instance);
			m_NumbersFilter.batchFinished();
			instance = m_NumbersFilter.output();
		}

		if (m_Dictionary == null) {
			bufferInput(instance);
			return false;
		} else {
			FastVector vector = convertInstance(instance, false);
			Enumeration en = vector.elements();
			while (en.hasMoreElements()) {
				Instance inst = (Instance) en.nextElement();
				push(inst);
			}
			return true;
		}

	}

	/**
	 * Returns an enumeration describing the available options
	 * 
	 * @return an enumeration of all the available options
	 */
	@SuppressWarnings("unchecked")
	public Enumeration listOptions() {

		Vector newVector = new Vector(7);

		newVector.addElement(new Option(
				"\tSpecifies whether keyphrase frequency statistic is used.",
				"K", 0, "-K"));
		newVector.addElement(new Option(
				"\tSets the maximum phrase length (default: 3).", "M", 1,
				"-M <length>"));
		newVector.addElement(new Option(
				"\tSets the minimum phrase length (default: 1).", "L", 1,
				"-L <length>"));
		newVector.addElement(new Option("\tTurns debugging mode on.", "D", 0,
				"-D"));
		newVector.addElement(new Option(
				"\tSets the index of the document attribute (default: 0).",
				"I", 1, "-I"));
		newVector.addElement(new Option(
				"\tSets the index of the keyphrase attribute (default: 1).",
				"J", 1, "-J"));
		newVector.addElement(new Option("\tDisallow internal periods.", "P", 0,
				"-P"));
		newVector.addElement(new Option(
				"\tSet the minimum number of occurences (default: 2).", "O", 1,
				"-O"));

		return newVector.elements();
	}

	public void loadThesaurus(Stemmer st, Stopwords sw) {
		m_Vocabulary = new Vocabulary(m_vocabulary, m_vocabularyFormat,
				m_documentLanguage);

		m_Vocabulary.setStemmer(st);
		m_Vocabulary.setStopwords(sw);
		m_Vocabulary.initialize();
		try {

			if (m_DESCRreplace) {
				m_Vocabulary.buildUSE();
			}
			if (m_NODEfeature) {
				m_Vocabulary.buildREL();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates the preudo phrase from a string. A pseudo phrase is a version
	 * of a phrase that only contains non-stopwords, which are stemmed and
	 * sorted into alphabetical order.
	 */
	public String pseudoPhrase(String str) {
		// System.err.print(str + "\t");
		String[] pseudophrase;
		String[] words;
		String str_nostop;
		String stemmed;

		str = str.toLowerCase();

		// This is often the case with Mesh Terms,
		// where a term is accompanied by another specifying term
		// e.g. Monocytes/*immunology/microbiology
		// we ignore everything after the "/" symbol.
		if (str.matches(".+?/.+?")) {
			String[] elements = str.split("/");
			str = elements[0];
		}

		// removes scop notes in brackets
		// should be replaced with a cleaner solution
		if (str.matches(".+?\\(.+?")) {
			String[] elements = str.split("\\(");
			str = elements[0];
		}
		if (str.matches(".+?\\'.+?")) {
			String[] elements = str.split("\\'");
			str = elements[1];
		}

		// Remove some non-alphanumeric characters

		// str = str.replace('/', ' ');
		str = str.replace('-', ' ');
		str = str.replace('&', ' ');

		str = str.replaceAll("\\*", "");
		str = str.replaceAll("\\, ", " ");
		str = str.replaceAll("\\. ", " ");
		str = str.replaceAll("\\:", "");

		str = str.trim();

		// Stem string
		words = str.split(" ");
		str_nostop = "";
		for (int i = 0; i < words.length; i++) {
			if (!m_Stopwords.isStopword(words[i])) {
				if (str_nostop.equals("")) {
					str_nostop = words[i];
				} else {
					str_nostop = str_nostop + " " + words[i];
				}
			}
		}
		stemmed = m_Stemmer.stemString(str_nostop);

		// System.err.println(stemmed + "\t" + str_nostop + "\t"+ str);
		pseudophrase = sort(stemmed.split(" "));
		// System.err.println(join(pseudophrase));
		return join(pseudophrase);
	}

	/**
	 * Set the M_CheckProperNouns value.
	 * 
	 * @param newM_CheckProperNouns
	 *            The new M_CheckProperNouns value.
	 */
	public void setCheckForProperNouns(boolean newM_CheckProperNouns) {
		this.m_CheckForProperNouns = newM_CheckProperNouns;
	}

	/**
	 * Set the value of Debug.
	 * 
	 * @param newDebug
	 *            Value to assign to Debug.
	 */
	public void setDebug(boolean newDebug) {
		m_Debug = newDebug;
	}

	/**
	 * Set whether selected columns should be processed. If true the selected
	 * columns won't be processed.
	 * 
	 * @param disallow
	 *            the new invert setting
	 */
	public void setDisallowInternalPeriods(boolean disallow) {
		m_DisallowInternalPeriods = disallow;
	}

	/**
	 * Set the value of DocumentAtt.
	 * 
	 * @param newDocumentAtt
	 *            Value to assign to DocumentAtt.
	 */
	public void setDocumentAtt(int newDocumentAtt) {
		m_DocumentAtt = newDocumentAtt;
	}

	/**
	 * Set the M_documentLanguage value.
	 * 
	 * @param newM_documentLanguage
	 *            The new M_documentLanguage value.
	 */
	public void setDocumentLanguage(String newM_documentLanguage) {
		this.m_documentLanguage = newM_documentLanguage;
	}

	/**
	 * Sets the format of the input instances.
	 * 
	 * @param instanceInfo
	 *            an Instances object containing the input instance structure
	 *            (any instances contained in the object are ignored - only the
	 *            structure is required).
	 * @return true if the outputFormat may be collected immediately
	 */
	@Override
    public boolean setInputFormat(Instances instanceInfo) throws Exception {

		if (instanceInfo.classIndex() >= 0) {
			throw new Exception("Don't know what do to if class index set!");
		}
		if (!instanceInfo.attribute(m_KeyphrasesAtt).isString()
				|| !instanceInfo.attribute(m_DocumentAtt).isString()) {
			throw new Exception("Keyphrase attribute and document attribute "
					+ "need to be string attributes.");
		}
		m_PunctFilter = new KEAPhraseFilter();
		int[] arr = new int[1];
		arr[0] = m_DocumentAtt;
		m_PunctFilter.setAttributeIndicesArray(arr);
		m_PunctFilter.setInputFormat(instanceInfo);
		m_PunctFilter.setDisallowInternalPeriods(getDisallowInternalPeriods());

		if (m_vocabulary.equals("none")) {
			m_NumbersFilter = new NumbersFilter();
			m_NumbersFilter.setInputFormat(m_PunctFilter.getOutputFormat());
			super.setInputFormat(m_NumbersFilter.getOutputFormat());
		} else {
			super.setInputFormat(m_PunctFilter.getOutputFormat());
		}

		return false;

	}

	/**
	 * Set the value of KeyphrasesAtt.
	 * 
	 * @param newKeyphrasesAtt
	 *            Value to assign to KeyphrasesAtt.
	 */
	public void setKeyphrasesAtt(int newKeyphrasesAtt) {
		m_KeyphrasesAtt = newKeyphrasesAtt;
	}

	/**
	 * Sets whether keyphrase frequency attribute is used.
	 */
	public void setKFused(boolean flag) {
		m_KFused = flag;
		if (flag) {
			m_NumFeatures++;
		}
	}

	/**
	 * Set the value of MaxPhraseLength.
	 * 
	 * @param newMaxPhraseLength
	 *            Value to assign to MaxPhraseLength.
	 */
	public void setMaxPhraseLength(int newMaxPhraseLength) {
		m_MaxPhraseLength = newMaxPhraseLength;
	}

	/*
	 * private HashMap getComposits(HashMap dict) { HashMap dictClone =
	 * (HashMap)dict.clone(); Iterator it1 = dictClone.keySet().iterator();
	 * while (it1.hasNext()) { String id1 = (String)it1.next(); String term1 =
	 * m_Vocabulary.getOrig(id1); Iterator it2 = dictClone.keySet().iterator();
	 * while (it2.hasNext()) { String id2 = (String)it2.next();
	 * 
	 * String term2 = m_Vocabulary.getOrig(id2);
	 * 
	 * String composite = term1 + " " + term2; String idNew =
	 * m_Vocabulary.getID(composite);
	 * 
	 * if (term1 != term2 && idNew != null) {
	 * 
	 * FastVector vec = (FastVector)dict.get(idNew);
	 * 
	 * if (vec == null) { System.err.println("Found " +
	 * m_Vocabulary.getOrig(idNew) + " (" + term1 + ", " + term2 + ")"); //
	 * Specifying the size of the vector // According to additional selected
	 * features: vec = new FastVector(2); // Update hashtable with all the info
	 * vec.addElement(new Counter(0)); //0 vec.addElement(new Counter()); //1
	 * vec.addElement(m_Vocabulary.getOrig(idNew)); //2 dict.put(idNew, vec); }
	 * else { // Update number of occurrences
	 * ((Counter)((FastVector)vec).elementAt(1)).increment(); } } } } return
	 * dict; }
	 */

	/**
	 * Set the value of MinNumOccur.
	 * 
	 * @param newMinNumOccur
	 *            Value to assign to MinNumOccur.
	 */
	public void setMinNumOccur(int newMinNumOccur) {
		m_MinNumOccur = newMinNumOccur;
	}

	/**
	 * Set the value of MinPhraseLength.
	 * 
	 * @param newMinPhraseLength
	 *            Value to assign to MinPhraseLength.
	 */
	public void setMinPhraseLength(int newMinPhraseLength) {
		m_MinPhraseLength = newMinPhraseLength;
	}

	/**
	 * Sets whether Vocabulary relation attribute is used.
	 */
	public void setNumFeature() {
		if (m_STDEVfeature) {
			m_NumFeatures++;
		}
		if (m_NODEfeature) {
			m_NumFeatures++;
		}
		if (m_LENGTHfeature) {
			m_NumFeatures++;
		}
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
	 * -K<br>
	 * Specifies whether keyphrase frequency statistic is used.
	 * <p>
	 * 
	 * -R<br>
	 * Specifies whether Vocabulary relation statistic is used.
	 * <p>
	 * 
	 * -M length<br>
	 * Sets the maximum phrase length (default: 5).
	 * <p>
	 * 
	 * -L length<br>
	 * Sets the minimum phrase length (default: 1).
	 * <p>
	 * 
	 * -D<br>
	 * Turns debugging mode on.
	 * <p>
	 * 
	 * -I index<br>
	 * Sets the index of the attribute containing the documents (default: 0).
	 * <p>
	 * 
	 * -J index<br>
	 * Sets the index of the attribute containing the keyphrases (default: 1).
	 * <p>
	 * 
	 * -P<br>
	 * Disallow internal periods
	 * <p>
	 * 
	 * -O number<br>
	 * The minimum number of times a phrase needs to occur (default: 2).
	 * <p>
	 * 
	 * @param options
	 *            the list of options as an array of strings
	 * @exception Exception
	 *                if an option is not supported
	 */
	public void setOptions(String[] options) throws Exception {

		setKFused(Utils.getFlag('K', options));
		setDebug(Utils.getFlag('D', options));
		String docAttIndexString = Utils.getOption('I', options);
		if (docAttIndexString.length() > 0) {
			setDocumentAtt(Integer.parseInt(docAttIndexString) - 1);
		} else {
			setDocumentAtt(0);
		}
		String keyphraseAttIndexString = Utils.getOption('J', options);
		if (keyphraseAttIndexString.length() > 0) {
			setKeyphrasesAtt(Integer.parseInt(keyphraseAttIndexString) - 1);
		} else {
			setKeyphrasesAtt(1);
		}
		String maxPhraseLengthString = Utils.getOption('M', options);
		if (maxPhraseLengthString.length() > 0) {
			setMaxPhraseLength(Integer.parseInt(maxPhraseLengthString));
		} else {
			setMaxPhraseLength(3);
		}
		String minPhraseLengthString = Utils.getOption('M', options);
		if (minPhraseLengthString.length() > 0) {
			setMinPhraseLength(Integer.parseInt(minPhraseLengthString));
		} else {
			setMinPhraseLength(1);
		}
		String minNumOccurString = Utils.getOption('O', options);
		if (minNumOccurString.length() > 0) {
			setMinNumOccur(Integer.parseInt(minNumOccurString));
		} else {
			setMinNumOccur(2);
		}
		setDisallowInternalPeriods(Utils.getFlag('P', options));
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
	 * Set the M_Stopwords value.
	 * 
	 * @param newM_Stopwords
	 *            The new M_Stopwords value.
	 */
	public void setStopwords(Stopwords newM_Stopwords) {
		this.m_Stopwords = newM_Stopwords;
	}

	/**
	 * Set the M_Vocabulary value.
	 * 
	 * @param newM_Vocabulary
	 *            The new M_Vocabulary value.
	 */
	public void setVocabulary(String newM_Vocabulary) {
		this.m_vocabulary = newM_Vocabulary;
	}

	/**
	 * Set the M_VocabularyFormat value.
	 * 
	 * @param newM_VocabularyFormat
	 *            The new M_VocabularyFormat value.
	 */
	public void setVocabularyFormat(String newM_VocabularyFormat) {
		this.m_vocabularyFormat = newM_VocabularyFormat;
	}
}

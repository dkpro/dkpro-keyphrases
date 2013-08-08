package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import kea.stopwords.Stopwords;

/**
 * This class supports generic generation of Stopwords objects necessary for KEA
 * key-phrase extraction.
 * 
 * @author Florian Schwager
 * 
 */
public class FileStopwords extends Stopwords {

	private static final long serialVersionUID = -8324541655674111897L;
	private Hashtable<String, Double> m_Stopwords = null;

	/**
	 * Constructor using an input file containing the stopwords.
	 * 
	 * @param file
	 *            String
	 */
	public FileStopwords(String file) {
		if (m_Stopwords == null) {
			m_Stopwords = new Hashtable<String, Double>();
			Double dummy = new Double(0);
			File txt = new File(file);
			InputStreamReader is;
			String sw = null;
			try {
				is = new InputStreamReader(new FileInputStream(txt), "UTF-8");
				BufferedReader br = new BufferedReader(is);
				while ((sw = br.readLine()) != null) {
					m_Stopwords.put(sw, dummy);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean isStopword(String str) {
		return m_Stopwords.containsKey(str.toLowerCase());
	}
}

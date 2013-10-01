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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Static utility-methods for {@link Keyphrase}-annotations.
 * @author Mateusz Parzonka
 *
 */
public class KeyphraseUtil
{

	/**
	 * @param jcas a JCas containing {@link Keyphrase}-annotations.
	 * @param comparator Keyphrases are sorted by the given comparator
	 * @return a sorted list of keyphrases as strings without filtering for duplicates
	 */
	public static List<String> getOrderedPhrasesWithDuplicates(JCas jcas, Comparator<Keyphrase> comparator, boolean convertToLowercase) {

		List<Keyphrase> keyphrases = new ArrayList<Keyphrase>();
		keyphrases.addAll(JCasUtil.select(jcas, Keyphrase.class));
		Collections.sort(keyphrases, comparator);

		List<String> phrases = new ArrayList<String>();
		for (Keyphrase keyphrase : keyphrases) {
			String phrase = keyphrase.getKeyphrase().toLowerCase();
			phrase = convertToLowercase ? phrase.toLowerCase() : phrase;
				phrases.add(phrase);
			}
		return phrases;
	}

	/**
	 * @param jcas a JCas containing {@link Keyphrase}-annotations.
	 * @param comparator Keyphrases are sorted by the given comparator
	 * @return a sorted list of keyphrases as strings without duplicates.
	 */
	public static List<String> getOrderedPhrasesNoDuplicates(JCas jcas, Comparator<Keyphrase> comparator, boolean convertToLowercase) {

		List<Keyphrase> keyphrases = new ArrayList<Keyphrase>();
		keyphrases.addAll(JCasUtil.select(jcas, Keyphrase.class));
		Collections.sort(keyphrases, comparator);
		Set<String> uniquePhrases = new HashSet<String>();

		List<String> phrases = new ArrayList<String>();
		for (Keyphrase keyphrase : keyphrases) {
			String phrase = keyphrase.getKeyphrase();
			phrase = convertToLowercase ? phrase.toLowerCase() : phrase;
			if (!uniquePhrases.contains(phrase)) {
				phrases.add(phrase);
				uniquePhrases.add(phrase);
			}
		}
		return phrases;
	}

}

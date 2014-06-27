/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
	 * @param convertToLowercase If it should be converted to lower case
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
	 * @param convertToLowercase If parameter should be converted to lower case
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

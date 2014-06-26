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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import static org.apache.commons.io.FilenameUtils.indexOfExtension;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator.EvaluatorType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.util.KeyphraseScoreComparator;

public class EvaluatorUtils {

	/**
	 * Get the gold keyphrases from the file system.
	 * By convention gold keyphrase files should have the same name as the document file.
	 * Standard extension is .key, but something else can be specified in the corresponding parameter.
	 *
	 * @param metaData The DocumentMetaData annotation of this document.
	 * @param goldSuffix The suffix of the gold standard file.
	 * @param toLowercase If gold keys should be lowercased or not.
	 *
	 * @return The set of gold keyphrases for this document.
	 * @throws AnalysisEngineProcessException an analysis engine process exception
	 */
	public static Set<String> getGoldKeyphrases(DocumentMetaData metaData, String goldSuffix, boolean toLowerCase) throws AnalysisEngineProcessException {

		Set<String> goldKeyphrases = new TreeSet<String>();

		String uri = metaData.getDocumentUri();
		URL keyUrl;
		try {
			keyUrl = URI.create(uri.substring(0, indexOfExtension(uri)) + goldSuffix).toURL();

			List<String> lines = IOUtils.readLines(keyUrl.openStream(), "UTF-8");

			for (String line : lines) {
				if (toLowerCase) {
					line = line.toLowerCase().trim();
				}
				else {
					line = line.trim();
				}
				if (line.length() > 0) {
					if(line.contains(";")){
						for(String part: line.split(";")){
							goldKeyphrases.add(part.trim());
						}

					}
					else{
						goldKeyphrases.add(line);
					}
				}
			}

			return goldKeyphrases;

		} catch (IOException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	/**
	 * @param jcas the jcas being evaluated
	 * @param type Whether the text should be built using tokens, stems, or lemmas.
	 * @return A concatenated string representing the document text.<br>
	 *         Depending on the type parameter, the text contains tokens, stems, or lemmas.
	 */
	public static String getDocumentText(JCas jcas, EvaluatorType type) {
		StringBuilder sb = new StringBuilder();
		if (type.equals(EvaluatorType.Token)) {
			sb.append(jcas.getDocumentText());
		}
		else if (type.equals(EvaluatorType.Stem)) {
			int lastBegin = 0;
			for (Stem s : JCasUtil.select(jcas, Stem.class)) {
				if (s.getBegin() >= lastBegin) {
					lastBegin = s.getBegin();
					sb.append(s.getValue());
					sb.append(" ");
				}
			}
		}
		else if (type.equals(EvaluatorType.Lemma)) {
			int lastBegin = 0;
			for (Lemma l : JCasUtil.select(jcas, Lemma.class)) {
				if (l.getBegin() >= lastBegin) {
					lastBegin = l.getBegin();
					sb.append(l.getValue());
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Sorts the keyphrases by score.
	 * Filters duplicate keyphrases.
	 * The keyphrase with the higher score is kept.
	 *
	 * @param keyphraseIterable An iterable over keyphrases.
	 * @param toLowercase If gold keys should be lowercased or not.
	 *
	 * @return A list of unique keyphrases in order of descending scores.
	 */
	public static List<Keyphrase> filterAndSortKeyphrases(Iterable<Keyphrase> keyphraseIterable, boolean toLowercase) {

		List<Keyphrase> keyphrases = new ArrayList<Keyphrase>();
		for (Keyphrase keyphrase : keyphraseIterable) {
			keyphrases.add(keyphrase);
		}
		return filterAndSortKeyphrases(keyphrases, toLowercase);

	}

	/**
	 * Sorts the keyphrases by score.
	 * Filters duplicate keyphrases.
	 * The keyphrase with the higher score is kept.
	 *
	 * @param keyphrasesCollection A list over keyphrases.
	 * @param toLowercase If gold keys should be lowercased or not.
	 *
	 * @return A list of unique keyphrases in order of descending scores.
	 */
	public static List<Keyphrase> filterAndSortKeyphrases(Collection<Keyphrase> keyphrasesCollection, boolean toLowercase) {

		List<Keyphrase> keyphrases = new ArrayList<Keyphrase>(keyphrasesCollection);


		// sort the keyphrases
		Collections.sort(keyphrases, new KeyphraseScoreComparator());

		// filter the keyphrases
		List<Keyphrase> filteredKeyphrases = new ArrayList<Keyphrase>();
		Set<String> uniqueKeyphrases = new HashSet<String>();
		for (Keyphrase keyphrase : keyphrases) {
			String keyphraseString = toLowercase ? keyphrase.getKeyphrase().toLowerCase() : keyphrase.getKeyphrase();

			if (keyphraseString.length() == 0) {
				continue;
			}

			if (!uniqueKeyphrases.contains(keyphraseString)) {
				uniqueKeyphrases.add(keyphraseString);
				filteredKeyphrases.add(keyphrase);
			}
		}

		return filteredKeyphrases;
	}
}
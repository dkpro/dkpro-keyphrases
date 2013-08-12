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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;

/**
 * This component adds {@link NamedEntity} annotations to every list of
 * consecutive terms (= phrase) that satisfy a simple heuristic.
 * <p>
 * - A term has to be alphanumeric.<br>
 * - The phrase has to start with an upper case letter. <br>
 * - When the phrase consists of a single term, it is not allowed to be at the
 * beginning of a sentence.<br>
 * - Terms that are written in capital letters are not added to a phrase.
 * <p>
 * Obviously, not a valid approach for German :)
 *
 * @author zesch, parzonka
 *
 */
public class HeuristicNamedEntityAnnotator
	extends JCasAnnotator_ImplBase
{
	private static Pattern p = Pattern.compile("[^A-Za-z0-9]");

	@Override
	public void initialize(UimaContext aContext)
		throws ResourceInitializationException
	{

		super.initialize(aContext);
	}

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		String document = jcas.getDocumentText();
		if (document.length() > 0) {
			processDocument(jcas);
		}
		else {
			getContext().getLogger().log(Level.WARNING, "Document is empty");
		}
	}

	private void processDocument(JCas aJCas)
		throws AnalysisEngineProcessException
	{

		for (Sentence sentence : JCasUtil.select(aJCas, Sentence.class)) {

			int sentenceOffset = 0;
			List<Token> namedEntityList = new ArrayList<Token>();

			for (Token token : JCasUtil.selectCovered(Token.class, sentence)) {
				String tokenString = token.getCoveredText();

				if (startsWithUpperCase(tokenString) && isAlphaNumeric(tokenString)
						&& !isAllCaps(tokenString)) {
					namedEntityList.add(token);

				}
				else {
					if (namedEntityList.size() > 0) {
						// do only add, if not a single sentence beginning token
						if (namedEntityList.size() != 1 || sentenceOffset != 1) {
							NamedEntity ne = new NamedEntity(aJCas);
							ne.setBegin(namedEntityList.get(0).getBegin());
							ne.setEnd(namedEntityList.get(namedEntityList.size() - 1)
									.getEnd());

							List<String> namedEntityStrings = new ArrayList<String>();
							for (Token t : namedEntityList) {
								namedEntityStrings.add(t.getCoveredText());
							}
							ne.setValue(StringUtils.join(namedEntityStrings, " "));
							ne.addToIndexes();
						}
						namedEntityList.clear();
					}
				}
				sentenceOffset++;
			}
		}
	}

	private boolean startsWithUpperCase(String token)
	{
		if (!token.isEmpty()) {
            return Character.isUpperCase(token.charAt(0));
        }
        else {
            return false;
        }
	}

	/**
	 * Checks if the token is written in all capitals.
	 *
	 * @param token
	 * @return true when token in all-caps, else false
	 */
	private boolean isAllCaps(String token)
	{
		if (!token.isEmpty()) {
			for (int i = 0; i < token.length(); i++) {
				if (!Character.isUpperCase(token.charAt(i))) {
                    return false;
                }
			}
			return true;
		}
        else {
            return false;
        }
	}

	private boolean isAlphaNumeric(String token)
	{
		return !p.matcher(token).find();
	}

}
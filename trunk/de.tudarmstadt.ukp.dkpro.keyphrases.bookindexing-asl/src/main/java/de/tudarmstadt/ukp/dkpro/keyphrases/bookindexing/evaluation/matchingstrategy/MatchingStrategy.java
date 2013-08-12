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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.matchingstrategy;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.fit.component.Resource_ImplBase;
import org.apache.uima.util.Level;

/**
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class MatchingStrategy
	extends Resource_ImplBase
{

	/**
	 * Returns a set containing the gold phrases that are covered by the given
	 * phrase.
	 *
	 * @param phrase
	 * @return a set of matches. When no matches had been found, then the set is
	 *         empty.
	 */
	public Set<String> getCovered(String phrase, Set<String> goldSet)
	{
		Set<String> matches = new HashSet<String>();
		for (String goldPhrase : goldSet) {
			if (isMatch(phrase, goldPhrase)) {
				if (getUimaContext().getLogger().isLoggable(Level.FINEST)) {
                    getUimaContext().getLogger().log(Level.FINEST, String.format("[%s] and [%s] match.", phrase, goldPhrase));
                }
				matches.add(goldPhrase);
			}
			else {
				if (getUimaContext().getLogger().isLoggable(Level.FINEST)) {
                    getUimaContext().getLogger().log(Level.FINEST, String.format("[%s] and [%s] match NOT.", phrase, goldPhrase));
                }
			}
		}
		return matches;
	}

	public abstract boolean isMatch(String phrase, String goldPhrase);

	@Override
	public String toString()
	{
		return getClass().getSimpleName();
	}

}

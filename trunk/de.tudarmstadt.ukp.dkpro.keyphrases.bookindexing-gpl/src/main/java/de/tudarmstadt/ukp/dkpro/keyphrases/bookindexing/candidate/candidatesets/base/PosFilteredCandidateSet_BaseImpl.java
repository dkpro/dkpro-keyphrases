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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.posfilter.PosFilter;

/**
 * Provides base implementation for {@link CandidateSet}s that offer the
 * possibility to filter Part-of-Speech, namely Tokens, Lemmas and Stems.
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class PosFilteredCandidateSet_BaseImpl
	extends CandidateSet_BaseImpl

{
	private Set<PosType> posToKeep;

	public PosFilteredCandidateSet_BaseImpl()
	{
		this.posToKeep = new HashSet<PosType>();
	}

	/**
	 * The POS-filter lets candidates pass, which have one of the given POS-tags.
	 *
	 * @param posToKeep a list of tags which will pass the filter
	 *
	 * @return the candidate set with activated POS-filter
	 */
	public CandidateSet setPosToKeep(PosType... posToKeep)
	{
		setPosToKeep(new HashSet<PosType>());
		for (PosType tag : posToKeep) {
			getPosToKeep().add(tag);
		}
		return this;
	}

	/**
	 * The POS-filter lets candidates pass, which have one of the given POS-tags.
	 *
	 * @param posToKeep a set containing tags which will pass the filter
	 *
	 * @return the candidate set with activated POS-filter
	 */
	public CandidateSet setPosToKeep(Set<PosType> posToKeep)
	{
		this.posToKeep = posToKeep;
		return this;
	}

	protected AnalysisEngineDescription getPosFilter()
		throws ResourceInitializationException
	{
		 return createPrimitiveDescription(
         PosFilter.class,
         PosFilter.PARAM_TYPE_TO_REMOVE, Token.class.getName(),
         PosFilter.PARAM_ADJ,  posToKeep.contains(PosType.ADJ),
         PosFilter.PARAM_ADV,  posToKeep.contains(PosType.ADV),
         PosFilter.PARAM_ART,  posToKeep.contains(PosType.ART),
         PosFilter.PARAM_CARD, posToKeep.contains(PosType.CARD),
         PosFilter.PARAM_CONJ, posToKeep.contains(PosType.CONJ),
         PosFilter.PARAM_N,    posToKeep.contains(PosType.N),
         PosFilter.PARAM_O,    posToKeep.contains(PosType.O),
         PosFilter.PARAM_PP,   posToKeep.contains(PosType.PP),
         PosFilter.PARAM_PR,   posToKeep.contains(PosType.PR),
         PosFilter.PARAM_PUNC, posToKeep.contains(PosType.PUNC),
         PosFilter.PARAM_V,    posToKeep.contains(PosType.V)
         );
	}

	public Set<PosType> getPosToKeep()
	{
		return posToKeep;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("PosFilter: ");

		if (getPosToKeep().size() > 0) {
			sb.append("Keeping { ");

			Iterator<PosType> iter = posToKeep.iterator();
			while (iter.hasNext()) {
				sb.append(iter.next());
				if (iter.hasNext()) {
                    sb.append(", ");
                }
			}
			sb.append(" }");

		}
		else {
			sb.append("OFF");
		}
		return sb.append(LF).toString();
	}

}

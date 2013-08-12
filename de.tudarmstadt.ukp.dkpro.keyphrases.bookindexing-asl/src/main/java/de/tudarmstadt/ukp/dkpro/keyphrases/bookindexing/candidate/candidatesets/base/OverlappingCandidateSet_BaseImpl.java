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

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;

/**
 * Provides base implementation for {@link CandidateSet}s that are comprised of
 * multiple tokens and can therefor overlap, like NChunks and Named Entities.
 *
 * @author Mateusz Parzonka
 *
 */
public abstract class OverlappingCandidateSet_BaseImpl
	extends CandidateSet_BaseImpl
{

	private boolean resolveOverlaps;

	/**
	 * In case of overlapping phrases and a resolveOverlaps parameter set to true:
	 * <p>
	 * a)<br>
	 * <code>xxxx yyyy zzzz</code><br>
	 * <code>&nbsp;&nbsp;&nbsp;&nbsp; yyyy &nbsp;&nbsp;&nbsp;&nbsp;</code><br>
	 * We only add the larger one (<code>xxxx yyyy zzzz</code>) as a candidate .
	 * <p>
	 *
	 * b)<br>
	 * <code>xxxx yyyy &#160;&#160;&#160;&#160;</code><br>
	 * <code>&nbsp;&nbsp;&nbsp;&nbsp; yyyy zzzz</code><br>
	 * We add a merged candiate (<code>xxxx yyyy zzzz</code>).
	 *
	 *
	 * @param resolveOverlaps
	 */
	public void setResolveOverlaps(boolean resolveOverlaps)
	{
		this.resolveOverlaps = resolveOverlaps;
	}

	public boolean isResolveOverlaps()
	{
		return resolveOverlaps;
	}

	@Override
	protected AnalysisEngineDescription getCandidateAnnotator()
		throws ResourceInitializationException
	{
		return createPrimitiveDescription(CandidateAnnotator.class,
				CandidateAnnotator.PARAM_FEATURE_PATH, getFeaturePath(),
				CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolveOverlaps);
	}

	@Override
	public String toString()
	{
		return "ResolveOverlaps: " + resolveOverlaps + LF;
	}
}

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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.pipeline;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * All {@link Keyphrase}s associated to the covering {@link Segment} are printed
 * to the logger.
 *
 * @author Mateusz Parzonka
 *
 */
public class AggregatorMock
	extends JCasAnnotator_ImplBase
{

	private int jcasNr = 0;

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{

		getContext().getLogger().log(Level.INFO,
				"JCas " + jcasNr++ + " after segment processing: " + jcas);

		assert jcas.getAnnotationIndex(Segment.type).size() > 0 : "JCas must contain at least one Segment annotation.";

		int segNr = 0;
		for (Segment segment : JCasUtil.select(jcas, Segment.class)) {
			for (Keyphrase k : JCasUtil.selectCovered(Keyphrase.class, segment)) {
				getContext().getLogger().log(Level.INFO,
						"SegNr.:" + segNr + " " + k.toString());
			}
			segNr++;
		}

	}

}

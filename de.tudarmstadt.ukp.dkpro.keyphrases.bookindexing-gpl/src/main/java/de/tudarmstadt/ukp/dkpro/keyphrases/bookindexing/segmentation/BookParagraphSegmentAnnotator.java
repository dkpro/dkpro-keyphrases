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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;

/**
 * Draft of a annotator that add {@link Segment}-annotations representing the
 * structure of a book. At the moment, we split the book into pseudo-paragraphs
 * separated by one or more line breaks. "pseudo", because headlines and picture
 * descriptions at similar are handled as paragraphs too.
 *
 * @author Mateusz Parzonka
 */

public class BookParagraphSegmentAnnotator extends JCasAnnotator_ImplBase {

    @Override
    public void initialize(UimaContext context)
	    throws ResourceInitializationException {
	super.initialize(context);

    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {

	String text = jCas.getDocumentText();
	String[] paragraphs = text.split("[\n]+");
	String[] linebreaks = text.split("[^\n]+");
	int segmentNr = 0;
	int begin = linebreaks[segmentNr++].length();
	int end = text.length();

	for (String paragraph : paragraphs) {
	    end = begin + paragraph.length();
	    Segment segment = new Segment(jCas);
	    segment.setBegin(begin);
	    segment.setEnd(end);
	    segment.addToIndexes();
	    if (segmentNr < linebreaks.length) {
            begin = end + linebreaks[segmentNr++].length();
        }
	}

    }
}

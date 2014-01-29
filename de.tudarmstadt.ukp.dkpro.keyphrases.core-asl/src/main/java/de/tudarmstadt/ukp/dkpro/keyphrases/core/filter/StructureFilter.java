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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters;
import de.tudarmstadt.ukp.dkpro.core.api.resources.MappingProvider;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.filter.util.PosPatternFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * Removes all annotations that:
 * 1) contain less than MinTokens or more than MaxTokens
 * 2) do not conform to certain POS-combinations
 *
 * If there is a keyphrase index, it works on keyphrases. Otherwise, it works on candidates.
 *
 * @author zesch
 *
 */
public class StructureFilter extends JCasAnnotator_ImplBase {
	public static final String PARAM_LANGUAGE = ComponentParameters.PARAM_LANGUAGE;
	@ConfigurationParameter(name = PARAM_LANGUAGE, mandatory = false)
	protected String language;

	public static final String PARAM_TAGGER_MAPPING_LOCATION = "taggerMappingLocation";
	@ConfigurationParameter(name = PARAM_TAGGER_MAPPING_LOCATION, mandatory = false)
	protected String taggerMappingLocation;

	public static final String PARAM_TAGGER_TAGSET = "taggerTagset";
	@ConfigurationParameter(name = PARAM_TAGGER_TAGSET, mandatory = false)
	protected String taggerTagset;

    public static final String PARAM_MIN_TOKENS   = "MinTokens";
    @ConfigurationParameter(name=PARAM_MIN_TOKENS, mandatory=true, defaultValue="1")
    private int minTokens;

    public static final String PARAM_MAX_TOKENS   = "MaxTokens";
    @ConfigurationParameter(name=PARAM_MAX_TOKENS, mandatory=true, defaultValue="4")
    private int maxTokens;

    public static final String PARAM_POS_PATTERNS = "UsePosPatterns";
    @ConfigurationParameter(name=PARAM_POS_PATTERNS, mandatory=true, defaultValue="false")
    private boolean usePosPatterns;

    private PosPatternFilter posPatternFilter;

	private MappingProvider taggerMappingProvider;

    @Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);

        if (usePosPatterns) {
            posPatternFilter = new PosPatternFilter(minTokens, maxTokens);
        }

		taggerMappingProvider = new MappingProvider();
		taggerMappingProvider.setDefault(MappingProvider.LOCATION, "classpath:/de/tudarmstadt/ukp/dkpro/" +
				"core/api/lexmorph/tagset/${language}-${tagger.tagset}-pos.map");
		taggerMappingProvider.setDefault(MappingProvider.BASE_TYPE, POS.class.getName());
		taggerMappingProvider.setDefault("tagger.tagset", "default");
		taggerMappingProvider.setOverride(MappingProvider.LOCATION, taggerMappingLocation);
		taggerMappingProvider.setOverride(MappingProvider.LANGUAGE, language);
		taggerMappingProvider.setOverride("tagger.tagset", taggerTagset);

    }

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
        getContext().getLogger().log(Level.CONFIG, "Entering " + this.getClass().getSimpleName());

        taggerMappingProvider.configure(jcas.getCas());

        FSIterator<Annotation> annotationIter = jcas.getAnnotationIndex(KeyphraseCandidate.type).iterator();

        List<Annotation> toRemove = new ArrayList<Annotation>();
        while (annotationIter.hasNext()) {
            Annotation a = annotationIter.next();

            if (usePosPatterns) {
                
                List<POS> annotations = selectCovered(jcas, POS.class, a);

                if (annotations.size() == 0) {
                    throw new AnalysisEngineProcessException(new Throwable("Could not get annotations: " + a + 
                            ". A POS Tagger should be run in the preprocessing phase."));
                }
                List<String> posList = new ArrayList<String>();
                for (POS pos : annotations) {
                	Type posType = taggerMappingProvider.getTagType(pos.getPosValue());
                	if (posType != null) {
                		posList.add(posType.getShortName().substring(0, 1));
                	}
                    else {
                        posList.add( "O" );
                    }
                }

                if (!posPatternFilter.isValidPosPattern(posList)) {
                    toRemove.add(a);
                }
            }
            else {
                // TODO in the final results, there might be keyphrases of other size, as this only checks the original candidate terms *with* stopwords
//                if (annotations.size() < minTokens || annotations.size() > maxTokens) {
//                    toRemove.add(a);
//                }

                KeyphraseCandidate c = (KeyphraseCandidate) a;
                String termString = c.getKeyphrase();

                if (termString == null) {
                    throw new AnalysisEngineProcessException(new Throwable("Unexpected null value."));
                }

                String[] parts = termString.trim().split("\\s+");
                if (parts.length < minTokens || parts.length > maxTokens) {
                    toRemove.add(a);
                }
            }
        }

        // remove candidates
        for (Annotation a : toRemove) {
            a.removeFromIndexes();
        }
	}
}
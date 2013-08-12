package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.ArrayList;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationIndex;
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
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
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

    // the mode is automatically determined from the indexes present in the CAS
    private enum Mode {
        Candidates,
        Keyphrases
    }
    private Mode mode;

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
				"core/api/lexmorph/tagset/${language}-${tagger.tagset}-tagger.map");
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

        AnnotationIndex<Annotation> keyphraseIndex = jcas.getAnnotationIndex(Keyphrase.type);
        if (keyphraseIndex.size() > 0) {
            mode = Mode.Keyphrases;
        }
        else {
            mode = Mode.Candidates;
        }

        FSIterator<Annotation> annotationIter = null;
        if (mode.equals(Mode.Candidates)) {
            annotationIter = jcas.getAnnotationIndex(KeyphraseCandidate.type).iterator();
        }
        else if (mode.equals(Mode.Keyphrases)) {
            annotationIter = jcas.getAnnotationIndex(Keyphrase.type).iterator();
        }

        List<Annotation> toRemove = new ArrayList<Annotation>();
        while (annotationIter.hasNext()) {
            Annotation a = annotationIter.next();

            List<POS> annotations = selectCovered(jcas, POS.class, a);

            if (annotations.size() == 0) {
                getContext().getLogger().log(Level.FINE, "Could not get annotations: " + a + ". Skipping!");
                continue;
            }

            if (usePosPatterns) {
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
                String termString = "";
                if (mode.equals(Mode.Candidates)) {
                    KeyphraseCandidate c = (KeyphraseCandidate) a;
                    termString = c.getKeyphrase();
                }
                else if (mode.equals(Mode.Keyphrases)) {
                    Keyphrase k = (Keyphrase) a;
                    termString = k.getKeyphrase();
                }

                if (termString == null) {
                    throw new AnalysisEngineProcessException(new Throwable("Unexpected null value."));
                }

                termString = termString.trim();
                termString = termString.replaceAll("\\s{2,}"," ");
                String[] parts = termString.split(" ");
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
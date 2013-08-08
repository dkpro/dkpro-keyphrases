package de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

/**
 * This annotator takes a fully qualified type name and a method name that returns a string representation of the type as parameters.
 *
 * In case of overlapping annotations and a resolveOverlaps parameter set to true:
 * a)
 * xxxx yyyy zzzz
 *      yyyy
 * We only add the larger one (xxxx yyyy zzzz) as a candidate .
 *
 * b)
 * xxxx yyyy
 *      yyyy zzzz
 * We add a merged candiate (xxxx yyyy zzzz).
 *
 * @author zesch
 */
public class CandidateAnnotator extends JCasAnnotator_ImplBase {
    public static final String PARAM_FEATURE_PATH = "FeaturePath";
    @ConfigurationParameter(name=PARAM_FEATURE_PATH, mandatory=true)
    private String featurePath;

    public static final String PARAM_RESOLVE_OVERLAPS = "resolveOverlaps";
    @ConfigurationParameter(name=PARAM_RESOLVE_OVERLAPS, mandatory=true, defaultValue="false")
    private boolean resolveOverlaps; // setting this true might not make too much sense with NGrams :)

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        getContext().getLogger().log(Level.FINE, "Entering " + this.getClass().getSimpleName());

        // get the candidates according to the parameters
        List<Candidate> candidates = new ArrayList<Candidate>();

        try {
            for (Entry<AnnotationFS, String> entry: FeaturePathFactory.select(jcas.getCas(), featurePath)) {
                candidates.add(new Candidate(entry.getValue(), entry.getKey().getBegin(), entry.getKey().getEnd()));
            }
        } catch (FeaturePathException e) {
            throw new AnalysisEngineProcessException(e);
        }

        List<Candidate> resolvedCandidates;
        if (resolveOverlaps) {
            resolvedCandidates = resolveOverlappingCandidates(candidates);
        }
        else {
            resolvedCandidates = candidates;
        }

        AnnotationIndex<Annotation> candidateIndex = jcas.getAnnotationIndex(KeyphraseCandidate.type);
        for (Candidate candidate : resolvedCandidates) {
            KeyphraseCandidate kc = new KeyphraseCandidate(jcas);
            kc.setKeyphrase(candidate.term);
            kc.setBegin(candidate.begin);
            kc.setEnd(candidate.end);

            // do not allow duplicates
            if (!candidateIndex.contains(kc)) {
                kc.addToIndexes(jcas);
            }
        }
    }


    private List<Candidate> resolveOverlappingCandidates(List<Candidate> candidates) {
        List<Candidate> resolvedCandidates = new ArrayList<Candidate>();

        boolean finished = false;
        while (!finished && candidates.size() > 0) {
            if (candidates.size() == 1) {
                resolvedCandidates.add(candidates.get(0));
                finished = true;
                continue;
            }

            Candidate firstCandidate = candidates.get(0);

            Overlap overlap = getOverlappingCandidate(firstCandidate, candidates.subList(1, candidates.size()));

            if (overlap == null) {
                // if there is no overlap, add the firstCandidate to the list of resolved cases
                resolvedCandidates.add(firstCandidate);
                // shorten candidate list
                candidates = candidates.subList(1, candidates.size());
            }
            else {
                // resolve
                Candidate resolvedCandidate = resolve(firstCandidate, overlap.candidate);

                if (resolvedCandidate != null) {
                    // Add the resolved candidate at the position of the overlap and shorten list.
                    candidates.set(overlap.offset, resolvedCandidate);
                    candidates = candidates.subList(1, candidates.size());
                }
                else {
                    // just shorten the list => remove the candidate that caused the error
                    candidates = candidates.subList(1, candidates.size());
                }
            }
        }

        return resolvedCandidates;
    }

    /**
     * @param candidate
     * @param candidateList
     * @return Returns an Overlap object containing the first candidate from candidateList overlapping with candidate. Or null, if no overlap was found.
     */
    private Overlap getOverlappingCandidate(Candidate candidate, List<Candidate> candidateList) {
        int i = 1;
        for (Candidate c : candidateList) {
            if (overlaps(c, candidate)) {
                return new Overlap(c,i);
            }
            i++;
        }
        return null;
    }

    /**
     * @param c1
     * @param c2
     * @return True, if c1 and c2 overlap.
     */
    private boolean overlaps(Candidate c1, Candidate c2) {
        if ((c1.begin == c2.begin || c1.end == c2.end) ||
            (c1.begin < c2.begin && c1.end > c2.end) ||
            (c1.begin > c2.begin && c1.end < c2.end) ||
            (c1.begin < c2.begin && c1.end > c2.begin) ||
            (c2.begin < c1.begin && c2.end > c1.begin)) {

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Resolve cases:
     * a)
     * Equal candidates. Return one.
     *
     * b)
     * xxxx yyyy zzzz
     *      yyyy
     * We only add the larger one (xxxx yyyy zzzz) as a candidate.
     *
     * c)
     * xxxx yyyy
     *      yyyy zzzz
     * We add a merged candiate (xxxx yyyy zzzz).
     *
     *
     * @param firstCandidate
     * @param secondCandidate
     * @return The resolved candidate or null.
     */
    private Candidate resolve(Candidate c1, Candidate c2) {
        // case a
        if (c1.begin == c2.begin && c1.end == c2.end) {
            getContext().getLogger().log(Level.FINEST, "Resolve case a");
            return c1;
        }
        // case b(1)
        else if (c1.begin <= c2.begin && c1.end >= c2.end) {
            getContext().getLogger().log(Level.FINEST, "Resolve case b1");
            return c1;
        }
        // case b(2)
        else if (c1.begin >= c2.begin && c1.end <= c2.end) {
            getContext().getLogger().log(Level.FINEST, "Resolve case b2");
            return c2;
        }
        // case c(1)
        else if (c1.begin < c2.begin && c1.end > c2.begin) {
            getContext().getLogger().log(Level.FINEST, "Resolve case c1");
            int mismatch = c2.begin - c1.begin;
            if (mismatch < c2.term.length()) {
                String term = c1.term.substring(0,mismatch) + c2.term;
                return new Candidate(term, c1.begin, c2.end);
            }
            else {
                return null;
            }
        }
        // case c(2)
        else if (c2.begin < c1.begin && c2.end > c1.begin) {
            getContext().getLogger().log(Level.FINEST, "Resolve case c2");
            int mismatch = c1.begin - c2.begin;
            if (mismatch < c1.term.length()) {
                String term = c2.term.substring(0,mismatch) + c1.term;
                return new Candidate(term, c2.begin, c1.end);
            }
            else {
                return null;
            }
        }
        else {
            getContext().getLogger().log(Level.WARNING, "Reached unexpected case when resolving overlaps.");
            getContext().getLogger().log(Level.WARNING, c1.toString());
            getContext().getLogger().log(Level.WARNING, c2.toString());
            return null;
        }
    }

    private class Candidate {
        private final String term;
        private final int begin;
        private final int end;
        public Candidate(String term, int begin, int end) {
            super();
            this.term = term;
            this.begin = begin;
            this.end = end;
        }
        @Override
		public String toString() {
            return term + " (" + begin + " - " + end + ")";
        }

    }

    private class Overlap {
        private final Candidate candidate;
        private final int offset;
        public Overlap(Candidate candidate, int offset) {
            super();
            this.candidate = candidate;
            this.offset = offset;
        }
        @Override
		public String toString() {
            return candidate.toString() + " #:" + offset;
        }
    }
}
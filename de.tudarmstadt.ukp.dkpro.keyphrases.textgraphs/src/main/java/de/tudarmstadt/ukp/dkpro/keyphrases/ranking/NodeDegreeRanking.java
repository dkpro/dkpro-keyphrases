package de.tudarmstadt.ukp.dkpro.keyphrases.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WeightedAnnotationPair;

/**
 * Creates keyphrases from a cooccurrence graph using the node degree as the ranking method.
 * If the parameter "WeightedNodeDegree" is set to true, the weighted node degree is used. Otherwise, the score of a node is its normal degree.
 *
 * Normal node degree should be used with care with Lexical-Semantic-Graphs, as they are usually fully connected. Thus, normal node degree does not give sensible results.
 *
 * @author zesch
 *
 */
public class NodeDegreeRanking extends JCasAnnotator_ImplBase {
    public static final String PARAM_WEIGHTED = "WeightedNodeDegree";
    @ConfigurationParameter(name=PARAM_WEIGHTED, mandatory=true, defaultValue="false")
    private boolean weighted;

    private int nextFreeTermId = 0;

    private Map<String, Integer> termMap;
    private Map<Integer, Set<Integer>> neighborMap;
    private Map<Integer, List<Double>> scoreMap;
    private Map<Integer, int[]> offsetMap;
    private Integer numberEdges = 0;
    private Double totalScore = 0.0;

    private JCas jcas;


    @Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
        getContext().getLogger().log(Level.CONFIG, "Entering " + this.getClass().getSimpleName());

        this.jcas = jcas;

        initializeDataStructures();

        String documentText = jcas.getDocumentText().toLowerCase();

        for (String term : termMap.keySet()) {
            int termId = termMap.get(term);

            double score;
            if (weighted) {
                score = getWeightedScore(termId, scoreMap);
            }
            else {
                score = getUnweightedScore(termId, scoreMap);
            }

            if (!documentText.contains(term.toLowerCase())) {
                getContext().getLogger().log(Level.WARNING, "Document text does not contain keyphrase: " + term.toLowerCase());
                continue;
            }

            int[] offset = offsetMap.get(termId);

            Keyphrase keyphrase = new Keyphrase(jcas);
            keyphrase.setScore(score);
            keyphrase.setBegin(offset[0]);
            keyphrase.setEnd(offset[1]);
            keyphrase.setKeyphrase(term);
            keyphrase.addToIndexes();
        }
	}

    /**
     * Initializes the internal datastructures.
     * @throws AnalysisEngineProcessException
     */
    private void initializeDataStructures() throws AnalysisEngineProcessException {

        termMap = new HashMap<String, Integer>();
        neighborMap = new HashMap<Integer, Set<Integer>>();
        scoreMap = new HashMap<Integer, List<Double>>();
        offsetMap = new HashMap<Integer, int[]>();

        for (AnnotationPair ap : JCasUtil.select(jcas, AnnotationPair.class)) {
            String term1 = ap.getStringRepresentation1();
            String term2 = ap.getStringRepresentation2();

            int start1 = ap.getAnnotation1().getBegin();
            int start2 = ap.getAnnotation2().getBegin();
            int end1   = ap.getAnnotation1().getEnd();
            int end2   = ap.getAnnotation2().getEnd();

            int[] offsetTerm1 = new int[]{start1, end1};
            int[] offsetTerm2 = new int[]{start2, end2};

            double weight = 1.0;
            if (weighted) {
                weight = ((WeightedAnnotationPair) ap).getWeight();
            }
            totalScore+=weight;
            ++numberEdges;

            update(term1, term2, offsetTerm1, offsetTerm2, weight);
        }
    }

    private void update(String t1, String t2, int[] offset1, int[] offset2, double score) {
        // add a new term
        if (!termMap.containsKey(t1)) {
            addTerm(t1, offset1);
        }
        if (!termMap.containsKey(t2)) {
            addTerm(t2, offset2);
        }

        // update datastructures when hitting a known term
        updateTerm(t1, t2, score);
        updateTerm(t2, t1, score);
    }

    private void addTerm(String t1, int[] offset) {
        termMap.put(t1, nextFreeTermId);
        neighborMap.put(nextFreeTermId, new HashSet<Integer>());
        scoreMap.put(nextFreeTermId, new ArrayList<Double>());
        offsetMap.put(nextFreeTermId, offset);
        nextFreeTermId++;
    }

    private void updateTerm(String t1, String t2, double score) {
        int currentWordId = termMap.get(t1);
        Set<Integer> neighbors = neighborMap.get(currentWordId);
        neighbors.add(termMap.get(t2));

        List<Double> scores = scoreMap.get(currentWordId);
        scores.add(score);
    }

    /**
     * @param id The id of the node.
     * @param scoreMap A map with the edge scores of all nodes.
     * @return The score (i.e. the weighted node degree) of the node indicated by the given id.
     */
    private double getWeightedScore(int id, Map<Integer,List<Double>> scoreMap) {
        double result = 0.0;
        List<Double> scores = scoreMap.get(id);
        for (Double score : scores) {
            if (score >= 0.0) {
                result += score;
            }
        }
        return result / totalScore;
    }

    /**
     * @param id The id of the node.
     * @param scoreMap A map with the edge scores of all nodes.
     * @return The score (i.e. the unweighted node degree) of the node indicated by the given id.
     */
    private double getUnweightedScore(int id, Map<Integer,List<Double>> scoreMap) {
        double result = 0.0;
        List<Double> scores = scoreMap.get(id);
        for (Double score : scores) {
            result += score;
        }
        return result / numberEdges;
    }
}
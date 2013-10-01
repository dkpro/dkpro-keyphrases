/*******************************************************************************
 * Copyright 2013

 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.CasUtil;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathInfo;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WeightedAnnotationPair;

/**
 * Creates a fully connected lexical graph by adding AnnotationPair annotations that represent edges in the graph.
 *
 * The type of the the annotation used, is indicated by a the AnnotationType parameter.
 *
 * The annotator puts an edge in the graph, if two annotations co-occur in the window size.
 *
 * If UsePosFilter is set to true, the annotator expects FilteredToken annotations to be present. It only takes annotation that have a corresponding (same offset) FilteredToken.
 *
 * @author zesch, n_erbs
 *
 */
public class CooccurrenceGraph extends JCasAnnotator_ImplBase {
    /**
     * Size of the context window.
     * Two nodes in the graph are connected if their corresponding lexical units co-occur within a window of that size.
     */
    public static final String PARAM_WINDOW_SIZE = "WindowSize";
    @ConfigurationParameter(name=PARAM_WINDOW_SIZE, mandatory=true, defaultValue="2")
    private int windowSize;

    /**
     * A feature path corresponding to the DKPro type that should be used for building the graph.
     */
    public static final String PARAM_FEATURE_PATH = "FeaturePath";
    @ConfigurationParameter(name=PARAM_FEATURE_PATH)
    private String featurePath;


    // datastructures for representing the cooccurrence graph
    private Map<String, Integer> termMap;
    private HashMap<Edge, Integer> edgeMap;

    @Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
        getContext().getLogger().log(Level.CONFIG, "Entering " + this.getClass().getSimpleName());

        // data structures need to be initialized here to provide fresh and clean ones for each run
        // Do NOT put that into initialize()!
        termMap = new HashMap<String, Integer>();
        edgeMap = new HashMap<Edge,Integer>();

        initializeGraph(jcas);


        //calculates the maximum amount of connections between two nodes and normalizes every number with this value
        int maxAmount = 1;
        for(Integer amountOfConnections : edgeMap.values()){
        	if(amountOfConnections > maxAmount){
        		maxAmount = amountOfConnections;
        	}
        }

        for (Edge edge : edgeMap.keySet()) {
            WeightedAnnotationPair pair = new WeightedAnnotationPair(jcas);
            pair.setAnnotation1( (Annotation) edge.annotation1 );
            pair.setAnnotation2( (Annotation) edge.annotation2 );
            pair.setStringRepresentation1( edge.term1 );
            pair.setStringRepresentation2( edge.term2 );
            pair.setWeight(edgeMap.get(edge)/(double) maxAmount);
            pair.addToIndexes();
        }
	}

    /**
     * Initializes the co-occurrence graph.
     * @param jcas The JCas.
     * @throws AnalysisEngineProcessException
     */
    private void initializeGraph(JCas jcas) throws AnalysisEngineProcessException {

        // initialize the FeaturePathInfo with the corresponding part
        FeaturePathInfo fp = new FeaturePathInfo();

        // separate typename and featurepath
        String[] segments = featurePath.split("/", 2);

        String typeName = segments[0];
        Type t = jcas.getCas().getTypeSystem().getType(typeName);
        if (t == null) {
        	throw new AnalysisEngineProcessException(new IllegalStateException("Type " +
        			typeName + " not found in type system"));
        }

        try {
            if (segments.length > 1) {
                fp.initialize(segments[1]);
            }
            else {
                fp.initialize("");
            }
        } catch (FeaturePathException e) {
            throw new AnalysisEngineProcessException(e);
        }

        Queue<WindowItem> window = new LinkedList<WindowItem>();
        int wordId = 0;
        for (Sentence sentence : JCasUtil.select(jcas, Sentence.class)) {
            List<AnnotationFS> annotations = CasUtil.selectCovered(jcas.getCas(), t, sentence);

            for (AnnotationFS a : annotations) {
                String term = fp.getValue(a);

                int currentWordId;
                if (!termMap.containsKey(term)) {
                    // add a new term to the datastructures
                    termMap.put(term, wordId);
                    currentWordId = wordId;
                    wordId++;
                } else {
                    currentWordId = termMap.get(term);
                }

                for (WindowItem windowItem : window) {
                    if (!termMap.containsKey(windowItem.getTerm())) {
                        // add a new term to the datastructures
                        termMap.put(windowItem.getTerm(), wordId);
                        wordId++;
                    }
                    int neighborId = termMap.get(windowItem.getTerm());
                    if (neighborId < currentWordId) {
                        addToEdgeSet(windowItem.getTerm(), term, windowItem.a, a, neighborId, currentWordId);
                    }
                    else {
                    	addToEdgeSet(term, windowItem.getTerm(), a, windowItem.a, currentWordId, neighborId);
                    }
                }

                // slide the window
                window.offer(new WindowItem(a, term));
                if (window.size() >= windowSize) {
                    window.poll();
                }
            }
        }
    }

    private void addToEdgeSet(String term1, String term2, AnnotationFS a1,
			AnnotationFS a2, int id1, int id2) {
    	Edge edge = new Edge(term1, term2, a1, a2, id1, id2);
    	if (edgeMap.containsKey(edge)) {
    		edgeMap.put(edge, edgeMap.get(edge).intValue()+1);
    	}
    	else{
    		edgeMap.put(edge, 1);
    	}
	}

    protected class WindowItem {
        private AnnotationFS a;
        private String term;

        public WindowItem(AnnotationFS a, String term) {
            super();
            this.a = a;
            this.term = term;
        }

        public AnnotationFS getA() {
            return a;
        }

        public void setA(AnnotationFS a) {
            this.a = a;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }
    }

    protected class Edge {
        private AnnotationFS annotation1;
        private AnnotationFS annotation2;
        private String term1;
        private String term2;
        private int nodeId1;
        private int nodeId2;

        public Edge(String term1, String term2, AnnotationFS a1, AnnotationFS a2, int nodeId1, int nodeId2) {
            super();
            this.term1 = term1;
            this.term2 = term2;
            this.annotation1 = a1;
            this.annotation2 = a2;
            this.nodeId1 = nodeId1;
            this.nodeId2 = nodeId2;
        }
        public int getNodeId1() {
            return nodeId1;
        }
        public void setNodeId1(int nodeId1) {
            this.nodeId1 = nodeId1;
        }
        public int getNodeId2() {
            return nodeId2;
        }
        public void setNodeId2(int nodeId2) {
            this.nodeId2 = nodeId2;
        }
        public String getTerm1() {
            return term1;
        }
        public void setTerm1(String term1) {
            this.term1 = term1;
        }
        public String getTerm2() {
            return term2;
        }
        public void setTerm2(String term2) {
            this.term2 = term2;
        }
        public AnnotationFS getAnnotation1() {
            return annotation1;
        }
        public void setAnnotation1(Annotation annotation1) {
            this.annotation1 = annotation1;
        }
        public AnnotationFS getAnnotation2() {
            return annotation2;
        }
        public void setAnnotation2(Annotation annotation2) {
            this.annotation2 = annotation2;
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(nodeId1);
            sb.append("-");
            sb.append(nodeId2);
            return sb.toString();
        }
        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = 1;
            result = PRIME * result + nodeId1;
            result = PRIME * result + nodeId2;
            return result;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
				return true;
			}
            if (obj == null) {
				return false;
			}
            if (getClass() != obj.getClass()) {
				return false;
			}
            final Edge other = (Edge) obj;
            if (nodeId1 != other.nodeId1) {
				return false;
			}
            if (nodeId2 != other.nodeId2) {
				return false;
			}
            return true;
        }
    }
}

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
package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.VectorEntry;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WeightedAnnotationPair;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WordPair;

public class PageRank {

	private final Log log = LogFactory.getLog(getClass());


	protected Map<String, Integer> termMap;
	protected List<TermRank> termRanks;
	protected FlexCompColMatrix adjacentMatrix;
	protected int maxRuns = -1;

	public PageRank() {
		// Do nothing
	}

	public PageRank(Map<String, Integer> termMap, Map<Integer, Set<Integer>> neighborMap) {
		super();
		this.termMap = termMap;
		this.termRanks = new ArrayList<TermRank>();
	}

	public void initializeFromDisambiguationCollection(Collection<Disambiguation> disambiguations) {
		// data structures need to be initialized here to provide fresh and
		// clean ones for each run
		termMap = new HashMap<String, Integer>();
		termRanks = new ArrayList<TermRank>();

		// first, we have to get all terms
		int termId = 0;
		for(Disambiguation disambiguation : disambiguations){
			FeatureStructure[] senses = disambiguation.getSenses().toArray();
			
			initializeFromSenses(senses, termId, disambiguation.getSourceId());
		}

		fillAdjacencyMatrixFromCollection(disambiguations);
	}

	private void initializeFromSenses(FeatureStructure[] senses, int termId, long sourceId) {
		for(FeatureStructure fs : senses){
			Sense sense = (Sense) fs;

			String term1 = String.valueOf(sourceId);
			String term2 = String.valueOf(sense.getId());

			// adds term if not already in map, and increases termId
			termId = updateTermMap(term1, termId, 0, 0);
			termId = updateTermMap(term2, termId, 0, 0);
			
			
			FSArray linkedSenses = sense.getLinkedSenses();
			if(linkedSenses!=null){
				initializeFromSenses(linkedSenses.toArray(), termId, sense.getId());
			}
			
		}
		
	}

	public void initializeFromIterator(FSIterator<Annotation> wpIterator, FSIterator<Annotation> srIterator, boolean weighted) {
		// data structures need to be initialized here to provide fresh and
		// clean ones for each run
		termMap = new HashMap<String, Integer>();
		termRanks = new ArrayList<TermRank>();

		// first, we have to get all terms
		int termId = 0;
		while (wpIterator.hasNext()) {

			WordPair wp = (WordPair) wpIterator.next();

			String term1 = wp.getWord1();
			String term2 = wp.getWord2();

			// adds term if not already in map, and increases termId
			termId = updateTermMap(term1, termId, wp.getToken1().getBegin(), wp.getToken1().getEnd());
			termId = updateTermMap(term2, termId, wp.getToken2().getBegin(), wp.getToken2().getEnd());
		}

		fillAdjacencyMatrix(wpIterator, srIterator, weighted);
	}

	public void initializeFromAnnotationPairIterator(Collection<AnnotationPair> pairs, boolean weighted) {
		// data structures need to be initialized here to provide fresh and
		// clean ones for each run
		termMap = new HashMap<String, Integer>();
		termRanks = new ArrayList<TermRank>();

		// first, we have to get all terms
		int termId = 0;
		for (AnnotationPair pair : pairs) {
		    
			String term1 = pair.getStringRepresentation1();
			String term2 = pair.getStringRepresentation2();

			// adds term if not already in map, and increases termId
			termId = updateTermMap(term1, termId, pair.getAnnotation1().getBegin(), pair.getAnnotation1().getEnd());
			termId = updateTermMap(term2, termId, pair.getAnnotation2().getBegin(), pair.getAnnotation2().getEnd());
		}

		fillAdjacencyMatrixAnnotationPair(pairs, weighted);
	}

	public void run() {
		int annotationSize = termMap.size();
		double d = 0.85;
		double threshold = 0.001; // threshold for stopping the iteration
		DenseVector biasVector = createPreferenceVector(annotationSize, d);
		Vector scoreVector = initScoreVector(annotationSize);
		Vector degreeVector = getDegreeVector(annotationSize, adjacentMatrix);

		FlexCompColMatrix transitionMatrix = createTransitionMatrix(degreeVector);

		nodeRank(transitionMatrix, d, biasVector, scoreVector, threshold);

		for (int i = 0; i < scoreVector.size(); i++) {
			termRanks.get(i).setScore(scoreVector.get(i));
		}
		Collections.sort(termRanks);
	}

	public List<TermRank> getTermRanks() {
		return termRanks;
	}

	/**
	 * Updates the termMap and the list of termRanks. Increases termId, if a new
	 * term is inserted into the termMap.
	 *
	 * @param term tthe term itself
	 * @param termId term id
	 * @param begin begin offset
	 * @param end end offset
	 * @return The same termId as the parameter, if no new term was inserted.
	 *         The increased termId otherwise.
	 */
	protected int updateTermMap(String term, int termId, int begin, int end) {
		int[] offset = new int[]{begin,end};
		if (!termMap.containsKey(term)) {
			termMap.put(term, termId);
			termId++;

			TermRank newRank = new TermRank(term, offset);
			termRanks.add(newRank);
		} else {
			TermRank rank = termRanks.get(termMap.get(term));
			rank.addTerm(term, offset);
		}
		return termId;
	}

	protected void fillAdjacencyMatrixFromCollection(Collection<Disambiguation> disambiguations) {
		adjacentMatrix = new FlexCompColMatrix(termMap.size(), termMap.size());

		for(Disambiguation disambiguation : disambiguations){
			FeatureStructure[] senses = disambiguation.getSenses().toArray();
			for(FeatureStructure fs : senses){
				Sense sense = (Sense) fs;
				
				fillAdjacencyMatrixWithSenseRecursively(sense,disambiguation.getSourceId());

			}
		}
	}
	private void fillAdjacencyMatrixWithSenseRecursively(Sense sense,long sourceId) {
		String term1 = String.valueOf(sourceId);
		String term2 = String.valueOf(sense.getId());

		int termId1 = termMap.get(term1);
		int termId2 = termMap.get(term2);

		if (termId1 != termId2) {
			adjacentMatrix.set(termId1, termId2, sense.getScore());
			adjacentMatrix.set(termId2, termId1, sense.getScore());
		}
		FSArray linkedSenses = sense.getLinkedSenses();
		if(linkedSenses!=null){
			for(FeatureStructure fs : linkedSenses.toArray()){
				Sense linkedSense = (Sense) fs;
				fillAdjacencyMatrixWithSenseRecursively(linkedSense, sense.getId());
			}
		}
	}

	protected void fillAdjacencyMatrix(FSIterator<Annotation> wpIterator, FSIterator<Annotation> srIterator, boolean weighted) {
		adjacentMatrix = new FlexCompColMatrix(termMap.size(), termMap.size());

		if (weighted) {
			while (srIterator.hasNext()) {
				EdgeWeight sr = (EdgeWeight) srIterator.next();
				WordPair wp = sr.getWordPair();
				double score = sr.getRelatednessValue();

				String term1 = wp.getWord1();
				String term2 = wp.getWord2();

				int termId1 = termMap.get(term1);
				int termId2 = termMap.get(term2);

				if (termId1 != termId2) {
					adjacentMatrix.set(termId1, termId2, score);
					adjacentMatrix.set(termId2, termId1, score);
				}
			}

		} else {
			wpIterator.moveToFirst();
			while (wpIterator.hasNext()) {
				WordPair wp = (WordPair) wpIterator.next();

				String term1 = wp.getWord1();
				String term2 = wp.getWord2();

				int termId1 = termMap.get(term1);
				int termId2 = termMap.get(term2);

				if (termId1 != termId2) {
					adjacentMatrix.set(termId1, termId2, 1.0);
					adjacentMatrix.set(termId2, termId1, 1.0);
				}
			}
		}

	}

	protected void fillAdjacencyMatrixAnnotationPair(Collection<AnnotationPair> pairs, boolean weighted) {
		adjacentMatrix = new FlexCompColMatrix(termMap.size(), termMap.size());

		for (AnnotationPair pair : pairs) {
            String term1 = pair.getStringRepresentation1();
            String term2 = pair.getStringRepresentation2();

            int termId1 = termMap.get(term1);
            int termId2 = termMap.get(term2);

            double weight = 1.0;
            if (weighted) {
                weight = ((WeightedAnnotationPair) pair).getWeight();
            }

            if (termId1 != termId2) {
                adjacentMatrix.set(termId1, termId2, weight);
                adjacentMatrix.set(termId2, termId1, weight);
            }
		}
	}

	protected FlexCompColMatrix createTransitionMatrix(Vector degreeVector) {
		return createTransitionMatrix(adjacentMatrix, degreeVector, termMap.size());
	}

	protected FlexCompColMatrix createTransitionMatrix(FlexCompColMatrix adjacentMatrix, Vector degreeVector, int degree) {
		FlexCompColMatrix transitionMatrix = new FlexCompColMatrix(degree,degree);
		for (int i = 0; i < adjacentMatrix.numColumns(); i++) {
			SparseVector col = adjacentMatrix.getColumn(i);
			SparseVector transCol = transitionMatrix.getColumn(i);

			for (VectorEntry entry : col) {
				if (i != entry.index()) {
					double nominator = degreeVector.get(entry.index());
					if (nominator != 0) {
						double value = entry.get() / nominator;
						transCol.set(entry.index(), value);
					}
				} else {
					transCol.set(entry.index(), 0);
				}
			}

		}
		return transitionMatrix;
	}

	protected Vector initScoreVector(int tokenSize) {
		Vector scoreVector = new DenseVector(tokenSize);
		double initValue = 1.0;
		for (int i = 0; i < scoreVector.size(); i++) {
			scoreVector.set(i, initValue);
		}
		return scoreVector;
	}

	protected Vector getDegreeVector(int tokenSize, FlexCompColMatrix adjacentMatrix) {
		Vector degreeVector = new DenseVector(tokenSize);
		for (int i = 0; i < adjacentMatrix.numColumns(); i++) {
			SparseVector col = adjacentMatrix.getColumn(i);
			double sum = 0;
			for (VectorEntry entry : col) {
				sum += entry.get();
			}
			degreeVector.set(i, sum);
		}
		return degreeVector;
	}

	/**
	 * Updates the score vector.
	 *
	 * @param transitionMatrix the transition matrix
	 * @param d d
	 * @param biasVector bias vector
	 * @param scoreVector score vector
	 * @param degreeVector degree vector
	 * @param threshold threshold
	 */
	protected void nodeRank(FlexCompColMatrix transitionMatrix, double d, DenseVector biasVector, Vector scoreVector, double threshold) {
		double diff;
		int count = 0;
		boolean keepRun = true;
		do {
			Vector oldScore = new DenseVector(scoreVector.size());
			for (int i = 0; i < oldScore.size(); i++) {
				oldScore.set(i, scoreVector.get(i));
			}
			for (int i = 0; i < transitionMatrix.numColumns(); i++) {
				SparseVector col = transitionMatrix.getColumn(i);
				double sum = 0;
				for (VectorEntry entry : col) {
					if (entry.get() > 0) {
						double oldValue = oldScore.get(entry.index());
						sum += entry.get() * oldValue;
					}
				}

				sum = (1.0 - d) * biasVector.get(i) + d * sum;
				scoreVector.set(i, sum);
			}

			double sum = 0;
			for (VectorEntry entry : scoreVector) {
				double temp = entry.get() - oldScore.get(entry.index());
				sum += Math.abs(temp);
			}

			diff = sum;
			count++;
			if (maxRuns != -1 && count > maxRuns) {
				keepRun = false;
			}
		} while (diff > threshold && keepRun);

		log.info("# iterations: " + count);
	}

	public int getMaxRuns() {
		return maxRuns;
	}

	public void setMaxRuns(int maxRuns) {
		this.maxRuns = maxRuns;
	}

	protected DenseVector createPreferenceVector(int tokenSize, double d) {
		DenseVector biasVector = new DenseVector(tokenSize);
		double value = 1.0;
		for (int i = 0; i < biasVector.size(); i++) {
			biasVector.set(i, value);
		}
		return biasVector;
	}

	/**
	 * Storing temp results of term information. A term can either be a token,
	 * stem, or lemma.
	 *
	 * @author Lizhen Qu, zesch
	 *
	 */
	public class Term implements Comparable<Term> {
		private final String term;
		private final int[] offset;
		private double score;

		public Term(String term, int[] offset) {
			super();
			this.term = term;
			this.offset = offset;
		}

		public String getTerm() {
			return term;
		}

		public int[] getOffset() {
			return offset;
		}

		public double getScore() {
			return score;
		}

		public void setScore(double score) {
			this.score = score;
		}

		public int compareTo(Term other) {
			return Double.compare(other.getScore(), score);
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(term);
			buffer.append(" ");
			buffer.append(offset[0]);
			buffer.append(":");
			buffer.append(offset[1]);

			return buffer.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + Arrays.hashCode(offset);
			long temp;
			temp = Double.doubleToLongBits(score);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + ((term == null) ? 0 : term.hashCode());
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
			Term other = (Term) obj;
			if (!getOuterType().equals(other.getOuterType())) {
				return false;
			}
			if (!Arrays.equals(offset, other.offset)) {
				return false;
			}
			if (Double.doubleToLongBits(score) != Double.doubleToLongBits(other.score)) {
				return false;
			}
			if (term == null) {
				if (other.term != null) {
					return false;
				}
			} else if (!term.equals(other.term)) {
				return false;
			}
			return true;
		}

		private PageRank getOuterType() {
			return PageRank.this;
		}
	}

	/**
	 * Storing temp rank information of terms. A term can either be a token,
	 * stem, or lemma.
	 *
	 * @author Lizhen Qu, zesch
	 *
	 */
	public class TermRank implements Comparable<TermRank> {
		private double score;
		private final String term;
		private final Set<Term> termList;

		public TermRank(String term, int[] offset) {
			super();
			// REC 2009-08-07 - This assertion fails with stems - possibly it
			// is bogus if someone thinks this is really a problem, please track
			// it down and fix it.
			// assert term.length() == offset[1]-offset[0];

			this.term = term;
			termList = new HashSet<Term>();
			termList.add(new Term(term, offset));
		}

		/**
		 * Add the term, if it is not already in the list.
		 *
		 * @param term the term
		 * @param offset the offsets
		 */
		public void addTerm(String term, int[] offset) {
			// REC 2009-08-07 - This assertion fails with stems - possibly it
			// is bogus if someone thinks this is really a problem, please track
			// it down and fix it.
			// assert term.length() == offset[1]-offset[0];

			Term t = new Term(term, offset);
			if (!termList.contains(t)) {
				termList.add(t);
			}
		}

		public double getScore() {
			return score;
		}

		public String getStringRepresentation() {
			return term;
		}

		public Set<Term> getTermList() {
			return termList;
		}

		public void setScore(double score) {
			for (Term term : termList) {
				term.setScore(score);
			}
			this.score = score;
		}

		public int compareTo(TermRank other) {
			return Double.compare(other.getScore(), score);
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(term);
			buffer.append(" ");
			buffer.append(score);
			buffer.append(" ");
			buffer.append(StringUtils.join(termList, " "));

			return buffer.toString();
		}
	}
}

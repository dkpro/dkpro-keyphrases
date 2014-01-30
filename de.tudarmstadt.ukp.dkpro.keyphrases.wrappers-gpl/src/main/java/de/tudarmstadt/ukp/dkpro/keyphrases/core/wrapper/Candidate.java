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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.NGram;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.NC;
import de.tudarmstadt.ukp.dkpro.core.posfilter.PosFilter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;

public class Candidate implements KeyphraseConstants {

	public enum CandidateType {
		Token,
		Stem,
		Lemma,
		NamedEntity,
		NounPhrase,
		NGram
	}

	public enum PosType {
        ADJ,
        ADV,
        ART,
        CARD,
        CONJ,
        N,
        O,
        PP,
        PR,
        PUNC,
        V
    }

	/**
	 * If true, overlapping candidates are merged.
	 */
	private boolean resolveOverlaps;

	private AnalysisEngineDescription candidateAnnotator;

	private CandidateType type;

	/**
     * POS types that the filter should let pass.
     * Default is nouns and adjectives.
     */
    private Set<PosType> posToKeep = new HashSet<PosType>();

	public Candidate(CandidateType type) {
		initializeCandidate(type);
	}

	public Candidate(CandidateType type, PosType ... posToKeep) {
		initializeCandidate(type, posToKeep);
	}

	public Candidate(CandidateType type, boolean resolveOverlaps) {
		initializeCandidate(type, resolveOverlaps);
	}

	public Candidate(CandidateType type, boolean resolveOverlaps, PosType ... posToKeep) {
		initializeCandidate(type, resolveOverlaps, posToKeep);
	}

	private void initializeCandidate(CandidateType type, boolean resolveOverlaps, PosType ... posToKeep) {
		this.setType(type);
		this.setPosToKeep(posToKeep);
		this.setResolveOverlaps(resolveOverlaps);
		try {
			this.setCandidateAnnotator(createCandidateAggregate());
		} catch (ResourceInitializationException e) {
			throw new RuntimeException(e);
		}
	}

	private void initializeCandidate(CandidateType type) {
		initializeCandidate(type, false, new PosType[0]);
	}

	private void initializeCandidate(CandidateType type, PosType ... posToKeep) {
		initializeCandidate(type, false, posToKeep);
	}

	private AnalysisEngineDescription createCandidateAggregate() throws ResourceInitializationException {
		// check if we need to instantiate a POS filter

		List<AnalysisEngineDescription> aes = new ArrayList<AnalysisEngineDescription>();
		if (posToKeep.size() > 0) {
			aes.add(createPosFilter());
		}
		aes.add(createCandidateAnnotator());
		
		return createEngineDescription(aes.toArray(new AnalysisEngineDescription[aes.size()]));

	}

	private AnalysisEngineDescription createCandidateAnnotator() throws ResourceInitializationException {
		String featurePath = null;
		switch (type) {
			case Token :
				featurePath = Token.class.getName();
				break;
			case Stem :
				featurePath = Stem.class.getName();
				break;
			case Lemma :
				featurePath = Lemma.class.getName();
				break;
			case NamedEntity :
				featurePath = NamedEntity.class.getName();
				break;
			case NounPhrase :
				featurePath = NC.class.getName();
				break;
			case NGram :
				featurePath = NGram.class.getName();
				break;
			default :
				featurePath = Token.class.getName();
				break;
		}

		return createEngineDescription(
				CandidateAnnotator.class,
                CandidateAnnotator.PARAM_FEATURE_PATH,     featurePath,
                CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, resolveOverlaps
        );
	}

	private AnalysisEngineDescription createPosFilter() throws ResourceInitializationException {

        return createEngineDescription(
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

	public Set<PosType> getPosToKeep() {
        return posToKeep;
    }

    public void setPosToKeep(Set<PosType> posToKeep) {
        this.posToKeep = posToKeep;
    }

    public void setPosToKeep(PosType ... posToKeep) {
        Set<PosType> posSet = new HashSet<PosType>();
        for (PosType pos : posToKeep) {
            posSet.add(pos);
        }

        this.posToKeep = posSet;
    }

    public CandidateType getType()
    {
        return type;
    }

    public void setType(CandidateType type) {
		this.type = type;
	}

	public AnalysisEngineDescription getCandidateAnnotator() {
		return candidateAnnotator;
	}

	private void setCandidateAnnotator(AnalysisEngineDescription candidateAnnotator) {
		this.candidateAnnotator = candidateAnnotator;
	}

	public boolean isResolveOverlaps() {
		return resolveOverlaps;
	}

	public void setResolveOverlaps(boolean resolveOverlaps) {
		this.resolveOverlaps = resolveOverlaps;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Candidate Type: " + type.name()); sb.append(LF);
		sb.append("POS to keep: " + StringUtils.join(posToKeep, ", ")); sb.append(LF);
		sb.append("Resolve overlaps: " + resolveOverlaps); sb.append(LF);
		return sb.toString();
	}
}

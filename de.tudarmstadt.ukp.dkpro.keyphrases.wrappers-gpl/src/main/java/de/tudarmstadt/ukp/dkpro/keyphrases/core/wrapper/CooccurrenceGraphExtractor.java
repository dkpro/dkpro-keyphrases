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

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.NodeDegreeRankingFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.PageRankRankingFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.CooccurrenceGraph;

public class CooccurrenceGraphExtractor extends KeyphraseExtractor_ImplBase {

    public enum RankingMode {
        NodeDegreeUnweighted,
        NodeDegreeWeighted,
        PageRankUnweighted,
        PageRankWeighted,
    }

    /**
     * The size of the context window when building the cooccurrence graph.
     * Default is 2.
     */
    private int windowSize = 2;

    /**
     * Whether to use node degree or PageRank for ranking (both can either use edge weights or not).
     */
    private RankingMode rankingMode = RankingMode.PageRankWeighted;

    @Override
    protected AnalysisEngineDescription createKeyphraseExtractorAggregate() throws ResourceInitializationException {

        return createEngineDescription(

                createPreprocessingComponents(getCandidate().getType()),

                createEngineDescription(
                        CooccurrenceGraph.class,
                        CooccurrenceGraph.PARAM_FEATURE_PATH, Keyphrase.class.getName(),
                        CooccurrenceGraph.PARAM_WINDOW_SIZE, windowSize
                ),

                createRanker(getRankingMode()),

                createPostprocessingComponents()
          );
    }

    private AnalysisEngineDescription createRanker(RankingMode mode) throws ResourceInitializationException {
        AnalysisEngineDescription ranker = null;

        if (mode.equals(RankingMode.PageRankWeighted)) {
            ranker = PageRankRankingFactory.getPageRankRanking_weighted();
        }
        else if (mode.equals(RankingMode.PageRankUnweighted)) {
            ranker = PageRankRankingFactory.getPageRankRanking_unweighted();
        }
        else if (mode.equals(RankingMode.NodeDegreeWeighted)) {
            ranker = NodeDegreeRankingFactory.getNodeDegreeRanking_weighted();
        }
        else if (mode.equals(RankingMode.NodeDegreeUnweighted)) {
            ranker = NodeDegreeRankingFactory.getNodeDegreeRanking_unweighted();
        }

        return ranker;

    }

    /**
     * A convenience factory method that configures a CooccurrenceGraphExtractor to act like
     * the TextRank method by (Mihalcea & Tarau, 2004).
     *
     * @param language The language the extractor should work on.
     * @return A CooccurrenceGraphExtractor configured like the original TextRank method.
     */
    public static CooccurrenceGraphExtractor createTextRankExtractor(String language) {
        CooccurrenceGraphExtractor textRank = new CooccurrenceGraphExtractor();
        textRank.setLanguage(language);
        textRank.setMinKeyphraseLength(1);
        textRank.setMaxKeyphraseLength(4);
        textRank.setWindowSize(2);
        textRank.setRankingMode(RankingMode.PageRankWeighted);
        textRank.setCandidate(
        		new Candidate(
        				CandidateType.Token,
        				false,
        				PosType.N,
        				PosType.ADJ
        		)
        );

        return textRank;
    }

    /**
     * A convenience factory method that configures a {@link CooccurrenceGraphExtractor} to act like
     * the TextRank method by (Mihalcea & Tarau, 2004) for English text.
     *
     * @return A {@link CooccurrenceGraphExtractor} for English text configured like the original TextRank method.
     */
    public static CooccurrenceGraphExtractor createTextRankExtractor() {
        return createTextRankExtractor("en");
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public RankingMode getRankingMode() {
        return rankingMode;
    }

    public void setRankingMode(RankingMode rankingMode) {
        this.rankingMode = rankingMode;
    }

    @Override
    public String getConfigurationDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getConfigurationDetails());
		sb.append("Window Size: " + this.getWindowSize()); sb.append(LF);
		sb.append(LF);
		return sb.toString();
	}

}
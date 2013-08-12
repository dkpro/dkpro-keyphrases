package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.NodeDegreeRankingFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.PageRankRankingFactory;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.CooccurrenceGraph;

/**
 * Book-index-phrase-extractor based on a cooccurrence graph.
 *
 * @author Mateusz Parzonka
 *
 */
public class CooccurrenceGraphExtractor
	extends BookIndexPhraseExtractor_BaseImpl
{

	public enum RankingMode
	{
		NodeDegreeUnweighted, NodeDegreeWeighted, PageRankUnweighted, PageRankWeighted,
	}

	private RankingMode rankingMode = RankingMode.NodeDegreeUnweighted;
	private int windowSize = 2;

	@Override
	protected AnalysisEngineDescription createRanker()
		throws ResourceInitializationException
	{
		return createAggregateDescription(createPrimitiveDescription(
				CooccurrenceGraph.class,
				CooccurrenceGraph.PARAM_FEATURE_PATH, KeyphraseCandidate.class.getName(),
				CooccurrenceGraph.PARAM_WINDOW_SIZE, windowSize),

		createCooccurrenceRanker(getRankingMode()));
	}

	public RankingMode getRankingMode()
	{
		return rankingMode;
	}

	/**
	 * Configures the ranking mode of the coocurrence graph.
	 * 
	 * @param rankingMode
	 *          Choose between PageRank and NodeDegree<br>
	 *          weighting==true : Edge weight quantifies the number of
	 *          cooccurrences<br>
	 *          weighting==false: No edge weights. Cooccurrences with the
	 *          frequency 1 are rated as high as those with a frequency of 100.
	 */
	public void setRankingMode(RankingMode rankingMode)
	{
		this.rankingMode = rankingMode;
	}

	public int getWindowSize()
	{
		return windowSize;
	}

	/**
	 * Size of the context window. Two nodes in the graph are connected if their
	 * corresponding lexical units (marked as candidates) co-occur within a window
	 * of the given size.
	 *
	 * @param windowSize
	 *          the windowSize to set
	 */
	public void setWindowSize(int windowSize)
	{
		this.windowSize = windowSize;
	}

	private AnalysisEngineDescription createCooccurrenceRanker(
			RankingMode rankingMode)
		throws ResourceInitializationException
	{
		switch (rankingMode) {
		case PageRankWeighted:
			return PageRankRankingFactory.getPageRankRanking_weighted();
		case PageRankUnweighted:
			return PageRankRankingFactory.getPageRankRanking_unweighted();
		case NodeDegreeWeighted:
			return NodeDegreeRankingFactory.getNodeDegreeRanking_weighted();
		case NodeDegreeUnweighted:
			return NodeDegreeRankingFactory.getNodeDegreeRanking_unweighted();
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("Ranker: CooccurrenceGraph").append(LF);
		sb.append("CooccurenceRankingMethod: ").append(rankingMode).append(LF);
		sb.append("Context-window size: ").append(windowSize).append(LF);
		return sb.toString();
	}

}

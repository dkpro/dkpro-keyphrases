package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures;

import java.io.Serializable;
import java.util.Locale;

/**
 * Represents performance measures like precision, recall and f-measure.
 *
 * @author Mateusz Parzonka
 *
 */
public class PerformanceMeasures
	implements Serializable
{

	private static final long serialVersionUID = -5821926209167303530L;

	private final int coveredCount;
	private final int goldCount;
	private final int retrievedCount;
	private final int relevantCount;

	/**
	 * Creates performance measures with parameters from the information retrieval
	 * context, where an retrieved item can cover 0 - 1 gold items and a gold item
	 * can be covered by 0 - 1 retrieved items.
	 *
	 * @param retrievedCount
	 *          count of items which apply for a match with gold items
	 * @param relevantCount
	 *          count of retrieved items that correctly match gold items
	 * @param goldCount
	 *          count of items that are tried to be matched by retrieved items
	 */
	public PerformanceMeasures(int retrievedCount, int relevantCount,
			int goldCount)
	{
		this(retrievedCount, relevantCount, relevantCount, goldCount);
	}

	/**
	 * Creates performance measures with parameters from the approximate matching
	 * context, where an retrieved item can cover 0 - n gold items and a gold item
	 * can be covered by 0 - 1 retrieved items.
	 *
	 * @param retrievedCount
	 *          count of items which apply for a match with gold items
	 * @param relevantCount
	 *          count of retrieved items that correctly match gold items (aka:
	 *          true positives)
	 * @param coveredCount
	 *          count of gold items that are correctly matched by retrieved items
	 * @param goldCount
	 *          count of items that are tried to be matched by retrieved items
	 */
	public PerformanceMeasures(int retrievedCount, int relevantCount,
			int coveredCount, int goldCount)
	{
		super();
		if (relevantCount > retrievedCount)
			throw new IllegalArgumentException(
					"relevantCount may not be greater then retrievedCount");
		if (relevantCount > goldCount)
			throw new IllegalArgumentException(
					"relevantCount may not be greater then goldCount");
		if (coveredCount > goldCount)
			throw new IllegalArgumentException(
					"coveredCount may not be greater then goldCount");
		this.retrievedCount = retrievedCount;
		this.relevantCount = relevantCount;
		this.coveredCount = coveredCount;
		this.goldCount = goldCount;
	}

	/**
	 * @return count of items that are to be matched by retrieved items
	 */
	public int getGoldCount()
	{
		return goldCount;
	}

	/**
	 * @return count of retrieved items that correctly matches gold items
	 */
	public int getRelevantCount()
	{
		return relevantCount;
	}

	/**
	 * @return count of items which applies for a match with the gold items
	 */
	public int getRetrievedCount()
	{
		return retrievedCount;
	}

	/**
	 * @return count of gold items that get covered by retrieved items
	 */
	public int getCoveredCount()
	{
		return coveredCount;
	}

	/**
	 * @return The fraction of relevant items that are retrieved.
	 */
	public double getRecall()
	{
		return coveredCount / (double) goldCount;
	}

	/**
	 * @return The fraction of retrieved items that are relevant.
	 */
	public double getPrecision()
	{
		return relevantCount / (double) retrievedCount;
	}

	/**
	 * @return The traditional F-measure or balanced F-score (F1 score) as the
	 *         harmonic mean of precision and recall.
	 */
	public double getFMeasure()
	{
		double precision = getPrecision();
		double recall = getRecall();

		if (precision + recall == 0)
			return 0;

		return 2 * precision * recall / (precision + recall);

	}

	/**
	 * Prints the measures as a triple in the format: <br>
	 * <code>P: _.___ R: _.___ F: _.___</code>
	 */
	@Override
	public String toString()
	{
		return String.format(Locale.US, "P: %.3f\t R: %.3f\t F: %.3f",
				getPrecision(), getRecall(), getFMeasure());
	}

}

package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures;

import java.io.Serializable;
import java.util.List;

/**
 * Represents the result of an evaluation for a document holding performance
 * measures.
 *
 * @author Mateusz Parzonka
 *
 */
public class DocumentPerformanceResult
	implements Serializable
{

	private static final long serialVersionUID = 2079776151348936663L;

	private final String documentTitle;
	private final int goldCount;
	private final List<Integer> truePositiveCounts;

	/**
	 * @param documentTitle
	 *          title of the evaluated document
	 * @param truePositiveCounts
	 *          represents the counts of gold items the i-th retrieved item has
	 *          covered <br>
	 *          Example: The list (2,0,1) means: Retrieved item 0 has covered 2
	 *          gold items, retrieved item 1 has covered no gold items and
	 *          retrieved item 2 has covered 1 gold item. Thus, when using 3
	 *          retrieved items (the whole list) we cover 3 gold items in total
	 *          haveing 2 true positives.
	 *
	 * @param goldCount
	 *          the count of gold items
	 */
	public DocumentPerformanceResult(String documentTitle,
			List<Integer> truePositiveCounts, int goldCount)
	{
		super();

		// check if we have not too much true positives
		if (sumCoveredItems(truePositiveCounts, truePositiveCounts.size()) > goldCount)
			throw new IllegalArgumentException(
					"Registered more true positives then gold items exist");

		this.documentTitle = documentTitle;
		this.goldCount = goldCount;
		this.truePositiveCounts = truePositiveCounts;
	}

	/**
	 * Returns performance measures for this document and a given percentage of
	 * best ranked retrieved items.
	 *
	 * @param retrievedPercentage
	 *          The percentage of best ranked items taken into account represented
	 *          by a double in [0.0, 1.0].
	 * @return performance measures for this document
	 */
	public PerformanceMeasures getPerformanceMeasures(double retrievedPercentage)
	{
		return getPerformanceMeasures((int) Math
				.floor((getRetrievedCount() * retrievedPercentage)));
	}

	/**
	 * Returns performance measures for this document and a given number of
	 * retrieved items specified by count.
	 *
	 * @param retrievedItems
	 *          The number of best ranked items taken into account.<br>
	 *          If the number is greater then the total retrieved count, all items
	 *          are used.
	 * @return performance measures for this document
	 */
	public PerformanceMeasures getPerformanceMeasures(int retrievedItems)
	{
		int relevantItems = sumRelevantItems(truePositiveCounts, retrievedItems);
		int coveredItems = sumCoveredItems(truePositiveCounts, retrievedItems);
		return new PerformanceMeasures(retrievedItems, relevantItems, coveredItems,
				goldCount);
	}

	/**
	 * @param truePositiveCounts
	 *          represents the counts of gold items the i-th retrieved item has
	 *          covered
	 * @param numberOfUsedRetrievedItems
	 *          specifies the number of items which is used to cover the gold
	 *          items
	 * @return sum of retrieved items that cover (correctly match) gold items
	 *         (aka: true positives) using a limited number of retrieved items.
	 */
	private static int sumRelevantItems(List<Integer> truePositiveCounts,
			int numberOfUsedRetrievedItems)
	{
		int currentListPosition = 0;
		int sum = 0;
		for (Integer i : truePositiveCounts) {
			if (++currentListPosition > numberOfUsedRetrievedItems)
				return sum;
			if (i > 0)
				sum += 1;
		}
		return sum;
	}

	/**
	 * @param truePositiveCounts
	 *          represents the counts of gold items the i-th retrieved item has
	 *          covered
	 * @param numberOfUsedRetrievedItems
	 *          specifies the number of items which is used to cover the gold
	 *          items
	 * @return Sum of gold items that get covered (correctly matched) by retrieved
	 *         items (aka: true positives) using a limited number of retrieved
	 *         items.
	 */
	private static int sumCoveredItems(List<Integer> truePositiveCounts,
			int numberOfUsedRetrievedItems)
	{
		int currentListPosition = 0;
		int sum = 0;
		for (Integer i : truePositiveCounts) {
			if (++currentListPosition > numberOfUsedRetrievedItems)
				return sum;
			sum += i;
		}
		return sum;
	}

	/**
	 * @return total number of retrieved items
	 */
	public int getRetrievedCount()
	{
		return truePositiveCounts.size();
	}

	/**
	 * @return title of the evaluated document
	 */
	public String getDocumentTitle()
	{
		return documentTitle;
	}

	/**
	 * @return total number of gold items
	 */
	public int getGoldCount()
	{
		return goldCount;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((documentTitle == null) ? 0 : documentTitle.hashCode());
		result = prime * result + goldCount;
		result = prime * result
				+ ((truePositiveCounts == null) ? 0 : truePositiveCounts.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentPerformanceResult other = (DocumentPerformanceResult) obj;
		if (documentTitle == null) {
			if (other.documentTitle != null)
				return false;
		}
		else if (!documentTitle.equals(other.documentTitle))
			return false;
		if (goldCount != other.goldCount)
			return false;
		if (truePositiveCounts == null) {
			if (other.truePositiveCounts != null)
				return false;
		}
		else if (!truePositiveCounts.equals(other.truePositiveCounts))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "DocumentPerformanceResult [documentTitle=" + documentTitle
				+ ", goldCount=" + goldCount + ", truePositiveCounts="
				+ truePositiveCounts + "]";
	}

}

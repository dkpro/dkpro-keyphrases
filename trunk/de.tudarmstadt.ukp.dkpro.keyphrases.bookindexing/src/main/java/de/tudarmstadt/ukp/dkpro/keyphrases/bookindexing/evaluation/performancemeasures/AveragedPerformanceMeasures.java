package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Represents averaged performance measures for a document collection.
 *
 * @author Mateusz Parzonka
 *
 */
public class AveragedPerformanceMeasures
	implements Serializable
{

	private static final long serialVersionUID = 1753831854872341841L;

	private int totalRetrievedCount;
	private int totalRelevantCount;
	private int totalCoveredCount;
	private int totalGoldCount;
	private int documentCount;
	private double summedPrecision;
	private double summedRecall;
	private double summedFMeasure;

	public AveragedPerformanceMeasures(
			List<PerformanceMeasures> listOfPerformanceMeasures)
	{
		super();

		for (PerformanceMeasures performanceMeasures : listOfPerformanceMeasures) {
			this.totalRetrievedCount += performanceMeasures.getRetrievedCount();
			this.totalRelevantCount += performanceMeasures.getRelevantCount();
			this.totalCoveredCount += performanceMeasures.getCoveredCount();
			this.totalGoldCount += performanceMeasures.getGoldCount();
			this.summedPrecision += performanceMeasures.getPrecision();
			this.summedRecall += performanceMeasures.getRecall();
			this.summedFMeasure += performanceMeasures.getFMeasure();
		}

		this.documentCount = listOfPerformanceMeasures.size();

	}

	public AveragedPerformanceMeasures(int totalRetrievedCount,
			int totalRelevantCount, int totalCoveredCount, int totalGoldCount,
			int documentCount, double summedPrecision, double summedRecall,
			double summedFMeasure)
	{
		super();
		this.totalRetrievedCount = totalRetrievedCount;
		this.totalRelevantCount = totalRelevantCount;
		this.totalCoveredCount = totalCoveredCount;
		this.totalGoldCount = totalGoldCount;
		this.documentCount = documentCount;
		this.summedPrecision = summedPrecision;
		this.summedRecall = summedRecall;
		this.summedFMeasure = summedFMeasure;
	}

	/**
	 * @return fraction of total retrieved items that match gold items
	 */
	public double getMicroPrecision()
	{
		if (totalRetrievedCount == 0)
			return 1D;
		return totalRelevantCount / (double) totalRetrievedCount;

	}

	public double getMicroRecall()
	{
		if (totalGoldCount == 0)
			return 1D;
		return totalRelevantCount / (double) totalGoldCount;

	}

	/**
	 * @return harmonic mean of micro-precision and micro-recall
	 */
	public double getMicroFMeasure()
	{
		double precision = getMicroPrecision();
		double recall = getMicroRecall();

		if (precision + recall == 0)
			return 0;

		return 2 * precision * recall / (precision + recall);
	}

	/**
	 * @return arithmetic mean of all precision values of the documents in the
	 *         collection
	 */
	public double getMacroPrecision()
	{
		return summedPrecision / documentCount;

	}

	/**
	 * @return arithmetic mean of all recall values of the documents in the
	 *         collection
	 */
	public double getMacroRecall()
	{
		return summedRecall / documentCount;

	}

	/**
	 * @return arithmetic mean of all f-measure values of the documents in the
	 *         collection
	 */
	public double getMacroFMeasure()
	{
		return summedFMeasure / documentCount;
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
		result = prime * result + documentCount;
		long temp;
		temp = Double.doubleToLongBits(summedFMeasure);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(summedPrecision);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(summedRecall);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + totalCoveredCount;
		result = prime * result + totalGoldCount;
		result = prime * result + totalRelevantCount;
		result = prime * result + totalRetrievedCount;
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
		AveragedPerformanceMeasures other = (AveragedPerformanceMeasures) obj;
		if (documentCount != other.documentCount)
			return false;
		if (Double.doubleToLongBits(summedFMeasure) != Double
				.doubleToLongBits(other.summedFMeasure))
			return false;
		if (Double.doubleToLongBits(summedPrecision) != Double
				.doubleToLongBits(other.summedPrecision))
			return false;
		if (Double.doubleToLongBits(summedRecall) != Double
				.doubleToLongBits(other.summedRecall))
			return false;
		if (totalCoveredCount != other.totalCoveredCount)
			return false;
		if (totalGoldCount != other.totalGoldCount)
			return false;
		if (totalRelevantCount != other.totalRelevantCount)
			return false;
		if (totalRetrievedCount != other.totalRetrievedCount)
			return false;
		return true;
	}

	/**
	 * Prints the measures as two triples in the format: <br>
	 * <code>MicP: _.___ MicR: _.___ MicF: _.___ | MacP: _.___ MacR: _.___ MacF: _.___</code>
	 */
		@Override
		public String toString()
		{
		return String
				.format(
						Locale.US,
						"MicP: %.3f\t MicR: %.3f\t MicF: %.3f\t | \tMacP: %.3f\t MacR: %.3f\t MacF: %.3f",
						getMicroPrecision(), getMicroRecall(), getMicroFMeasure(),
						getMacroPrecision(), getMacroRecall(), getMacroFMeasure());
	}

}

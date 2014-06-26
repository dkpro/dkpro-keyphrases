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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.performancemeasures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to averaged performance measures for the collection and to
 * performance results of all its aggregated documents.
 *
 * @author Mateusz Parzonka
 *
 * @see AveragedPerformanceMeasures
 * @see DocumentPerformanceResult
 *
 */
public class CollectionPerformanceResult
	implements Serializable
{

	private static final long serialVersionUID = 1018768931785183926L;

	private final List<DocumentPerformanceResult> documentPerformanceResults;

	/**
	 * @param documentEvaluationResults
	 *          the underlying collection
	 */
	public CollectionPerformanceResult(List<DocumentPerformanceResult> documentEvaluationResults)
	{
		super();
		this.documentPerformanceResults = documentEvaluationResults;
	}

	/**
	 * Returns averaged performance measures for this collection and a given
	 * number of the best ranked retrieved items in each aggregated document.
	 *
	 * @param retrievedCount
	 *          The number of best ranked items taken into account.<br>
	 *          If the number is greater then the total retrieved count, all items
	 *          used.
	 * @return performance measures for this collection
	 */
	public AveragedPerformanceMeasures getAveragedPerformanceMeasures(int retrievedCount)
	{
		return new AveragedPerformanceMeasures(getDocumentPerformanceMeasures(retrievedCount));
	}

	/**
	 * Returns performance measures for this collection and a given percentage of
	 * the best ranked retrieved items in each aggregated document.
	 *
	 * @param retrievedPercentage
	 *          The percentage of best ranked items taken into account represented
	 *          by a double in [0.0, 1.0].
	 * @return averaged performance measures for this collection
	 */
	public AveragedPerformanceMeasures getAveragedPerformanceMeasures(double retrievedPercentage)
	{
		return new AveragedPerformanceMeasures(getDocumentPerformanceMeasures(retrievedPercentage));
	}

	/**
	 * Returns performance measures for this collection and a the number of best
	 * ranked retrieved items set to the number of gold items in each document
	 * (aka R-precision /-recall).
	 *
	 * @return averaged performance measures for this collection
	 */
	public AveragedPerformanceMeasures getAveragedPerformanceMeasures()
	{
		return new AveragedPerformanceMeasures(getDocumentPerformanceMeasures());
	}

	/**
	 * Returns document performance results for each document in the collection.
	 *
	 * @return a list of document evaluation results
	 */
	public List<DocumentPerformanceResult> getDocumentPerformanceResults()
	{
		return documentPerformanceResults;
	}

	/**
	 * Returns performance measures for all documents in this collection and a
	 * given number of the best ranked retrieved items in each document.
	 *
	 * @param retrievedCount
	 *          The number of best ranked items taken into account.<br>
	 *          If the number is greater then the total retrieved count, all items
	 *          are used.
	 * @return performance measures for each document of this collection
	 */
	public List<PerformanceMeasures> getDocumentPerformanceMeasures(int retrievedCount)
	{
		// maps getDocumentMeasures(retrievedCount) over
		// documentPerformanceResults
		List<PerformanceMeasures> listOfPerformanceMeasures = new ArrayList<PerformanceMeasures>();
		for (DocumentPerformanceResult documentPerformanceResult : documentPerformanceResults)
			listOfPerformanceMeasures.add(documentPerformanceResult.getPerformanceMeasures(retrievedCount));
		return listOfPerformanceMeasures;
	}

	/**
	 * Returns performance measures for all documents in this collection and a
	 * given percentage of the best ranked retrieved items in each document.
	 *
	 * @param retrievedPercentage
	 *          The percentage of best ranked items taken into account represented
	 *          by a double in [0.0, 1.0].
	 * @return performance measures for each document of this collection
	 */
	public List<PerformanceMeasures> getDocumentPerformanceMeasures(double retrievedPercentage)
	{
		// maps getDocumentMeasures(retrievedPercentage) over
		// documentPerformanceResults
		List<PerformanceMeasures> listOfPerformanceMeasures = new ArrayList<PerformanceMeasures>();
		for (DocumentPerformanceResult documentPerformanceResult : documentPerformanceResults)
			listOfPerformanceMeasures.add(documentPerformanceResult.getPerformanceMeasures(retrievedPercentage));
		return listOfPerformanceMeasures;
	}

	/**
	 * Returns performance measures for all documents in this collection with the
	 * number of best ranked items set to the number of gold items (aka
	 * R-precision /-recall).
	 *
	 * @return performance measures for each document of this collection
	 */
	public List<PerformanceMeasures> getDocumentPerformanceMeasures()
	{
		// maps getDocumentMeasures(retrievedCount) over
		// documentPerformanceResults
		List<PerformanceMeasures> listOfPerformanceMeasures = new ArrayList<PerformanceMeasures>();
		for (DocumentPerformanceResult documentPerformanceResult : documentPerformanceResults)
			listOfPerformanceMeasures.add(documentPerformanceResult.getPerformanceMeasures(documentPerformanceResult
					.getGoldCount()));
		return listOfPerformanceMeasures;
	}

	/**
	 * @return count of all documents in the collection
	 */
	public int getDocumentCount()
	{
		return documentPerformanceResults.size();
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
		result = prime * result + ((documentPerformanceResults == null) ? 0 : documentPerformanceResults.hashCode());
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
		CollectionPerformanceResult other = (CollectionPerformanceResult) obj;
		if (documentPerformanceResults == null) {
			if (other.documentPerformanceResults != null)
				return false;
		}
		else if (!documentPerformanceResults.equals(other.documentPerformanceResults))
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
		return "CollectionPerformanceResult [documentPerformanceResults=" + documentPerformanceResults + "]";
	}

}

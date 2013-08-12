package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.pipeline;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.FlowControllerFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.flow.impl.FixedFlowController;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.segmentation.OneSegmentAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.Segment;

/**
 * This class offers static methods that consume documents containing
 * {@link Segment} annotations, processing each segment as it were a individual
 * CAS.
 *
 * @author Mateusz Parzonka
 *
 */
public final class SegmentProcessingPipeline
{

	private SegmentProcessingPipeline()
	{
		// This class is not meant to be instantiated
	}

	/**
	 * This method runs a pipeline with three sequential processing steps
	 * (aggregates) specified by {@link AnalysisEngineDescriptions}s.
	 * {@link Segment}s contained in the CAS after the first step are processed as
	 * individual CASes (second step), after which they are further processed in
	 * the third step.
	 *
	 * @param reader
	 *          reads documents from a collection.
	 * @param preSegmentProcessing
	 *          Processes the CAS and ensures {@link Segment} -annotations are
	 *          added to the CAS.
	 * @param segmentProcessing
	 *          Processes Every {@link Segment} as a individual (sub)CAS.
	 * @param postSegmentProcessing
	 *          Processes the CAS further after the subCASes had been
	 *          demultiplied.
	 * @throws UIMAException
	 * @throws IOException
	 */
	public static void runPipeline(CollectionReader reader,
			AnalysisEngineDescription preSegmentProcessing,
			AnalysisEngineDescription segmentProcessing,
			AnalysisEngineDescription postSegmentProcessing)
		throws UIMAException, IOException
	{

		AnalysisEngineDescription multiSegmentPipeline = createSegmentProcessingAggregate(
				preSegmentProcessing, segmentProcessing, postSegmentProcessing);

		SimplePipeline.runPipeline(reader, multiSegmentPipeline);
	}

	/**
	 * This method runs a pipeline with three sequential processing steps
	 * (aggregates) specified by {@link AggregateBuilder}s. {@link Segment}s
	 * contained in the CAS after the first step are processed as individual CASes
	 * (second step), after which they are further processed in the third step.
	 *
	 * @param reader
	 *          reads documents from a collection.
	 * @param preSegmentProcessing
	 *          Processes the CAS and ensures {@link Segment} -annotations are
	 *          added to the CAS.
	 * @param segmentProcessing
	 *          Processes Every {@link Segment} as a individual (sub)CAS.
	 * @param postSegmentProcessing
	 *          Processes the CAS further after the subCASes had been
	 *          demultiplied.
	 * @throws UIMAException
	 * @throws IOException
	 */
	public static void runPipeline(CollectionReader reader,
			AggregateBuilder preSegmentProcessing,
			AggregateBuilder segmentProcessing, AggregateBuilder postSegmentProcessing)
		throws UIMAException, IOException
	{

		// create AggregateDescriptions from every builder
		AnalysisEngineDescription multiSegmentAggregate = createSegmentProcessingAggregate(
				preSegmentProcessing.createAggregateDescription(), segmentProcessing
						.createAggregateDescription(), postSegmentProcessing
						.createAggregateDescription());

		SimplePipeline.runPipeline(reader, multiSegmentAggregate);
	}

	/**
	 * Returns a segment processing aggregate which processes each segment
	 * contained in the CAS as it would be a individual CAS itself.
	 *
	 * How it works: For every {@link Segment}-annotation in the JCas after the
	 * pre-processing step a single JCas will be created containing only
	 * annotations that are covered by the segment. This subCAS will be processed
	 * in the second step and be de-multiplied afterwards. The resulting CAS can
	 * processed by filters, writers and evaluators in the post-segment-processing
	 * step.
	 *
	 * @param preSegmentProcessing
	 * @param segmentProcessing
	 * @param postSegmentProcessing
	 * @return
	 * @throws ResourceInitializationException
	 */
	public static AnalysisEngineDescription createSegmentProcessingAggregate(
			AnalysisEngineDescription preSegmentProcessing,
			AnalysisEngineDescription segmentProcessing,
			AnalysisEngineDescription postSegmentProcessing)
		throws ResourceInitializationException
	{

		AnalysisEngineDescription multiSegmentAggregate = AnalysisEngineFactory
				.createAggregateDescription(

				// add the flow controller description
						FlowControllerFactory.createFlowControllerDescription(
								FixedFlowController.class,
								// Determines what should happen to a
								// CAS after it has been input to a CAS
								// Multiplier: The CAS will no longer
								// continue in the flow, and will be
								// dropped (not returned from the
								// aggregate) if possible.
								FixedFlowController.PARAM_ACTION_AFTER_CAS_MULTIPLIER, "drop"),

						// add the pipeline step descriptions
						preSegmentProcessing,

//						// multiply: segments become subCASes
//						createPrimitiveDescription(SegmentMultiplier.class,
//								SegmentMultiplier.PARAM_ANNOTATION_CLASS, Segment.class
//										.getName()),

						// process all subCASes individually
						segmentProcessing,

						// segment annotations get lost by multiplication. This
						// analysis engine restores the original segment
						// annotations by adding it to each individual subCAS.
						createPrimitiveDescription(OneSegmentAnnotator.class),

						// de-multiply subCASes to a single CAS
//						createPrimitiveDescription(SegmentDeMultiplier.class),

						// continue processing with de-multiplied CAS
						postSegmentProcessing);

		// is this really needed?
		multiSegmentAggregate.getAnalysisEngineMetaData()
				.getOperationalProperties().setOutputsNewCASes(true);

		return multiSegmentAggregate;
	}

}

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

import java.io.File;
import java.io.IOException;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.api.io.ResourceCollectionReaderBase;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeIdf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeTf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfConsumer;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking.TfidfAggregate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.lab.Lab;
import de.tudarmstadt.ukp.dkpro.lab.engine.TaskContext;
import de.tudarmstadt.ukp.dkpro.lab.task.Task;
import de.tudarmstadt.ukp.dkpro.lab.uima.task.impl.UimaTaskBase;

/**
 * A keyphrase extractor whose ranking is based on the tf.idf values of the keyphrase terms.
 * This is not fully unsupervised, as some reference collection is needed,
 * on which the tf.idf values are based
 *
 * @author zesch
 *
 */
public class TfidfExtractor extends KeyphraseExtractor_ImplBase {

    /**
     * The way tf.idf values of multi-word components are aggregated, if there is no tf.idf annotation for the whole multi-word.
     */
    private TfidfAggregate aggregateFunction = TfidfAggregate.max;

    /**
     * The weighting mode for the term frequency (tf).
     */
    private WeightingModeTf tfWeightingMode = WeightingModeTf.NORMAL;

    /**
     * The weighting mode for the inverse document frequency (idf).
     */
    private WeightingModeIdf idfWeightingMode = WeightingModeIdf.LOG;

    /**
     * File with the tf.idf model.
     */
    private File tfidfModelFile;

    /**
     * If all terms should be converted to lowercase during the whole process.
     */
    private boolean convertToLowercase = false;

    @Override
    protected AnalysisEngineDescription createKeyphraseExtractorAggregate() throws ResourceInitializationException {

        return createEngineDescription(
                createPreprocessingComponents(getCandidate().getType()),
                createEngineDescription(
                        TfidfAnnotator.class,
                        TfidfAnnotator.PARAM_FEATURE_PATH, Keyphrase.class.getName(),
                        TfidfAnnotator.PARAM_TFDF_PATH, getTfidfModelFile().getAbsolutePath(),
                        TfidfAnnotator.PARAM_LOWERCASE, isConvertToLowercase(),
                        TfidfAnnotator.PARAM_TF_MODE, getTfWeightingMode().name(),
                        TfidfAnnotator.PARAM_IDF_MODE, getIdfWeightingMode().name()),
                createEngineDescription(
                        TfidfRanking.class,
                        TfidfRanking.PARAM_AGGREGATE, TfidfRanking.TfidfAggregate.max.name()),
                createPostprocessingComponents()
          );
    }

    /**
     * Builds a tf.idf model from the input files found in inputDir.
     * It writes the model to a temporary file and sets the corresponding field in the TfidfExtractor.
     *
     * @param inputDir The input directory with the input files.
     * @param suffix The suffix of the valid input files.
     * @throws IOException io exception
     * @throws ResourceInitializationException resource initialization exception
     */
    public void buildTfidfModel(File inputDir, String suffix) throws ResourceInitializationException {
        try {
            File tmpFile = File.createTempFile("tfidf_model_ukp", "tmp");
            buildTfidfModel(inputDir, suffix, tmpFile);
        }
        catch (Exception e) {
            throw new ResourceInitializationException(e);
        }
    }

    /**
     * Builds a tf.idf model from the input files found in inputDir.
     * It writes the model to a temporary file and sets the corresponding field in the TfidfExtractor.
     *
     * @param inputDir The input directory with the input files.
     * @param suffix The suffix of the valid input files.
     * @param outputFile The file to which the built model should be written.
     * @throws Exception exception
     */
    public void buildTfidfModel(final File inputDir, final String suffix, File outputFile) throws Exception {

        // set the output file first
        this.setTfidfModelFile(outputFile);

        Task task = new UimaTaskBase()
        {
            @Override
            public CollectionReaderDescription getCollectionReaderDescription(TaskContext arg0)
                throws ResourceInitializationException, IOException
            {
                return createReader(
                        TextReader.class,
                        TextReader.PARAM_SOURCE_LOCATION, inputDir.getAbsolutePath(),
                        TextReader.PARAM_PATTERNS, new String[] {
                                ResourceCollectionReaderBase.INCLUDE_PREFIX + "*." + suffix
                        }
                );
            }

            @Override
            public AnalysisEngineDescription getAnalysisEngineDescription(TaskContext arg0)
                throws ResourceInitializationException, IOException
            {
                return createEngineDescription(
                        createPreprocessingComponents(getCandidate().getType()),

                        createEngineDescription(
                                TfidfConsumer.class,
                                TfidfConsumer.PARAM_FEATURE_PATH, Keyphrase.class.getName(),
                                TfidfConsumer.PARAM_LOWERCASE, isConvertToLowercase(),
                                TfidfConsumer.PARAM_TARGET_LOCATION, getTfidfModelFile().getAbsolutePath()
                        )
                );
            }
        };

        Lab.getInstance().run(task);
    }

    public void setAggregateFunction(TfidfAggregate aggregateFunction)
    {
        this.aggregateFunction = aggregateFunction;
    }

    public TfidfAggregate getAggregateFunction()
    {
        return aggregateFunction;
    }

    public void setConvertToLowercase(boolean convertToLowercase)
    {
        this.convertToLowercase = convertToLowercase;
    }

    public boolean isConvertToLowercase()
    {
        return convertToLowercase;
    }

    public void setTfWeightingMode(WeightingModeTf tfWeightingMode)
    {
        this.tfWeightingMode = tfWeightingMode;
    }

    public WeightingModeTf getTfWeightingMode()
    {
        return tfWeightingMode;
    }

    public void setIdfWeightingMode(WeightingModeIdf idfWeightingMode)
    {
        this.idfWeightingMode = idfWeightingMode;
    }

    public WeightingModeIdf getIdfWeightingMode()
    {
        return idfWeightingMode;
    }

    public void setTfidfModelFile(File tfidfModelFile)
    {
        this.tfidfModelFile = tfidfModelFile;
    }

    public File getTfidfModelFile()
    {
        return tfidfModelFile;
    }
}
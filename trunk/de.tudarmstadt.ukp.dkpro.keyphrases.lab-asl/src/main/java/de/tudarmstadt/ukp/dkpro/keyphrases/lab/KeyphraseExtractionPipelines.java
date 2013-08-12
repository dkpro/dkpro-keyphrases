package de.tudarmstadt.ukp.dkpro.keyphrases.lab;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Compound.CompoundSplitLevel;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.chunk.Chunk;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeIdf;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator.WeightingModeTf;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator.EvaluatorType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.KeyphraseEvaluator.MatchingType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.PositionRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfBackgroundIdfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfidfRanking.TfidfAggregate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;
import de.tudarmstadt.ukp.dkpro.keyphrases.lab.tasks.CandidateSelectionTask;
import de.tudarmstadt.ukp.dkpro.keyphrases.lab.tasks.EvaluationTask;
import de.tudarmstadt.ukp.dkpro.keyphrases.lab.tasks.KeyphraseRankingTask;
import de.tudarmstadt.ukp.dkpro.keyphrases.lab.tasks.PreprocessingTask;
import de.tudarmstadt.ukp.dkpro.keyphrases.ranking.PageRankRanking;
import de.tudarmstadt.ukp.dkpro.lab.Lab;
import de.tudarmstadt.ukp.dkpro.lab.task.Constraint;
import de.tudarmstadt.ukp.dkpro.lab.task.Dimension;
import de.tudarmstadt.ukp.dkpro.lab.task.ParameterSpace;
import de.tudarmstadt.ukp.dkpro.lab.task.Task;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask;
import de.tudarmstadt.ukp.dkpro.lab.task.impl.BatchTask.ExecutionPolicy;

public class KeyphraseExtractionPipelines
{

	public static void main(String[] args)
			throws Exception
			{
		
		//Get DKPRO_HOME
		String dkproHome = System.getenv("DKPRO_HOME");
		if(dkproHome==null){
			String msg = "DKPRO_HOME system environment variable is not set.";
			throw new IOException(msg);
		}

		//Get path to storage location of index file in DKPRO_HOME
		File dkproHomeDir =new File(dkproHome);
		if(!dkproHomeDir.exists()){
			String msg = "DKPRO_HOME directory ( "+dkproHomeDir.getPath()+" ) does not exist.";
			throw new IOException(msg);
		}



		final String language = "de"; 
		@SuppressWarnings("unchecked")
		ParameterSpace params = new ParameterSpace(

				//Input format
				Dimension.create("datasetPath", new File(dkproHome, "corpora/PEDOCS_cleansets/dev_small/").getAbsolutePath()),
				Dimension.create("language", language),
				Dimension.create("includePrefix", "*.txt"),
				Dimension.create("goldSuffix", ".keys.lemmatized"),

				// Global
				Dimension.createBundle("df",
						new Object[] {
						"tfidfFeaturePath",  Lemma.class.getName() + "/value",
						"dfModelFile",  "target/df/lemma.ser"}),

				// Candidate selection
				Dimension.create("candidateFeaturePath",  Token.class.getName(), Lemma.class.getName() + "/value"),
//						Dimension.create("candidateFeaturePath", Lemma.class.getName() + "/value",
//								Token.class.getName(), Chunk.class.getName(), NamedEntity.class.getName()),
				Dimension.create("shouldResolveOverlaps", false),
				Dimension.create("keyphraseMergerMaxTokens", 3),
				Dimension.create("structureFilterMinTokens", 1),
				Dimension.create("structureFilterMaxTokens", 3),
				Dimension.create("structureFilterPosPatterns", false),
				
				//JWeb1T
				Dimension.create("nGramFolder",new File(dkproHome, "corpora/web1t/" + language).getAbsolutePath()),
				
				//Other preprocessing
				Dimension.create("segmenterClass",
						StanfordSegmenter.class,
						BreakIteratorSegmenter.class),
						
						Dimension.create("usePosFilter",
								true, false),

				
				//Decompounding
				Dimension.create("compoundSplitLevel", CompoundSplitLevel.NONE, CompoundSplitLevel.ALL, CompoundSplitLevel.HIGHEST, CompoundSplitLevel.LOWEST),

				//Cooccurrence graph
				Dimension.create("cooccurrenceGraphFeaturePath", Keyphrase.class.getName()),
				Dimension.create("cooccurrenceGraphWindowSize", 2),

				// Ranking
				Dimension.create("rankerClass",
						PositionRanking.class,
						TfRanking.class,
						TfidfRanking.class,
						PageRankRanking.class,
						TfBackgroundIdfRanking.class),
				Dimension.create("shouldLowercaseCandidates", true),
				Dimension.create("shouldLowercaseExtractedKeyphrases", true),
				Dimension.create("shouldRemoveContainedKeyphrasesInExtractedKeyphrases", false),
				Dimension.create("weightingModeTf", WeightingModeTf.NORMAL),
				Dimension.create("weightingModeIdf", 
						WeightingModeIdf.NORMAL,
						WeightingModeIdf.CONSTANT_ONE,
						WeightingModeIdf.LOG),
				Dimension.create("tfidfAggregate", TfidfAggregate.max),

				// Evaluation
				Dimension.create("evaluationMatchingType", MatchingType.Exact),
				Dimension.create("evaluationN", 20),
				Dimension.create("evaluatorType", EvaluatorType.Lemma),
				Dimension.create("evaluationRemoveGoldAfterMatch", true),
				Dimension.create("removeKeyphrasesNotInText", true, false)
		);

		params.addConstraint(new Constraint()
		{
			@Override
			public boolean isValid(Map<String, Object> parameter)
			{
				if (parameter.get("rankerClass") != TfidfRanking.class) {
					return parameter.get("tfidfAggregate") == TfidfAggregate.max
							&& parameter.get("weightingModeTf") == WeightingModeTf.NORMAL
							&& parameter.get("weightingModeIdf") == WeightingModeIdf.NORMAL;
				}
				return true;
			}
		});

		Task preprocessingTask = new PreprocessingTask();

		Task candidateSelectionTask = new CandidateSelectionTask();
		candidateSelectionTask.addImport(preprocessingTask,
				PreprocessingTask.KEY_OUTPUT_XMI, CandidateSelectionTask.KEY_INPUT_XMI);

		Task keyphraseRankingTask = new KeyphraseRankingTask();
		keyphraseRankingTask.addImport(candidateSelectionTask,
				CandidateSelectionTask.KEY_OUTPUT_XMI, KeyphraseRankingTask.KEY_INPUT_XMI);

		Task evaluationTask = new EvaluationTask();
		evaluationTask.addImport(keyphraseRankingTask,
				KeyphraseRankingTask.KEY_OUTPUT_XMI, EvaluationTask.KEY_INPUT_XMI);

		BatchTask batch = new BatchTask();
		batch.setExecutionPolicy(ExecutionPolicy.USE_EXISTING);
		batch.setParameterSpace(params);

		batch.addTask(preprocessingTask);
		batch.addTask(candidateSelectionTask);
		batch.addTask(keyphraseRankingTask);
		batch.addTask(evaluationTask);
		batch.addReport(KeyphraseExtractionReport.class);

		Lab.getInstance().run(batch);
			}

}

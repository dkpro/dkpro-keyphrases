package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReaderDescription;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.N;
import de.tudarmstadt.ukp.dkpro.core.io.text.TextReader;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.tokit.BreakIteratorSegmenter;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.candidate.CandidateAnnotator;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking.TfRanking;

public class KeyphraseWriterTest
{
    
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    public void process() throws UIMAException, IOException
    {
        final CollectionReader reader = createReader(
                createReaderDescription(TextReader.class,
                TextReader.PARAM_SOURCE_LOCATION, "src/test/resources/keyphrase/evaluator",
                TextReader.PARAM_PATTERNS, TextReader.INCLUDE_PREFIX + "*.txt",
                TextReader.PARAM_LANGUAGE, "en"));
        final AnalysisEngine analysisEngine = createEngine(createEngineDescription(
                createEngineDescription(BreakIteratorSegmenter.class),
                AnalysisEngineFactory.createEngineDescription(StanfordPosTagger.class),
                AnalysisEngineFactory.createEngineDescription(StanfordLemmatizer.class),
                createEngineDescription(CandidateAnnotator.class,
                        CandidateAnnotator.PARAM_FEATURE_PATH, N.class.getName(),
                        CandidateAnnotator.PARAM_RESOLVE_OVERLAPS, false),
                createEngineDescription(TfRanking.class),
                createEngineDescription(KeyphraseWriter.class,
                        KeyphraseWriter.PARAM_SHOULD_WRITE_DOCUMENT, false,
                        KeyphraseWriter.PARAM_WRITE_TO_FILE, true,
                        KeyphraseWriter.PARAM_FILE_NAME, "src/test/resources/keyphrase/evaluator/evaluation.txt")));

        final StringBuilder actual = new StringBuilder();
        final StringBuilderOutputStream fout = new StringBuilderOutputStream(actual);

        final MultiOutputStream multiOut = new MultiOutputStream(System.out, fout);

        final PrintStream stdout = new PrintStream(multiOut);

        System.setOut(stdout);
        SimplePipeline.runPipeline(reader, analysisEngine);
        final String[] lines = actual.toString().split(LINE_SEPARATOR);
        actual.delete(0, actual.length());
        for (int i = 1; i < lines.length; ++i) {
            actual.append(lines[i]);
        }
        final String expected = "text 1.0";

        Assert.assertThat(actual.toString(), CoreMatchers.is(expected));

    }

}

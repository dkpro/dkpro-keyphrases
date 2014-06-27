package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RprecisionKeyphraseCounterTest
{
    
    private final RprecisionKeyphraseCounter perfCounter = new RprecisionKeyphraseCounter();

    private static final String FILE1_NAME = "file1";
    private static final String FILE2_NAME = "file2";
    
    @Before
    public final void setUp()
        throws AnalysisEngineProcessException
    {
        final Set<String> files = new HashSet<String>();
        files.add(FILE1_NAME);
        files.add(FILE2_NAME);
        perfCounter.registerFile(FILE1_NAME, 5);
        perfCounter.registerFile(FILE2_NAME, 3);
        Assert.assertThat(perfCounter.getRegisteredFiles(), CoreMatchers.is(files));
        perfCounter.setFileTPcount(FILE1_NAME, 1, 1);
        perfCounter.setFileTPcount(FILE1_NAME, 2, 1);
        perfCounter.setFileTPcount(FILE1_NAME, 3, 2);
        perfCounter.setFileTPcount(FILE1_NAME, 4, 2);
        perfCounter.setFileTPcount(FILE1_NAME, 5, 3);

        perfCounter.setFileFPcount(FILE1_NAME, 1, 0);
        perfCounter.setFileFPcount(FILE1_NAME, 2, 1);
        perfCounter.setFileFPcount(FILE1_NAME, 3, 1);
        perfCounter.setFileFPcount(FILE1_NAME, 4, 2);
        perfCounter.setFileFPcount(FILE1_NAME, 5, 2);

        perfCounter.setFileFNcount(FILE1_NAME, 1, 0);
        perfCounter.setFileFNcount(FILE1_NAME, 2, 4);
        perfCounter.setFileFNcount(FILE1_NAME, 3, 3);
        perfCounter.setFileFNcount(FILE1_NAME, 4, 3);
        perfCounter.setFileFNcount(FILE1_NAME, 5, 2);

        perfCounter.setRPrecision(FILE1_NAME, 0.6);

        perfCounter.setFileTPcount(FILE2_NAME, 1, 1);
        perfCounter.setFileTPcount(FILE2_NAME, 2, 1);
        perfCounter.setFileTPcount(FILE2_NAME, 3, 2);

        perfCounter.setFileFPcount(FILE2_NAME, 1, 0);
        perfCounter.setFileFPcount(FILE2_NAME, 2, 1);
        perfCounter.setFileFPcount(FILE2_NAME, 3, 1);

        perfCounter.setFileFNcount(FILE2_NAME, 1, 0);
        perfCounter.setFileFNcount(FILE2_NAME, 2, 2);
        perfCounter.setFileFNcount(FILE2_NAME, 3, 1);

        perfCounter.setRPrecision(FILE2_NAME, 2.0 / 3.0);
    }

    
    @Test
    public void test()
    {

        Assert.assertThat(perfCounter.getAverageRPrecision(),
                CoreMatchers.is(((2.0 / 3.0) + 0.6) / 2));
        
    }

}

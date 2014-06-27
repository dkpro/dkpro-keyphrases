package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PrecisionRecallFmeasureKeyphraseCounterTest
{

    private final PrecisionRecallFmeasureKeyphraseCounter perfCounter = new PrecisionRecallFmeasureKeyphraseCounter();
    private static final String LF = System.getProperty("line.separator");

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

        perfCounter.setFileTPcount(FILE2_NAME, 1, 1);
        perfCounter.setFileTPcount(FILE2_NAME, 2, 1);
        perfCounter.setFileTPcount(FILE2_NAME, 3, 2);

        perfCounter.setFileFPcount(FILE2_NAME, 1, 0);
        perfCounter.setFileFPcount(FILE2_NAME, 2, 1);
        perfCounter.setFileFPcount(FILE2_NAME, 3, 1);

        perfCounter.setFileFNcount(FILE2_NAME, 1, 0);
        perfCounter.setFileFNcount(FILE2_NAME, 2, 2);
        perfCounter.setFileFNcount(FILE2_NAME, 3, 1);

    }

    @Test
    public void test()
    {
        Assert.assertThat(perfCounter.getMicroPrecision(1), CoreMatchers.is(1.0));
        Assert.assertThat(perfCounter.getMicroPrecision(2), CoreMatchers.is(0.5));
        Assert.assertThat(perfCounter.getMicroPrecision(3), CoreMatchers.is(2.0 / 3.0));
        Assert.assertThat(perfCounter.getMicroPrecision(4), CoreMatchers.is(4.0 / 7.0));
        Assert.assertThat(perfCounter.getMicroPrecision(5), CoreMatchers.is(0.625));

        Assert.assertThat(perfCounter.getMicroRecall(1), CoreMatchers.is(1.0));
        Assert.assertThat(perfCounter.getMicroRecall(2), CoreMatchers.is(0.25));
        Assert.assertThat(perfCounter.getMicroRecall(3), CoreMatchers.is(0.5));
        Assert.assertThat(perfCounter.getMicroRecall(4), CoreMatchers.is(0.5));
        Assert.assertThat(perfCounter.getMicroRecall(5), CoreMatchers.is(0.625));

        Assert.assertThat(perfCounter.getMicroFMeasure(1), CoreMatchers.is(1.0));
        Assert.assertThat(perfCounter.getMicroFMeasure(2), CoreMatchers.is(1.0 / 3.0));
        Assert.assertThat(perfCounter.getMicroFMeasure(3),
                CoreMatchers.is((2.0 / 3.0) / (0.5 + 2.0 / 3.0)));
        Assert.assertThat(perfCounter.getMicroFMeasure(4),
                CoreMatchers.is((4.0 / 7.0) / (0.5 + 4.0 / 7.0)));
        Assert.assertThat(perfCounter.getMicroFMeasure(5), CoreMatchers.is(0.625));

        Assert.assertThat(perfCounter.getMacroPrecision(1), CoreMatchers.is(1.0));
        Assert.assertThat(perfCounter.getMacroPrecision(2), CoreMatchers.is(0.5));
        Assert.assertThat(perfCounter.getMacroPrecision(3), CoreMatchers.is(2.0 / 3.0));
        Assert.assertThat(perfCounter.getMacroPrecision(4),
                CoreMatchers.is(((2.0 / 3.0) + 0.5) / 2));
        Assert.assertThat(perfCounter.getMacroPrecision(5),
                CoreMatchers.is(((2.0 / 3.0) + 0.6) / 2));

        Assert.assertThat(perfCounter.getMacroRecall(1), CoreMatchers.is(1.0));
        Assert.assertThat(perfCounter.getMacroRecall(2), CoreMatchers.is(((1.0 / 3.0) + 0.2) / 2));
        Assert.assertThat(perfCounter.getMacroRecall(3), CoreMatchers.is(((2.0 / 3.0) + 0.4) / 2));
        Assert.assertThat(perfCounter.getMacroRecall(4), CoreMatchers.is(((2.0 / 3.0) + 0.4) / 2));
        Assert.assertThat(perfCounter.getMacroRecall(5), CoreMatchers.is(((2.0 / 3.0) + 0.6) / 2));

        Assert.assertThat(perfCounter.getMacroFMeasure(1), CoreMatchers.is(1.0));

        final DecimalFormat decimalFormat = new DecimalFormat("0.000");

        final String microPerfOverview = "Micro Performance Overview" + LF + "n\tP\tR\tF\t" + LF
                + "1\t" + decimalFormat.format(1.0) + "\t" + decimalFormat.format(1.0) + "\t"
                + decimalFormat.format(1.0) + "\t" + LF;

        Assert.assertThat(perfCounter.getMicroPerformanceOverview(1),
                CoreMatchers.is(microPerfOverview));

        final String macroPerfOverview = "Macro Performance Overview" + LF + "n\tP\tR\tF\t" + LF
                + "1\t" + decimalFormat.format(1.0) + "\t" + decimalFormat.format(1.0) + "\t"
                + decimalFormat.format(1.0) + "\t" + LF;

        Assert.assertThat(perfCounter.getMacroPerformanceOverview(1),
                CoreMatchers.is(macroPerfOverview));

        final String macroPrfOverview = "Macro P/R/F Overview"+LF+"k\tP\tR\tF\t"+LF+"5\t"
                + decimalFormat.format(0.633) + "\t" + decimalFormat.format(0.633) + "\t"
                + decimalFormat.format(0.633) + "\t"+LF+"10\t" + decimalFormat.format(0.633) + "\t"
                + decimalFormat.format(0.633) + "\t" + decimalFormat.format(0.633) + "\t"+LF+"15\t"
                + decimalFormat.format(0.633) + "\t" + decimalFormat.format(0.633) + "\t"
                + decimalFormat.format(0.633) + "\t"+LF+LF;

        Assert.assertThat(perfCounter.getMacroPrfOverview(), CoreMatchers.is(macroPrfOverview));

    }

}

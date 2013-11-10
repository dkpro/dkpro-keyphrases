package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MeanAveragePrecisionKeyphraseCounter
    extends AbstractKeyphraseCounter
{

    private static final int NBR_FLOATING_POINTS = 10;

    public Double getMeanAveragePrecision()
    {
        BigDecimal meanAveragePrecision = new BigDecimal("0.0");
        for (FilePerformance filePerformance : fileName2performanceMap.values()) {
            meanAveragePrecision = meanAveragePrecision.add(filePerformance
                    .calculateAveragePrecision());
        }
        meanAveragePrecision = meanAveragePrecision.divide(
                new BigDecimal(String.valueOf(fileName2performanceMap.size())),
                NBR_FLOATING_POINTS, RoundingMode.UP);
        return meanAveragePrecision.doubleValue();
    }

}

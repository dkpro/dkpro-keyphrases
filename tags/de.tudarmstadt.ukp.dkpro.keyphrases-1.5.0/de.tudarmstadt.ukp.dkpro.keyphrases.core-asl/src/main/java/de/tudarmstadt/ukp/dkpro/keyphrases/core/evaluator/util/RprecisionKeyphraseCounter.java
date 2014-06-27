package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.util.ArrayList;
import java.util.List;

public class RprecisionKeyphraseCounter
    extends AbstractKeyphraseCounter
{

    public void setRPrecision(String fileName, double rprecision)
    {

        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setRPrecision(rprecision);

    }

    public double getAverageRPrecision()
    {
        double rPrecisionSum = 0.0;
        List<String> files = new ArrayList<String>(getRegisteredFiles());
        for (String fileName : files) {
            rPrecisionSum += this.getFileRPrecision(fileName);
        }
        return rPrecisionSum / files.size();
    }

    /**
     * R-Precision
     * 
     * @param fileName The file name
     * @return The r-precision for that file.
     */
    public double getFileRPrecision(String fileName)
    {
        return fileName2performanceMap.get(fileName).getRPrecision();
    }

}

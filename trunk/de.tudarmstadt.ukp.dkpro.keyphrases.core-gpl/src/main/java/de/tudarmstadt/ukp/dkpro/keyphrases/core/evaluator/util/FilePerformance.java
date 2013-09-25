package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class FilePerformance
{
    // holds the tp, fp, and fn counts for each threshold n
    Map<Integer, Integer> TPcounter;
    Map<Integer, Integer> FPcounter;
    Map<Integer, Integer> FNcounter;
    Map<Integer, Matchings> matchings;
    double rPrecision;
    BigDecimal averagePrecision;
    BigDecimal[] precWith11Points = new BigDecimal[11];

    int nrOfKeyphrasesRetrieved;

    public FilePerformance(int nrOfKeyphrasesRetrieved)
    {
        super();
        this.TPcounter = new HashMap<Integer, Integer>();
        this.FPcounter = new HashMap<Integer, Integer>();
        this.FNcounter = new HashMap<Integer, Integer>();
        this.nrOfKeyphrasesRetrieved = nrOfKeyphrasesRetrieved;
    }

    public void getTPcounter(int i)
    {
        TPcounter.get(i);
    }

    public void getFPcounter(int i)
    {
        FPcounter.get(i);
    }

    public void getFNcounter(int i)
    {
        FNcounter.get(i);
    }

    public int getNrOfKeyphrasesRetrieved()
    {
        return this.nrOfKeyphrasesRetrieved;
    }

    public Matchings getMatchings(int i)
    {
        return this.matchings.get(i);
    }

    public void setTPcounter(int i, int value)
    {
        TPcounter.put(i, value);
    }

    public void setFPcounter(int i, int value)
    {
        FPcounter.put(i, value);
    }

    public void setFNcounter(int i, int value)
    {
        FNcounter.put(i, value);
    }

    public void setMatching(int i, Matchings matchings)
    {
        this.matchings.put(i, matchings);
    }

    public double getRPrecision()
    {
        return rPrecision;
    }

    public void setRPrecision(double precision)
    {
        rPrecision = precision;
    }

    /**
     *
     * This method calculates the average precision for the keyphrases assigned to this file.
     *
     * @return A BigDecimal with the respective average precision
     *
     * */
    public final BigDecimal calculateAveragePrecision()
    {
        BigDecimal[] precisions = new BigDecimal[nrOfKeyphrasesRetrieved];
        for (int i = 1; i <= nrOfKeyphrasesRetrieved; i++) {
            BigDecimal numerator = new BigDecimal(String.valueOf(TPcounter.get(i)));
            BigDecimal denominator = new BigDecimal(
                    String.valueOf(TPcounter.get(i) + FPcounter.get(i)));
            if(denominator != null && denominator.floatValue() > 0){
                precisions[i - 1] = numerator.divide(denominator, 10, RoundingMode.UP);
            }
            else{
                precisions[i - 1] = new BigDecimal("0");
            }
        }
        for (int i = 0; i < nrOfKeyphrasesRetrieved; ++i) {
            final BigDecimal currentValue = precisions[i];
            for (int j = i + 1; j < nrOfKeyphrasesRetrieved; ++j) {
                if (precisions[j].compareTo(currentValue) > 0) {
                    for (int k = i; k < j; ++k) {
                        precisions[k] = precisions[j];
                    }
                    break;
                }
            }
        }
        int i, j;
        for (i = 1, j = 0; i <= nrOfKeyphrasesRetrieved;) {
            BigDecimal standardRecallPoint = new BigDecimal(String.valueOf(j)).divide(
                    BigDecimal.TEN,10,RoundingMode.UP);
            BigDecimal normalRecallPoint = new BigDecimal(String.valueOf(i)).divide(new
                    BigDecimal(String.valueOf(nrOfKeyphrasesRetrieved)),
                    KeyphrasePerformanceCounter.NBR_FLOATING_POINTS,RoundingMode.UP);
            if ( standardRecallPoint.compareTo(normalRecallPoint) <= 0) {
                precWith11Points[j] = precisions[i-1];
                ++j;
            }
            else {
                ++i;
            }
        }
        averagePrecision = BigDecimal.ZERO;
        for (BigDecimal precision : precWith11Points) {
            averagePrecision = averagePrecision.add(precision);
        }

        averagePrecision = averagePrecision.divide(new BigDecimal("11.0"),
                KeyphrasePerformanceCounter.NBR_FLOATING_POINTS, RoundingMode.UP);

        return averagePrecision;

    }

    public int getMaxFileFNcount()
    {
        int maxN = 0;
        for(int n : FNcounter.keySet()){
            maxN = maxN<n ? n : maxN;
        }
        return FNcounter.get(maxN);
    }

    public int getMaxFileTPcount()
    {
        int maxN = 0;
        for(int n : TPcounter.keySet()){
            maxN = maxN<n ? n : maxN;
        }
        return TPcounter.get(maxN);
    }

    public double getMaxFileRecall()
    {
        int tp = getMaxFileTPcount();
        int fn = getMaxFileFNcount();

        if (tp + fn > 0) {
            return (double) tp / (tp + fn);
        }
        else {
            return 0.0;
        }
    }
}

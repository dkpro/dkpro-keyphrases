package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PrecisionRecallFmeasureKeyphraseCounter
    extends AbstractKeyphraseCounter
{

    private static final String LF = System.getProperty("line.separator");
    private final Log log = LogFactory.getLog(getClass());

    /**
     * @param n
     *            For which number of retrieved keyphrases the precision should be computed.
     * @return The micro precision for n
     */
    public double getMicroPrecision(int n)
    {
        int tpCount = 0;
        int fpCount = 0;

        for (String fileName : fileName2performanceMap.keySet()) {
            int tp = getFileTPcount(n, fileName);
            int fp = getFileFPcount(n, fileName);

            if (tp + fp > n) {
                log.error("Cannot have more tp and fp on a level than retrieved in that level.");
                System.exit(1);
            }

            tpCount += tp;
            fpCount += fp;
        }
        return (double) tpCount / (tpCount + fpCount);
    }

    private int getFileTPcount(int n, String fileName)
    {
        int nrOfKeyphrasesRetrieved = fileName2performanceMap.get(fileName)
                .getNrOfKeyphrasesRetrieved();

        // if a result for n larger as the number of actual retrieved keyphrases is requested,
        // return the highest available number instead
        if (n > nrOfKeyphrasesRetrieved) {
            n = nrOfKeyphrasesRetrieved;
        }
        if (fileName2performanceMap.get(fileName).TPcounter.containsKey(n)) {
            return fileName2performanceMap.get(fileName).TPcounter.get(n);
        }
        else {
            return 0;
        }
    }

    private int getFileFPcount(int n, String fileName)
    {
        int nrOfKeyphrasesRetrieved = fileName2performanceMap.get(fileName)
                .getNrOfKeyphrasesRetrieved();

        // if a result for n larger as the number of actual retrieved keyphrases is requested,
        // return the highest available number instead
        if (n > nrOfKeyphrasesRetrieved) {
            n = nrOfKeyphrasesRetrieved;
        }

        if (fileName2performanceMap.get(fileName).FPcounter.containsKey(n)) {
            return fileName2performanceMap.get(fileName).FPcounter.get(n);
        }
        else {
            return 0;
        }
    }

    /**
     * @param n
     *            For which number of retrieved keyphrases the recall should be computed.
     * @return The micro recall for n
     */
    public double getMicroRecall(int n)
    {
        int tpCount = 0;
        int fnCount = 0;

        for (String fileName : fileName2performanceMap.keySet()) {
            tpCount += getFileTPcount(n, fileName);
            fnCount += getFileFNcount(n, fileName);
        }
        return (double) tpCount / (tpCount + fnCount);
    }

    private int getFileFNcount(int n, String fileName)
    {
        int nrOfKeyphrasesRetrieved = fileName2performanceMap.get(fileName)
                .getNrOfKeyphrasesRetrieved();

        // if a result for n larger as the number of actual retrieved keyphrases is requested,
        // return the highest available number instead
        if (n > nrOfKeyphrasesRetrieved) {
            n = nrOfKeyphrasesRetrieved;
        }

        if (fileName2performanceMap.get(fileName).FNcounter.containsKey(n)) {
            return fileName2performanceMap.get(fileName).FNcounter.get(n);
        }
        else {
            return 0;
        }
    }

    /**
     * @param n
     *            For which number of retrieved keyphrases the f-measure should be computed.
     * @return the micro f-measure for n
     */
    public double getMicroFMeasure(int n)
    {
        double precision = getMicroPrecision(n);
        double recall = getMicroRecall(n);
        if (precision + recall > 0) {
            return (2 * precision * recall / (precision + recall));
        }
        else {
            return 0.0;
        }
    }

    public double getMacroPrecision(int n)
    {
        double precision = 0.0;
        for (String fileName : fileName2performanceMap.keySet()) {
            precision += getFilePrecision(n, fileName);
        }
        return precision / fileName2performanceMap.keySet().size();
    }

    private double getFilePrecision(int n, String fileName)
    {
        int nrOfKeyphrasesRetrieved = fileName2performanceMap.get(fileName)
                .getNrOfKeyphrasesRetrieved();

        // if a result for n larger as the number of actual retrieved keyphrases is requested,
        // return the highest available number instead
        if (n > nrOfKeyphrasesRetrieved) {
            n = nrOfKeyphrasesRetrieved;
        }

        int tp = getFileTPcount(n, fileName);
        int fp = getFileFPcount(n, fileName);
        if (tp + fp > 0) {
            return (double) tp / (tp + fp);
        }
        else {
            return 0.0;
        }
    }

    public double getMacroRecall(int n)
    {
        double recall = 0.0;
        for (String fileName : fileName2performanceMap.keySet()) {
            recall += getFileRecall(n, fileName);
        }
        return recall / getRegisteredFiles().size();
    }

    private double getFileRecall(int n, String fileName)
    {
        int nrOfKeyphrasesRetrieved = fileName2performanceMap.get(fileName)
                .getNrOfKeyphrasesRetrieved();

        // if a result for n larger as the number of actual retrieved keyphrases is requested,
        // return the highest available number instead
        if (n > nrOfKeyphrasesRetrieved) {
            n = nrOfKeyphrasesRetrieved;
        }

        int tp = getFileTPcount(n, fileName);
        int fn = getFileFNcount(n, fileName);

        if (tp + fn > 0) {
            return (double) tp / (tp + fn);
        }
        else {
            return 0.0;
        }
    }

    public double getMacroFMeasure(int n)
    {
        double fmeasure = 0.0;
        for (String fileName : fileName2performanceMap.keySet()) {
            fmeasure += getFileFMeasure(n, fileName);
        }
        return fmeasure / fileName2performanceMap.keySet().size();
    }

    private double getFileFMeasure(int n, String fileName)
    {
        int nrOfKeyphrasesRetrieved = fileName2performanceMap.get(fileName)
                .getNrOfKeyphrasesRetrieved();

        // if a result for n larger as the number of actual retrieved keyphrases is requested,
        // return the highest available number instead
        if (n > nrOfKeyphrasesRetrieved) {
            n = nrOfKeyphrasesRetrieved;
        }

        double precision = getFilePrecision(n, fileName);
        double recall = getFileRecall(n, fileName);
        if (precision + recall > 0) {
            return (2 * precision * recall / (precision + recall));
        }
        else {
            return 0.0;
        }
    }

    public String getMicroPerformanceOverview(int n)
    {
        DecimalFormat df = new DecimalFormat("0.000");

        StringBuilder sb = new StringBuilder();
        sb.append("Micro Performance Overview");
        sb.append(LF);
        sb.append("n");
        sb.append("\t");
        sb.append("P");
        sb.append("\t");
        sb.append("R");
        sb.append("\t");
        sb.append("F");
        sb.append("\t");
        sb.append(LF);
        for (int i = 1; i <= n; i++) {
            sb.append(i);
            sb.append("\t");
            sb.append(df.format(getMicroPrecision(i)));
            sb.append("\t");
            sb.append(df.format(getMicroRecall(i)));
            sb.append("\t");
            sb.append(df.format(getMicroFMeasure(i)));
            sb.append("\t");
            sb.append(LF);
        }

        return sb.toString();
    }

    public String getMacroPerformanceOverview(int n)
    {
        DecimalFormat df = new DecimalFormat("0.000");

        StringBuilder sb = new StringBuilder();
        sb.append("Macro Performance Overview");
        sb.append(LF);
        sb.append("n");
        sb.append("\t");
        sb.append("P");
        sb.append("\t");
        sb.append("R");
        sb.append("\t");
        sb.append("F");
        sb.append("\t");
        sb.append(LF);
        for (int i = 1; i <= n; i++) {
            sb.append(i);
            sb.append("\t");
            sb.append(df.format(getMacroPrecision(i)));
            sb.append("\t");
            sb.append(df.format(getMacroRecall(i)));
            sb.append("\t");
            sb.append(df.format(getMacroFMeasure(i)));
            sb.append("\t");
            sb.append(LF);
        }

        return sb.toString();

    }

    public String getMacroPrfOverview()
    {
        DecimalFormat df = new DecimalFormat("0.000");

        StringBuilder sb = new StringBuilder();
        sb.append("Macro P/R/F Overview");
        sb.append(LF);
        sb.append("k");
        sb.append("\t");
        sb.append("P");
        sb.append("\t");
        sb.append("R");
        sb.append("\t");
        sb.append("F");
        sb.append("\t");
        sb.append(LF);

        sb.append(5);
        sb.append("\t");
        sb.append(df.format(getMacroPrecision(5)));
        sb.append("\t");
        sb.append(df.format(getMacroRecall(5)));
        sb.append("\t");
        sb.append(df.format(getMacroFMeasure(5)));
        sb.append("\t");
        sb.append(LF);
        sb.append(10);
        sb.append("\t");
        sb.append(df.format(getMacroPrecision(10)));
        sb.append("\t");
        sb.append(df.format(getMacroRecall(10)));
        sb.append("\t");
        sb.append(df.format(getMacroFMeasure(10)));
        sb.append("\t");
        sb.append(LF);
        sb.append(15);
        sb.append("\t");
        sb.append(df.format(getMacroPrecision(15)));
        sb.append("\t");
        sb.append(df.format(getMacroRecall(15)));
        sb.append("\t");
        sb.append(df.format(getMacroFMeasure(15)));
        sb.append("\t");
        sb.append(LF);
        sb.append(LF);

        return sb.toString();
    }

}

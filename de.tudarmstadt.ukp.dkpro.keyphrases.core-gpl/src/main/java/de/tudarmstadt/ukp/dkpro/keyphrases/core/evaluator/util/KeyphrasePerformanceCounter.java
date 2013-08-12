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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;


public class KeyphrasePerformanceCounter
{
    private static final String LF = System.getProperty("line.separator");

    private static final int NBR_FLOATING_POINTS = 10;

    private final Log log = LogFactory.getLog(getClass());
    private final Map<String, FilePerformance> fileName2performanceMap;

    public KeyphrasePerformanceCounter()
    {
        this.fileName2performanceMap = new HashMap<String, FilePerformance>();
    }

    public void registerFile(String filename, int nrOfKeyphrasesRetrieved)
        throws AnalysisEngineProcessException
    {
        if (fileName2performanceMap.containsKey(filename)) {
            throw new AnalysisEngineProcessException(new Throwable("Filename '" + filename
                    + "' already registered."));
        }
        fileName2performanceMap.put(filename, new FilePerformance(nrOfKeyphrasesRetrieved));
    }

    public Set<String> getRegisteredFiles()
    {
        return fileName2performanceMap.keySet();
    }

    /**
     * @param n
     *            For which number of retrieved keyphrases the precision should be computed.
     * @return
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

    /**
     * @param n
     *            For which number of retrieved keyphrases the recall should be computed.
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

    /**
     * @param n
     *            For which number of retrieved keyphrases the f-measure should be computed.
     * @return
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

    public double getMacroRecall(int n)
    {
        double recall = 0.0;
        for (String fileName : fileName2performanceMap.keySet()) {
            recall += getFileRecall(n, fileName);
        }
        return recall / getRegisteredFiles().size();
    }

    public double getMacroFMeasure(int n)
    {
        double fmeasure = 0.0;
        for (String fileName : fileName2performanceMap.keySet()) {
            fmeasure += getFileFMeasure(n, fileName);
        }
        return fmeasure / fileName2performanceMap.keySet().size();
    }

    /**
     * R
     *
     * @param fileName
     * @return The rprecision for that file.
     */
    public double getFileRPrecision(String fileName)
    {
        return fileName2performanceMap.get(fileName).getRPrecision();
    }

    public double getFilePrecision(int n, String fileName)
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

    public double getFileRecall(int n, String fileName)
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

    public double getFileFMeasure(int n, String fileName)
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

    public int getFileTPcount(int n, String fileName)
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

    public int getFileFPcount(int n, String fileName)
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

    public int getFileFNcount(int n, String fileName)
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

    public double getAverageRPrecision()
    {
        double rPrecisionSum = 0.0;
        List<String> files = new ArrayList<String>(getRegisteredFiles());
        for (String fileName : files) {
            rPrecisionSum += this.getFileRPrecision(fileName);
        }
        return rPrecisionSum / files.size();
    }

    public void setFileTPcount(String fileName, int i, int count)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setTPcounter(i, count);
    }

    public void setFileFPcount(String fileName, int i, int count)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setFPcounter(i, count);
    }

    public void setFileFNcount(String fileName, int i, int count)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setFNcounter(i, count);
    }

    public void setRPrecision(String fileName, double rprecision)
    {
        FilePerformance filePerformance = fileName2performanceMap.get(fileName);
        filePerformance.setRPrecision(rprecision);
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

    public String getFilePerformanceOverview(int n)
    {
        DecimalFormat df = new DecimalFormat("0.000");

        StringBuilder sb = new StringBuilder();
        sb.append("File Performance Overview");
        sb.append(LF);
        sb.append("File");
        sb.append("\t");
        sb.append("P");
        sb.append("\t");
        sb.append("R");
        sb.append("\t");
        sb.append("F");
        sb.append("\t");
        sb.append("R-P");
        sb.append("\t");
        sb.append(LF);

        List<String> files = new ArrayList<String>(getRegisteredFiles());
        Collections.sort(files);
        for (String fileName : files) {
            sb.append(fileName);
            sb.append("\t");
            sb.append(df.format(getFilePrecision(n, fileName)));
            sb.append("\t");
            sb.append(df.format(getFileRecall(n, fileName)));
            sb.append("\t");
            sb.append(df.format(getFileFMeasure(n, fileName)));
            sb.append("\t");
            sb.append(df.format(getFileRPrecision(fileName)));
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

    public String getMicroPrfOverview()
    {
        DecimalFormat df = new DecimalFormat("0.000");

        StringBuilder sb = new StringBuilder();
        sb.append("Micro P/R/F Overview");
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
        sb.append(df.format(getMicroPrecision(5)));
        sb.append("\t");
        sb.append(df.format(getMicroRecall(5)));
        sb.append("\t");
        sb.append(df.format(getMicroFMeasure(5)));
        sb.append("\t");
        sb.append(LF);
        sb.append(10);
        sb.append("\t");
        sb.append(df.format(getMicroPrecision(10)));
        sb.append("\t");
        sb.append(df.format(getMicroRecall(10)));
        sb.append("\t");
        sb.append(df.format(getMicroFMeasure(10)));
        sb.append("\t");
        sb.append(LF);
        sb.append(15);
        sb.append("\t");
        sb.append(df.format(getMicroPrecision(15)));
        sb.append("\t");
        sb.append(df.format(getMicroRecall(15)));
        sb.append("\t");
        sb.append(df.format(getMicroFMeasure(15)));
        sb.append("\t");
        sb.append(LF);
        sb.append(LF);

        return sb.toString();
    }

    public Double getMeanAveragePrecision()
    {
        BigDecimal meanAveragePrecision = new BigDecimal("0.0");
        for (FilePerformance filePerformance : fileName2performanceMap.values()) {
            meanAveragePrecision = meanAveragePrecision.
                    add(filePerformance.calculateAveragePrecision());
        }
        meanAveragePrecision = meanAveragePrecision.divide(new
                BigDecimal(String.valueOf(fileName2performanceMap.size())), 2, RoundingMode.UP) ;
        return meanAveragePrecision.doubleValue();
    }

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
                BigDecimal denominator = new BigDecimal(String.valueOf(TPcounter.get(i))
                        + FPcounter.get(i));
                precisions[i - 1] = numerator.divide(denominator, 10, RoundingMode.UP);
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
                        NBR_FLOATING_POINTS,RoundingMode.UP);
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
                    NBR_FLOATING_POINTS, RoundingMode.UP);

            return averagePrecision;

        }

    }
}

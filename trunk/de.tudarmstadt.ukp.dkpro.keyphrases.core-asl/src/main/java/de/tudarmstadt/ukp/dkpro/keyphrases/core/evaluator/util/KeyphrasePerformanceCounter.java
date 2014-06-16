/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.core.evaluator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class KeyphrasePerformanceCounter extends AbstractKeyphraseCounter
{
    private static final String LF = System.getProperty("line.separator");

    static final int NBR_FLOATING_POINTS = 10;

    private final Log log = LogFactory.getLog(getClass());

    public KeyphrasePerformanceCounter()
    {
        this.fileName2performanceMap = new HashMap<String, FilePerformance>();
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
     * R-Precision
     *
     * @param fileName
     * @return The r-precision for that file.
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
        if(fileName2performanceMap.size() == 0){
            return 0d;
        }
        meanAveragePrecision = meanAveragePrecision.divide(new
                BigDecimal(String.valueOf(fileName2performanceMap.size())), NBR_FLOATING_POINTS, RoundingMode.UP) ;
        return meanAveragePrecision.doubleValue();
    }
}
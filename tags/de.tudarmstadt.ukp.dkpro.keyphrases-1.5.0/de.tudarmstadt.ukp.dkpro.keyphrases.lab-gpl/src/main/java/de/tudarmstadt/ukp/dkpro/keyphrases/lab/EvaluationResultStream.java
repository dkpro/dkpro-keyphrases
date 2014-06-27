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
package de.tudarmstadt.ukp.dkpro.keyphrases.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.io.Closeables;

import de.tudarmstadt.ukp.dkpro.lab.storage.StreamReader;
import de.tudarmstadt.ukp.dkpro.lab.storage.StreamWriter;

public class EvaluationResultStream
    implements StreamReader, StreamWriter
{
    private double macroPrecision;
    private double macroPrecisionAt5;
    private double macroPrecisionAt10;
    private double macroPrecisionAt15;
    private double macroRecall;
    private double macroRecallAt5;
    private double macroRecallAt10;
    private double macroRecallAt15;

    private double microPrecision;
    private double microPrecisionAt5;
    private double microPrecisionAt10;
    private double microPrecisionAt15;
    private double microRecall;
    private double microRecallAt5;
    private double microRecallAt10;
    private double microRecallAt15;

    private double rPrecisionAll;

    private double meanAveragePrecision;

    private double maxMicroRecall;
    private double maxMacroRecall;


    public EvaluationResultStream setMacroPrecision(double precision)
    {
        this.macroPrecision = precision;
        return this;
    }


    public EvaluationResultStream setMacroRecall(double recall)
    {
        this.macroRecall = recall;
        return this;
    }


    public EvaluationResultStream setMicroPrecision(double precision)
    {
        this.microPrecision = precision;
        return this;
    }


    public EvaluationResultStream setMicroRecall(double recall)
    {
        this.microRecall = recall;
        return this;
    }


    public EvaluationResultStream setRPrecisionAll(double r)
    {
        rPrecisionAll = r;
        return this;
    }

    public EvaluationResultStream setMeanAveragePrecision(double aMeanAveragePrecision){
        this.meanAveragePrecision = aMeanAveragePrecision;
        return this;
    }


    public EvaluationResultStream setMaxMicroRecall(double maxMicroRecall)
    {
        this.maxMicroRecall = maxMicroRecall;
        return this;
    }

    public EvaluationResultStream setMaxMacroRecall(double maxMacroRecall)
    {
        this.maxMacroRecall = maxMacroRecall;
        return this;
    }


    public double getMacroPrecisionAt5()
    {
        return macroPrecisionAt5;
    }


    public EvaluationResultStream setMacroPrecisionAt5(double macroPrecisionAt5)
    {
        this.macroPrecisionAt5 = macroPrecisionAt5;
        return this;
    }


    public double getMacroPrecisionAt10()
    {
        return macroPrecisionAt10;
    }


    public EvaluationResultStream setMacroPrecisionAt10(double macroPrecisionAt10)
    {
        this.macroPrecisionAt10 = macroPrecisionAt10;
        return this;
    }


    public double getMacroPrecisionAt15()
    {
        return macroPrecisionAt15;
    }


    public EvaluationResultStream setMacroPrecisionAt15(double macroPrecisionAt15)
    {
        this.macroPrecisionAt15 = macroPrecisionAt15;
        return this;
    }


    public double getMacroRecallAt5()
    {
        return macroRecallAt5;
    }


    public EvaluationResultStream setMacroRecallAt5(double macroRecallAt5)
    {
        this.macroRecallAt5 = macroRecallAt5;
        return this;
    }


    public double getMacroRecallAt10()
    {
        return macroRecallAt10;
    }


    public EvaluationResultStream setMacroRecallAt10(double macroRecallAt10)
    {
        this.macroRecallAt10 = macroRecallAt10;
        return this;
    }


    public double getMacroRecallAt15()
    {
        return macroRecallAt15;
    }


    public EvaluationResultStream setMacroRecallAt15(double macroRecallAt15)
    {
        this.macroRecallAt15 = macroRecallAt15;
        return this;
    }


    public double getMicroPrecisionAt5()
    {
        return microPrecisionAt5;
    }


    public EvaluationResultStream setMicroPrecisionAt5(double microPrecisionAt5)
    {
        this.microPrecisionAt5 = microPrecisionAt5;
        return this;
    }


    public double getMicroPrecisionAt10()
    {
        return microPrecisionAt10;
    }


    public EvaluationResultStream setMicroPrecisionAt10(double microPrecisionAt10)
    {
        this.microPrecisionAt10 = microPrecisionAt10;
        return this;
    }


    public double getMicroPrecisionAt15()
    {
        return microPrecisionAt15;
    }


    public EvaluationResultStream setMicroPrecisionAt15(double microPrecisionAt15)
    {
        this.microPrecisionAt15 = microPrecisionAt15;
        return this;
    }


    public double getMicroRecallAt5()
    {
        return microRecallAt5;
    }


    public EvaluationResultStream setMicroRecallAt5(double microRecallAt5)
    {
        this.microRecallAt5 = microRecallAt5;
        return this;
    }


    public double getMicroRecallAt10()
    {
        return microRecallAt10;
    }


    public EvaluationResultStream setMicroRecallAt10(double microRecallAt10)
    {
        this.microRecallAt10 = microRecallAt10;
        return this;
    }


    public double getMicroRecallAt15()
    {
        return microRecallAt15;
    }


    public EvaluationResultStream setMicroRecallAt15(double microRecallAt15)
    {
        this.microRecallAt15 = microRecallAt15;
        return this;
    }


    @Override
    public void read(InputStream inputStream)
        throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        macroPrecision = Double.parseDouble(reader.readLine());
        macroPrecisionAt5 = Double.parseDouble(reader.readLine());
        macroPrecisionAt10 = Double.parseDouble(reader.readLine());
        macroPrecisionAt15 = Double.parseDouble(reader.readLine());
        macroRecall = Double.parseDouble(reader.readLine());
        macroRecallAt5 = Double.parseDouble(reader.readLine());
        macroRecallAt10 = Double.parseDouble(reader.readLine());
        macroRecallAt15 = Double.parseDouble(reader.readLine());
        microPrecision = Double.parseDouble(reader.readLine());
        microPrecisionAt5 = Double.parseDouble(reader.readLine());
        microPrecisionAt10 = Double.parseDouble(reader.readLine());
        microPrecisionAt15 = Double.parseDouble(reader.readLine());
        microRecall = Double.parseDouble(reader.readLine());
        microRecallAt5 = Double.parseDouble(reader.readLine());
        microRecallAt10 = Double.parseDouble(reader.readLine());
        microRecallAt15 = Double.parseDouble(reader.readLine());
        rPrecisionAll = Double.parseDouble(reader.readLine());
        meanAveragePrecision = Double.parseDouble(reader.readLine());
        maxMicroRecall = Double.parseDouble(reader.readLine());
        maxMacroRecall = Double.parseDouble(reader.readLine());

        Closeables.closeQuietly(reader);
        Closeables.closeQuietly(inputStream);
    }


    @Override
    public void write(OutputStream outputStream)
        throws Exception
    {
        PrintWriter printer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        printer.println(macroPrecision);
        printer.println(macroPrecisionAt5);
        printer.println(macroPrecisionAt10);
        printer.println(macroPrecisionAt15);
        printer.println(macroRecall);
        printer.println(macroRecallAt5);
        printer.println(macroRecallAt10);
        printer.println(macroRecallAt15);
        printer.println(microPrecision);
        printer.println(microPrecisionAt5);
        printer.println(microPrecisionAt10);
        printer.println(microPrecisionAt15);
        printer.println(microRecall);
        printer.println(microRecallAt5);
        printer.println(microRecallAt10);
        printer.println(microRecallAt15);
        printer.println(rPrecisionAll);
        printer.println(meanAveragePrecision);
        printer.println(maxMicroRecall);
        printer.println(maxMacroRecall);

        IOUtils.closeQuietly(printer);
        IOUtils.closeQuietly(outputStream);
    }


    public Map<String, Double> getResults()
    {
        HashMap<String, Double> results = new HashMap<String, Double>();
        results.put("Macro Precison", macroPrecision);
        results.put("Macro Precison at 5", macroPrecisionAt5);
        results.put("Macro Precison at 10", macroPrecisionAt10);
        results.put("Macro Precison at 15", macroPrecisionAt15);
        results.put("Macro Recall", macroRecall);
        results.put("Macro Recall at 5", macroRecallAt5);
        results.put("Macro Recall at 10", macroRecallAt10);
        results.put("Macro Recall at 15", macroRecallAt15);
        results.put("Micro Precison", microPrecision);
        results.put("Micro Precison at 5", microPrecisionAt5);
        results.put("Micro Precison at 10", microPrecisionAt10);
        results.put("Micro Precison at 15", microPrecisionAt15);
        results.put("Micro Recall", microRecall);
        results.put("Micro Recall at 5", microRecallAt5);
        results.put("Micro Recall at 10", microRecallAt10);
        results.put("Micro Recall at 15", microRecallAt15);
        results.put("R-Precision (All)", rPrecisionAll);
        results.put("Mean Average Precision", meanAveragePrecision);
        results.put("Max micro Recall", maxMicroRecall);
        results.put("Max macro Recall", maxMacroRecall);
        return results;
    }


}

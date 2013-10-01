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

import com.google.common.io.Closeables;

import de.tudarmstadt.ukp.dkpro.lab.storage.StreamReader;
import de.tudarmstadt.ukp.dkpro.lab.storage.StreamWriter;

public class EvaluationResultStream
    implements StreamReader, StreamWriter
{
    private double macroPrecision;
    private double macroRecall;

    private double microPrecision;
    private double microRecall;

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


    @Override
    public void read(InputStream inputStream)
        throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        macroPrecision = Double.parseDouble(reader.readLine());
        macroRecall = Double.parseDouble(reader.readLine());
        microPrecision = Double.parseDouble(reader.readLine());
        microRecall = Double.parseDouble(reader.readLine());
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
        printer.println(macroRecall);
        printer.println(microPrecision);
        printer.println(microRecall);
        printer.println(rPrecisionAll);
        printer.println(meanAveragePrecision);
        printer.println(maxMicroRecall);
        printer.println(maxMacroRecall);

        Closeables.closeQuietly(printer);
        Closeables.closeQuietly(outputStream);
    }


    public Map<String, Double> getResults()
    {
        HashMap<String, Double> results = new HashMap<String, Double>();
        results.put("Macro Precison", macroPrecision);
        results.put("Macro Recall", macroRecall);
        results.put("Micro Precison", microPrecision);
        results.put("Micro Recall", microRecall);
        results.put("R-Precision (All)", rPrecisionAll);
        results.put("Mean Average Precision", meanAveragePrecision);
        results.put("Max micro Recall", maxMicroRecall);
        results.put("Max macro Recall", maxMacroRecall);
        return results;
    }


}

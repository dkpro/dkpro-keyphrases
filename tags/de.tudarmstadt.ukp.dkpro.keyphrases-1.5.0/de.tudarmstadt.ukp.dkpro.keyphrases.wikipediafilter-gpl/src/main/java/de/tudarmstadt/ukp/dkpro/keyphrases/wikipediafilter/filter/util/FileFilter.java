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
package de.tudarmstadt.ukp.dkpro.keyphrases.wikipediafilter.filter.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class FileFilter
{

    public static void main(String[] args) throws IOException
    {
        Set<String> articles = new HashSet<String>();
        articles.addAll(FileUtils.readLines(new File("target/wikipedia/articles.txt")));

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("target/passwords.txt.filtered")));
        for(String line : FileUtils.readLines(new File("target/passwords.txt"))){
            if(articles.contains(line.split("\t")[0].toLowerCase())){
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        }
    }
}

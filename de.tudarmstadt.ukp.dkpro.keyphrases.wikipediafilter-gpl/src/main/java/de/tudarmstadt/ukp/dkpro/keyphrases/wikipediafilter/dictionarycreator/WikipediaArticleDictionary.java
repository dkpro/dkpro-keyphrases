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
package de.tudarmstadt.ukp.dkpro.keyphrases.wikipediafilter.dictionarycreator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import de.tudarmstadt.ukp.wikipedia.api.DatabaseConfiguration;
import de.tudarmstadt.ukp.wikipedia.api.PageIterator;
import de.tudarmstadt.ukp.wikipedia.api.WikiConstants.Language;
import de.tudarmstadt.ukp.wikipedia.api.Wikipedia;
import de.tudarmstadt.ukp.wikipedia.api.exception.WikiApiException;

public class WikipediaArticleDictionary {

    public static void main(String[] args) throws WikiApiException, IOException
    {
        int buffer = 10000;
        if(args.length == 1){
            buffer = Integer.valueOf(args[0]);
        }
        
        // configure the database connection parameters
        DatabaseConfiguration dbConfig = new DatabaseConfiguration();
        dbConfig.setHost("localhost");
        dbConfig.setDatabase("wiki_en_20080103");
        dbConfig.setUser("root");
        dbConfig.setPassword("");
        dbConfig.setLanguage(Language.english);

        // Create the Wikipedia object
        Wikipedia wiki = new Wikipedia(dbConfig);

        PageIterator pageIterator = new PageIterator(wiki, true, buffer);

        File file = new File("target/wikipedia/articles.txt");
        file.getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        while(pageIterator.hasNext()){
            writer.write(pageIterator.next().getTitle().getPlainTitle());
            writer.newLine();
        }
        writer.close();
    }
}

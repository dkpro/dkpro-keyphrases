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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import java.io.IOException;
import java.util.List;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public interface KeyphraseExtractor {
	
    /**
     * @param inputText The input text.
     *  
     * @return
     *   The list of keyphrases extracted from the input text.
     *   Keyphrases are ordered by score in descending order.
     *   If a keyphrase occurs multiple times in the document,
     *   it will also be duplicated in the list.
     */
    List<Keyphrase> extract(String inputText) throws IOException;

    /**
     * @return The name of the extractor.
     */
    String getName();
    
    /**
     * @return Returns a string with the configuration details of this keyphrase extractor.
     */
    String getConfigurationDetails();
    
}

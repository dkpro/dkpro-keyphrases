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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface BookIndexPhraseExtractor
{

	/**
	 * @param textFile
	 *          The input text.
	 *
	 * @return The list of bookindex phrases extracted from the input text.
	 *         Phrases are ordered by score in descending order.
	 */
	List<String> extract(File textFile)
		throws IOException;

}

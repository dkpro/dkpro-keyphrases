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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.util;

import java.io.Serializable;
import java.util.Comparator;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase;


/**
 * Sorts descending by score.
 *
 * @author Mateusz Parzonka
 */
public class BookIndexPhraseScoreComparator implements Serializable, Comparator<BookIndexPhrase> {

	private static final long serialVersionUID = -7053969834375022386L;

		@Override
		public int compare(BookIndexPhrase b1, BookIndexPhrase b2) {
			return -1 * Double.compare(b1.getScore(), b2.getScore());
    }
}
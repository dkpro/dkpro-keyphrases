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

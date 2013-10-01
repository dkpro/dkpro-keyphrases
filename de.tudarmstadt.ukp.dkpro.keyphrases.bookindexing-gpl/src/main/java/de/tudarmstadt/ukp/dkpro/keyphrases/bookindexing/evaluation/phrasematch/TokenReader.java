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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.evaluation.phrasematch;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

/**
 * @author Mateusz Parzonka
 */
public class TokenReader implements StringReader
{

	private final String path;
	private final String suffix;
	private final String delimiter;
	private final boolean lowercase;

	/**
	 *
	 * @param path the path to the textfile
	 * @param suffix the suffix
	 * @param delimiter
	 * @param lowercase
	 */
	public TokenReader(String path, String suffix,String delimiter, boolean lowercase)
	{
		super();
		this.path = path;
		this.suffix = suffix;
		this.delimiter = delimiter;
		this.lowercase = lowercase;
	}

	@Override
	public Set<String> getSetOfStrings(final JCas jcas)
		throws AnalysisEngineProcessException
	{
		return new HashSet<String>(getListOfStrings(jcas));
	}

	@Override
	public List<String> getListOfStrings(final JCas jcas)
		throws AnalysisEngineProcessException
	{
		String goldPhrases;
		try {
			goldPhrases = FileUtils.readFileToString(new File(FilenameUtils.concat(path, getDocumentBaseName(jcas) + normalizeSuffix(suffix))));
		}
		catch (final IOException e) {
			throw new AnalysisEngineProcessException(new Throwable(e));
		}

		final List<String> result = new ArrayList<String>();

		for (String goldPhrase : goldPhrases.split(delimiter)) {
			goldPhrase = lowercase ? goldPhrase.toLowerCase() : goldPhrase;
			goldPhrase = goldPhrase.trim();
			if (!goldPhrase.isEmpty())
				result.add(goldPhrase);
		}

		return result;
	}

	/**
	 * @param jcas
	 * @return the document basename from the parsed document-URI-path.
	 * @throws AnalysisEngineProcessException
	 */
	private String getDocumentBaseName(JCas jcas)
		throws AnalysisEngineProcessException
	{
		try {
			URI uri = new URI(DocumentMetaData.get(jcas).getDocumentUri());
			return FilenameUtils.getBaseName(uri.getPath());
		}
		catch (URISyntaxException e) {
			throw new AnalysisEngineProcessException(e);
		}
	}

	private static String normalizeSuffix(String suffix) {
		return suffix.startsWith(".") ? suffix : "." + suffix;
	}

}

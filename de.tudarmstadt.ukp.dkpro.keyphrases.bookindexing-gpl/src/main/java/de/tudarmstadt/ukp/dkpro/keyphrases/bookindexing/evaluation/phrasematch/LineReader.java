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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

/**
 * Retrieves strings associated with a document name using a line based string
 * reader.
 *
 * @author Mateusz Parzonka
 *
 */
public class LineReader
	implements StringReader
{

	private final String pathName;
	private final String suffix;
	private final boolean lowercase;
	private final String encoding;

	public LineReader(String pathName, String suffix, boolean lowercase)
	{
		this.pathName = pathName;
		this.suffix = suffix;
		this.lowercase = lowercase;
		this.encoding = "UTF-8";
	}

	public LineReader(String pathName, String suffix, boolean lowercase,
			String encoding)
	{
		this.pathName = pathName;
		this.suffix = suffix;
		this.lowercase = lowercase;
		this.encoding = encoding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.tudarmstadt.ukp.dkpro.semantics.bookindexing.evaluation.phrasematch.
	 * StringReader#getSetOfStrings(java.lang.String)
	 */
	@Override
	public Set<String> getSetOfStrings(JCas jcas)
		throws AnalysisEngineProcessException
	{
		return new TreeSet<String>(getListOfStrings(jcas));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.tudarmstadt.ukp.dkpro.semantics.bookindexing.evaluation.phrasematch.
	 * StringReader#getListOfStrings(java.lang.String)
	 */
	@Override
	public List<String> getListOfStrings(JCas jcas)
		throws AnalysisEngineProcessException
	{

		List<String> goldList = new ArrayList<String>();
		LineIterator lineIterator;
		try {
			lineIterator = FileUtils.lineIterator(new File(getPath(getDocumentBaseName(jcas))),
					encoding);
		}
		catch (IOException e) {
			throw new AnalysisEngineProcessException(new Throwable(e));
		}
		try {
			while (lineIterator.hasNext()) {
				String line = lineIterator.nextLine().trim();
				if (!line.isEmpty()) {
					if (lowercase)
						line = line.toLowerCase();
					goldList.add(line);
				}
			}
		}
		finally {
			LineIterator.closeQuietly(lineIterator);
		}
		return goldList;
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

	private String getPath(String baseName)
	{
		return FilenameUtils.separatorsToSystem(FilenameUtils.concat(pathName,
				baseName)
				+ ((suffix.startsWith(".")) ? suffix : "." + suffix));
	}

}

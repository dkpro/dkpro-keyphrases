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

import org.junit.Ignore;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.model.DfModel;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.TfidfUtils;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.AbstractBookIndexingTestBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.NChunkCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.DfModelBuilder;

public class DfModelBuilderTest
	extends AbstractBookIndexingTestBase

{
	private final static String LANGUAGE = "en";
	private final static String INPUT_DIR = "src/main/resources/books/excerpts";
	private final static String INPUT_SUFFIX = "txt";
	private final static String OUTPUT_DIR = "src/test/resources/output/";

	@Test
	public void buildTfidfModel()
	{
		DfModelBuilder t = new DfModelBuilder(LANGUAGE, new File(INPUT_DIR),
				INPUT_SUFFIX);
		t.buildTokenModel(new File(OUTPUT_DIR + "token.dfmodel"));
		t.buildStemModel(new File(OUTPUT_DIR + "stem.dfmodel"));
		t.buildLemmaModel(new File(OUTPUT_DIR + "lemma.dfmodel"));
	}

	@Test
	public void buildCandidateModel()
	{
		DfModelBuilder t = new DfModelBuilder(LANGUAGE, new File(INPUT_DIR),
				INPUT_SUFFIX);

		NChunkCandidateSet candidateSet = new NChunkCandidateSet();
		candidateSet.setResolveOverlaps(false);

		t.buildKeyphraseCandidateModel(candidateSet, new File(OUTPUT_DIR
				+ "ne.dfmodel"));
	}

	@Test
	@Ignore
	public void name()
		throws Exception
	{
		DfModel model = (DfModel) TfidfUtils
				.deserialize("src/test/resources/books/token.dfModel");
		System.out.println("DocCount: " + model.getDocumentCount());
		System.out.println("Osmia: " + model.getDf("Osmia"));
		System.out.println("osmia: " + model.getDf("osmia"));
		System.out.println("the: " + model.getDf("the"));
	}
}

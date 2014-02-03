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

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedKeyphrases;
import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedUniqueKeyphrases;
import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.keyphraseList2StringList;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.CooccurrenceGraphExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class CooccurrenceGraphExtractorTest
{

    @Test
    public void cooccurrenceGraphExtractorTest()
        throws Exception
    {

        String testDoc = FileUtils.readFileToString(
                new File("src/test/resources/keyphrase/extractor/test.txt")
        );

        KeyphraseExtractor textRank = CooccurrenceGraphExtractor.createTextRankExtractor();

        System.out.println(textRank.getConfigurationDetails());
        for (Keyphrase keyphrase : getTopRankedUniqueKeyphrases(textRank.extract(testDoc), 5)) {
            System.out.println(keyphrase.getKeyphrase());
        }

        String textrankKeyphrases = StringUtils.join(
                keyphraseList2StringList(
                        getTopRankedUniqueKeyphrases(textRank.extract(testDoc), 5)
                ),
                ","
          );

        assertEquals(
        		"variable temporal rates,temporal rates,temporal rate," +
        		"video object-based rate allocation,multiple video objects",
				textrankKeyphrases);

        CooccurrenceGraphExtractor cooccExtractor = new CooccurrenceGraphExtractor();
        cooccExtractor.setMinKeyphraseLength(2);
        cooccExtractor.setCandidate(
                new Candidate(
                        CandidateType.Token,
                        PosType.N
                )
        );
        
        List<Keyphrase> keyphrases = cooccExtractor.extract(testDoc);
        assertEquals(28, keyphrases.size());

        List<Keyphrase> top5Keyphrases = getTopRankedUniqueKeyphrases(keyphrases, 5);
        assertEquals(5, top5Keyphrases.size());
        
        
        String cooccKeyphrases = StringUtils.join(
                keyphraseList2StringList(top5Keyphrases), ",");
        for(Keyphrase keyphrase : top5Keyphrases){
            System.out.println(keyphrase);
        }

        System.out.println(cooccKeyphrases);
        assertEquals(
                "rate allocation,target bit rate,algorithm deals,bit allocation,object level",
                cooccKeyphrases
        );
    }
}

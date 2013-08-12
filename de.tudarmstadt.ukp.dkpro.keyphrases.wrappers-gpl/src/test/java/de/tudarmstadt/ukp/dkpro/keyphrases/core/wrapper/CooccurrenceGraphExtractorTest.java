package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedKeyphrases;
import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedUniqueKeyphrases;
import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.keyphraseList2StringList;
import static org.junit.Assert.assertEquals;

import java.io.File;

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

        String cooccKeyphrases = StringUtils.join(
                keyphraseList2StringList(
                        getTopRankedKeyphrases(cooccExtractor.extract(testDoc), 5)
                ),
                ","
          );

        assertEquals(
                "rate allocation,target bit rate,algorithm deals,bit allocation,object level",
                cooccKeyphrases
        );
    }
}

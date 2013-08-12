package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedKeyphrases;
import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.keyphraseList2StringList;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractor_ImplBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.PositionBaselineExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.PosType;

public class BaselineExtractorTest
{

    @Test
    public void baselineTest()
        throws Exception
    {

        String testDoc = FileUtils.readFileToString(new File("src/test/resources/keyphrase/extractor/test.txt"));

        Candidate nounTokens = new Candidate(
                CandidateType.Token,
                PosType.N
        );
        
        KeyphraseExtractor_ImplBase positionBaselineExtractor = new PositionBaselineExtractor();
        positionBaselineExtractor.setCandidate(nounTokens);
        
        String actualKeyphrases = StringUtils.join(
                keyphraseList2StringList(
                        getTopRankedKeyphrases(positionBaselineExtractor.extract(testDoc), 5)
                ),
                ","
        );
        
        assertEquals("MPEG,video,rate allocation,rates,bit allocation", actualKeyphrases);
    }
}

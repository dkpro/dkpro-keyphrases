package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.tutorial;

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedUniqueKeyphrases;

import java.io.File;

import org.apache.commons.io.FileUtils;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.TfidfExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class TfidfExtractorTutorial
{

    public static void main(String[] args)
        throws Exception
    {

        TfidfExtractor tfidfExtractor = new TfidfExtractor();
        tfidfExtractor.setCandidate(new Candidate(CandidateType.NounPhrase));
        
        tfidfExtractor.buildTfidfModel(new File("src/test/resources/data/hulth/train/"), "abstr");

        System.out.println(tfidfExtractor.getConfigurationDetails());

        String text = FileUtils.readFileToString(new File("src/test/resources/data/hulth/train/6.abstr"));
        
        for (Keyphrase keyphrase : getTopRankedUniqueKeyphrases(tfidfExtractor.extract(text), 5)) {
            System.out.println(keyphrase.getKeyphrase());
        }
    }
}
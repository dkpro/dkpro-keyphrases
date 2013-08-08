package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.tutorial;

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedKeyphrases;
import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedUniqueKeyphrases;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.CooccurrenceGraphExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class CooccurrenceGraphExtractorTutorial
{

    public static void main(String[] args)
        throws Exception
    {

        String text = "MPEG-4 video object-based rate allocation with variable temporal rates. "
                + "In object-based coding, bit allocation is performed at the object level and "
                + "temporal rates of different objects may vary. The proposed algorithm "
                + "deals with these two issues when coding multiple video objects (MVOs). "
                + "The proposed algorithm is able to successfully achieve the target bit "
                + "rate, effectively code arbitrarily shaped MVOs with different temporal "
                + "rates, and maintain a stable buffer level.";

        KeyphraseExtractor textRank = CooccurrenceGraphExtractor.createTextRankExtractor();

        System.out.println(textRank.getConfigurationDetails());
        for (Keyphrase keyphrase : getTopRankedUniqueKeyphrases(textRank.extract(text), 5)) {
            System.out.println(keyphrase.getKeyphrase());
        }

        System.out.println();

        CooccurrenceGraphExtractor cooccExtractor = new CooccurrenceGraphExtractor();
        cooccExtractor.setMinKeyphraseLength(2);
        cooccExtractor.setCandidate(
        		new Candidate(
        				CandidateType.Token,
        				PosType.N
        		)
        );
        
        System.out.println(cooccExtractor.getConfigurationDetails());
        for (Keyphrase keyphrase : getTopRankedKeyphrases(cooccExtractor.extract(text), 5)) {
            System.out.println(keyphrase.getKeyphrase() + "\t\t" + keyphrase.getScore());
        }
    }
}
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.tutorial;

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedKeyphrases;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractor_ImplBase;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.PositionBaselineExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.RandomBaselineExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.CandidateType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.Candidate.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class BaselineExtractorTutorial
{

    public static void main(String[] args)
        throws Exception
    {

    	// TODO add some test data from keyphrase dataset to resources and use apache commons file loading to input the data
        String text = "MPEG-4 video object-based rate allocation with variable temporal rates. "
                + "In object-based coding, bit allocation is performed at the object level and "
                + "temporal rates of different objects may vary. The proposed algorithm "
                + "deals with these two issues when coding multiple video objects (MVOs). "
                + "The proposed algorithm is able to successfully achieve the target bit "
                + "rate, effectively code arbitrarily shaped MVOs with different temporal "
                + "rates, and maintain a stable buffer level.";

        Candidate nounTokens = new Candidate(
				CandidateType.Token,
				PosType.N
		);
        
        KeyphraseExtractor_ImplBase positionBaselineExtractor = new PositionBaselineExtractor();
        positionBaselineExtractor.setCandidate(nounTokens);
        
        System.out.println(positionBaselineExtractor.getConfigurationDetails());
        for (Keyphrase keyphrase : getTopRankedKeyphrases(positionBaselineExtractor.extract(text), 5)) {
            System.out.println(keyphrase.getKeyphrase() + "\t\t" + keyphrase.getScore());
        }

        System.out.println();
        
        KeyphraseExtractor_ImplBase randomBaselineExtractor = new RandomBaselineExtractor();
        randomBaselineExtractor.setCandidate(nounTokens);

        System.out.println(randomBaselineExtractor.getConfigurationDetails());
        for (Keyphrase keyphrase : getTopRankedKeyphrases(randomBaselineExtractor.extract(text), 5)) {
            System.out.println(keyphrase.getKeyphrase() + "\t\t" + keyphrase.getScore());
        }
    }
}
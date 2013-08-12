package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.tutorial;

import static de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeyphraseExtractorUtils.getTopRankedUniqueKeyphrases;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.KeaExtractor;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

public class KeaExtractorTutorial
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

        KeaExtractor keaExtractor = new KeaExtractor();

        System.out.println(keaExtractor.getConfigurationDetails());
        for (Keyphrase keyphrase : getTopRankedUniqueKeyphrases(keaExtractor.extract(text), 5)) {
            System.out.println(keyphrase.getKeyphrase());
        }
   }
}
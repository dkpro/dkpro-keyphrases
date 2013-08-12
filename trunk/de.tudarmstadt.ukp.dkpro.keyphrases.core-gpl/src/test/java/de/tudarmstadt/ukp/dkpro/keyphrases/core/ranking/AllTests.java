package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PositionRankingTest.class, RandomRankingTest.class,
        TfBackgroundIdfRankingTest.class, TfidfRankingTest.class, TfRankingTest.class })
public class AllTests
{

}

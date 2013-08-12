package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.tutorial;

import java.io.File;

import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.NamedEntityCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.StemCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.TokenCandidateSet;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.candidate.candidatesets.base.CandidateSet.PosType;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.DfModelBuilder;
import de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.wrapper.TfidfExtractor;

/**
 *
 * @author Mateusz Parzonka
 *
 */
public class DfModelBuilderTutorial
{

	public static void main(String... args)
	{
		DfModelBuilder tmb = new DfModelBuilder("en", new File(
				"src/main/resources/books/fulltext/"), "txt");
		tmb.buildTokenModel(new File(
				"src/main/resources/books/fulltext/token.dfModel"));
	}

	// this method is not meant to be executed
	public void tutorial(String[] args)
	{

		// dfmodels are based on representative number of documents. You need to
		// specify the language, location of the directory and suffix.
		DfModelBuilder tmb = new DfModelBuilder("en", new File("somedir"), ".txt");

		// the builder can create models based on different underlying types:
		// tokens, lemmas, stems etc.
		tmb.buildTokenModel(new File("token.model"));
		tmb.buildLemmaModel(new File("lemma.model"));
		tmb.buildStemModel(new File("stem.model"));

		// it is also possible to create models based on finer adjusted types:
		TokenCandidateSet candidateSet;
		// example tokens tagged as nouns, adverbs and adjectives:
		candidateSet = new TokenCandidateSet();
		candidateSet.setPosToKeep(PosType.N, PosType.ADV, PosType.ADJ);
		tmb.buildKeyphraseCandidateModel(candidateSet, new File(
				"token(n,adj,adv).model"));
		// which also works for stems:
		StemCandidateSet stemCandidateSet = new StemCandidateSet();
		stemCandidateSet.setPosToKeep(PosType.N, PosType.ADV, PosType.ADJ);
		tmb.buildKeyphraseCandidateModel(candidateSet, new File(
				"stem(n,adj,adv).model"));

		// ... or named entities:
		NamedEntityCandidateSet namedEntitySet = new NamedEntityCandidateSet();
		tmb.buildKeyphraseCandidateModel(namedEntitySet, new File("ne.model"));

		TfidfExtractor extractor = new TfidfExtractor();
		extractor.setTfidfModelFile(new File("lemma.model"));

	}

}

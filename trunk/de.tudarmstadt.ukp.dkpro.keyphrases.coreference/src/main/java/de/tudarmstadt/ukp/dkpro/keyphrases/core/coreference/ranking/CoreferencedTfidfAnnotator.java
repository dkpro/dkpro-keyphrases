package de.tudarmstadt.ukp.dkpro.keyphrases.core.coreference.ranking;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceChain;
import de.tudarmstadt.ukp.dkpro.core.api.coref.type.CoreferenceLink;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathFactory;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.TfidfAnnotator;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.FreqDist;
import de.tudarmstadt.ukp.dkpro.core.frequency.tfidf.util.TermIterator;

public class CoreferencedTfidfAnnotator extends TfidfAnnotator {

	@Override
	protected FreqDist<String> getTermFrequencies(JCas jcas)
			throws AnalysisEngineProcessException {
		// count all terms with the given annotation
		FreqDist<String> termFrequencies = super.getTermFrequencies(jcas);
				
		
		for(CoreferenceChain chain : JCasUtil.select(jcas, CoreferenceChain.class)){
			//TODO get lemma or token or whatever is needed
			String term = chain.getFirst().getCoveredText();
			
			termFrequencies.count(term, getChainLength(chain));
		}
		return termFrequencies;
	}

	int getChainLength(CoreferenceChain chain) {
		int counter = 1;
		CoreferenceLink link =chain.getFirst();
		while((link = link.getNext()) != null){
			counter++;
		}
		return counter;
	}
}

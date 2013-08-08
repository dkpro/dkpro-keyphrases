package de.tudarmstadt.ukp.dkpro.keyphrases.core.ranking;

import static org.apache.uima.fit.util.JCasUtil.select;
import static org.apache.uima.fit.util.JCasUtil.selectCovered;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;

import de.tudarmstadt.ukp.dkpro.core.api.frequency.tfidf.type.Tfidf;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;


/**
 * Ranks keyphrase annotations using the tfidf value of the keyphrase as the keyphrase's score.
 * If a keyphrase consists of more than one token, it is unclear which tfidf value to use.
 * The TfidfKeyphraseRanker provides a parameter that controls this behaviour.
 * It takes:
 * a) the maximum (default)
 * b) the average
 *
 * @author zesch
 *
 */
public class TfidfRanking extends JCasAnnotator_ImplBase {

    public enum TfidfAggregate {
        max,
        avg
    }

    public static final String PARAM_AGGREGATE = "AggregateFunction";
    @ConfigurationParameter(name=PARAM_AGGREGATE, mandatory=true, defaultValue="max")
    private TfidfAggregate aggregateFunction;

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        // TODO should check first, if there is a tf.idf value for the full candidate before aggregating over the components
        for (Keyphrase keyphrase : select(jcas, Keyphrase.class)) {
            List<Double> tfidfScores = new ArrayList<Double>();
            for (Tfidf tfidf : selectCovered(jcas, Tfidf.class, keyphrase)) {
                tfidfScores.add(tfidf.getTfidfValue());
            }

            keyphrase.setScore(
                    getAggregatedTfidfScore(tfidfScores, aggregateFunction)
            );
        }
    }

    private double getAggregatedTfidfScore(List<Double> scores, TfidfAggregate aggregate) {
        double aggregatedScore = 0.0;

        if (scores.size() == 0) {
            return aggregatedScore;
        }

        if (aggregate.equals(TfidfAggregate.max)) {
            aggregatedScore = getMax(scores);
        }
        else if (aggregate.equals(TfidfAggregate.avg)) {
            aggregatedScore = getAvg(scores);
        }

        return aggregatedScore;
    }

    private double getMax(List<Double> scores) {
        return Collections.max(scores);
    }

    private double getAvg(List<Double> scores) {
        double sum = 0.0;
        for (double score : scores) {
            sum += score;
        }
        double avg = sum / scores.size();
        return avg;
    }
}
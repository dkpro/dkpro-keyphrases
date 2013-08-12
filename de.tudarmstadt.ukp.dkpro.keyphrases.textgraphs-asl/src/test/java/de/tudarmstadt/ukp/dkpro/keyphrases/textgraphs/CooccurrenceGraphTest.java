package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitive;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair;
import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.FilteredToken;

public
class CooccurrenceGraphTest {

    @Test
    public void cooccurrenceGraphTest() throws Exception {
        String testDocument = "example sentence funny. second example.";

        Set<String> expectedValues = new HashSet<String>();
        expectedValues.add("example");
        expectedValues.add("sentence");
        expectedValues.add("funny");
        expectedValues.add("second");

    	AnalysisEngine analysisEngine = createPrimitive(
    	        CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Token.class.getName()+"/");

        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (AnnotationPair ap : JCasUtil.select(jcas, AnnotationPair.class)) {
            assertTrue(ap.getStringRepresentation1(), expectedValues.contains(ap.getStringRepresentation1()));
            assertTrue(ap.getStringRepresentation2(), expectedValues.contains(ap.getStringRepresentation2()));
            System.out.println(ap);
            i++;
        }
        assertEquals(4,i);
    }

    @Test
    public
    void cooccurrenceGraphTest2()
    throws Exception
    {
        String testDocument = "example sentence funny. second example.";

        Set<String> expectedValues = new HashSet<String>();
        expectedValues.add("examp");
        expectedValues.add("sent");
        expectedValues.add("fu");
        expectedValues.add("sec");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Stem.class.getName()+"/value",
                CooccurrenceGraph.PARAM_WINDOW_SIZE, 3
        );

        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (AnnotationPair ap : JCasUtil.select(jcas, AnnotationPair.class)) {
            assertTrue(ap.getStringRepresentation1(), expectedValues.contains(ap.getStringRepresentation1()));
            assertTrue(ap.getStringRepresentation2(), expectedValues.contains(ap.getStringRepresentation2()));
            System.out.println(ap);
            i++;
        }
        assertEquals(6,i);
    }

    @Test
    public
    void cooccurrenceGraphTest3()
    throws Exception
    {
        // stems based on FilteredToken
        String testDocument = "example sentence funny. second example.";

        Set<String> expectedValues = new HashSet<String>();
        expectedValues.add("exampl");
        expectedValues.add("sentenc");
        expectedValues.add("fu");
        expectedValues.add("secon");

        AnalysisEngine analysisEngine = AnalysisEngineFactory.createPrimitive(
                CooccurrenceGraph.class,
                CooccurrenceGraph.PARAM_FEATURE_PATH, Lemma.class.getName()+"/value"
        );

        JCas jcas = setup(testDocument, analysisEngine);

        analysisEngine.process(jcas);

        int i=0;
        for (AnnotationPair ap : JCasUtil.select(jcas, AnnotationPair.class)) {
            assertTrue(ap.getStringRepresentation1(), expectedValues.contains(ap.getStringRepresentation1()));
            assertTrue(ap.getStringRepresentation2(), expectedValues.contains(ap.getStringRepresentation2()));
            System.out.println(ap);
            i++;
        }
        assertEquals(4,i);
    }

	private JCas setup(String testDocument, AnalysisEngine analysisEngine)
		throws ResourceInitializationException
	{
        JCas jcas = analysisEngine.newJCas();
        jcas.setDocumentText(testDocument);

        Sentence sent1 = new Sentence(jcas, 0, 22);
        sent1.addToIndexes();

        Sentence sent2 = new Sentence(jcas, 24, 38);
        sent2.addToIndexes();

        FilteredToken t1 = new FilteredToken(jcas, 0, 7);
        t1.addToIndexes();
        assertEquals("example", t1.getCoveredText());

        FilteredToken t2 = new FilteredToken(jcas, 8, 16);
        t2.addToIndexes();
        assertEquals("sentence", t2.getCoveredText());

        FilteredToken t3 = new FilteredToken(jcas, 17, 22);
        t3.addToIndexes();
        assertEquals("funny", t3.getCoveredText());

        FilteredToken t4 = new FilteredToken(jcas, 24, 30);
        t4.addToIndexes();
        assertEquals("second", t4.getCoveredText());

        FilteredToken t5 = new FilteredToken(jcas, 31, 38);
        t5.addToIndexes();
        assertEquals("example", t5.getCoveredText());

        Stem s1 = new Stem(jcas, 0, 7);
        s1.setValue("examp");
        s1.addToIndexes();
        assertEquals("example", s1.getCoveredText());

        Stem s2 = new Stem(jcas, 8, 16);
        s2.setValue("sent");
        s2.addToIndexes();
        assertEquals("sentence", s2.getCoveredText());

        Stem s3 = new Stem(jcas, 17, 22);
        s3.setValue("fu");
        s3.addToIndexes();
        assertEquals("funny", s3.getCoveredText());

        Stem s4 = new Stem(jcas, 24, 30);
        s4.setValue("sec");
        s4.addToIndexes();
        assertEquals("second", s4.getCoveredText());

        Stem s5 = new Stem(jcas, 31, 38);
        s5.setValue("examp");
        s5.addToIndexes();
        assertEquals("example", s5.getCoveredText());

        Lemma l1 = new Lemma(jcas, 0, 7);
        l1.setValue("exampl");
        l1.addToIndexes();
        assertEquals("example", l1.getCoveredText());

        Lemma l2 = new Lemma(jcas, 8, 16);
        l2.setValue("sentenc");
        l2.addToIndexes();
        assertEquals("sentence", l2.getCoveredText());

        Lemma l3 = new Lemma(jcas, 17, 22);
        l3.setValue("fu");
        l3.addToIndexes();
        assertEquals("funny", l3.getCoveredText());

        Lemma l4 = new Lemma(jcas, 24, 30);
        l4.setValue("secon");
        l4.addToIndexes();
        assertEquals("second", l4.getCoveredText());

        Lemma l5 = new Lemma(jcas, 31, 38);
        l5.setValue("exampl");
        l5.addToIndexes();
        assertEquals("example", l5.getCoveredText());

        return jcas;
    }
}
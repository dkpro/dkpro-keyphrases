package de.tudarmstadt.ukp.dkpro.keyphrases.core.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate;

public class CorpusFilter
    extends JCasAnnotator_ImplBase
{

    public static final String CORPUS_FOLDER = "corpusFolder";
    @ConfigurationParameter(name = CORPUS_FOLDER, mandatory = true)
    private File corpusFolder;

    public static final String FILE_EXTENSION = "fileExtension";
    @ConfigurationParameter(name = FILE_EXTENSION, mandatory = true, defaultValue = "txt")
    private String fileExtension;
    private Set<String> tokensSet = new HashSet<String>();

    @Override
    public void initialize(final UimaContext context)
        throws ResourceInitializationException
    {
        super.initialize(context);

        try {
            for (File file : corpusFolder.listFiles(new FileFilter()
            {

                @Override
                public boolean accept(File file)
                {
                    if (file.getName().endsWith("." + fileExtension)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            })) {
                for (String line : FileUtils.readLines(file)) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    while (tokenizer.hasMoreElements()) {
                        tokensSet.add(tokenizer.nextToken().toLowerCase());
                    }
                }
            }
        }
        catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
    }

    @Override
    public void process(JCas aJCas)
        throws AnalysisEngineProcessException
    {
        List<KeyphraseCandidate> candidatesToBeRemoved = new LinkedList<KeyphraseCandidate>();
        for (KeyphraseCandidate keyphraseCandidate : JCasUtil.select(aJCas,
                KeyphraseCandidate.class)) {
            if (!tokensSet.contains(keyphraseCandidate.getCoveredText().toLowerCase())) {
                candidatesToBeRemoved.add(keyphraseCandidate);
            }
        }
        for (KeyphraseCandidate keyphraseCandidate : candidatesToBeRemoved) {
            keyphraseCandidate.removeFromIndexes(aJCas);
        }
    }

}

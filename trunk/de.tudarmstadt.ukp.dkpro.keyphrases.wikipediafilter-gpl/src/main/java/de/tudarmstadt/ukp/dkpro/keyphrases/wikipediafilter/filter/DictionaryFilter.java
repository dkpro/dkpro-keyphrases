/*******************************************************************************
 * Copyright 2013

 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl-3.0.txt
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.wikipediafilter.filter;

import static de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils.resolveLocation;
import static org.apache.uima.util.Level.FINE;
import static org.apache.uima.util.Level.INFO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Logger;
import org.apache.uima.cas.Type;

import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathException;
import de.tudarmstadt.ukp.dkpro.core.api.featurepath.FeaturePathInfo;
import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters;
import de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase;

/**
 * Remove all of the specified types from the CAS if their covered text is not in the list of articles.
 *
 * @author Nicolai Erbs, Richard Eckart de Castilho
 */
public class DictionaryFilter extends JCasAnnotator_ImplBase
{
    /**
     * A list of URLs from which to load the stop word lists. If an URL is prefixed with a language
     * code in square brackets, the stop word list is only used for documents in that language.
     * Using no prefix or the prefix "[*]" causes the list to be used for every document.
     * Example: "[de]classpath:/wikipedia/de_articles.txt"
     */
    public static final String PARAM_DICTIONARY_FILE_NAMES = ComponentParameters.PARAM_MODEL_LOCATION;
    @ConfigurationParameter(name = PARAM_DICTIONARY_FILE_NAMES, mandatory = true)
    private Set<String> dictionaryFileNames;

    /**
     * Feature paths for annotations that should be matched/removed. The default is
     *
     * <pre>
     * Keyphrase.class.getName()
     * </pre>
     */
    public static final String PARAM_PATHS = "Paths";
    @ConfigurationParameter(name = PARAM_PATHS, mandatory = false)
    private Set<String> paths;

    public static final String PARAM_LOWERCASE = "Lowercase";
    @ConfigurationParameter(name = PARAM_LOWERCASE, mandatory = false, defaultValue="true")
    private boolean lowercase;

    private Map<String, Set<String>> dictionarySets;

    @Override
    public void initialize(UimaContext context)
            throws ResourceInitializationException
            {
        super.initialize(context);

        // Set default paths. This cannot be done in the annotation because we cannot call
        // methods there.
        if (paths == null || paths.size() == 0) {
            paths = new HashSet<String>();
            paths.add(Keyphrase.class.getName());
        }

        try {
            dictionarySets = new HashMap<String, Set<String>>();
            for (String articleFileName : dictionaryFileNames) {
                String fileLocale = "*";
                // Check if a locale is defined for the file
                if (articleFileName.startsWith("[")) {
                    fileLocale = articleFileName.substring(1, articleFileName.indexOf(']'));
                    articleFileName = articleFileName.substring(articleFileName.indexOf(']')+1);
                }

                // Fetch the set for the specified locale
                Set<String> set = dictionarySets.get(fileLocale);
                if (set == null) {
                    set = new HashSet<String>();
                    dictionarySets.put(fileLocale, set);
                }

                // Load the set
                URL source = resolveLocation(articleFileName, this, context);
                for(String line : IOUtils.readLines(source.openStream())){
                    set.add(lowercase ? line.toLowerCase(new Locale(fileLocale)) : line);
                }

                context.getLogger().log(INFO,
                        "Loaded dicionary for locale [" + fileLocale + "] from [" + source + "]");
            }
        }
        catch (IOException e1) {
            throw new ResourceInitializationException(e1);
        }
            }

    @Override
    public void process(JCas jcas)
            throws AnalysisEngineProcessException
            {
        try{
            Logger log = getContext().getLogger();

            Locale casLocale = new Locale(jcas.getDocumentLanguage());
            Set<String> anyLocaleSet = dictionarySets.get("*");
            Set<String> casLocaleSet = dictionarySets.get(jcas.getDocumentLanguage());
            
            // Now really to the removal part
            FeaturePathInfo fp = new FeaturePathInfo();
            for (String path : paths) {

                // Initialize list of annotations to remove
                List<AnnotationFS> toRemove = new ArrayList<AnnotationFS>();

                // Separate Typename and featurepath
                String[] segments = path.split("/", 2);

                String typeName = segments[0];
                Type t = jcas.getTypeSystem().getType(typeName);
                if (t == null) {
                    throw new IllegalStateException("Type [" + typeName + "] not found in type system");
                }

                // initialize the FeaturePathInfo with the corresponding part
                if (segments.length > 1) {
                    fp.initialize(segments[1]);
                }
                else {
                    fp.initialize("");
                }

                Iterator<Annotation> i = jcas.getAnnotationIndex(t).iterator();
                while (i.hasNext()) {
                    Annotation anno = i.next();

                    String candidate = lowercase ? fp.getValue(anno).toLowerCase(casLocale) : fp.getValue(anno); 
                    if (((anyLocaleSet != null) && !anyLocaleSet.contains(candidate))
                            || ((casLocaleSet != null) && !casLocaleSet.contains(candidate))) {
                        // Remove the annotations that are not contained in dictionary
                        toRemove.add(anno);
                        if (log.isLoggable(FINE)) {
                            log.log(FINE, "Removing ["
                                    + typeName.substring(typeName.lastIndexOf('.') + 1)
                                    + "] filtered out [" + anno.getCoveredText() + "]@"
                                    + anno.getBegin() + ".." + anno.getEnd());
                        }
                    }
                }

                // Remove from the CAS
                for (AnnotationFS anno : toRemove) {
                    jcas.removeFsFromIndexes(anno);
                }
            }
        }
        catch (FeaturePathException e) {
            throw new AnalysisEngineProcessException(e);
        }
            }
}
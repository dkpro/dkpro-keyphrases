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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.uima.UimaContext;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.component.JCasCollectionReader_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;

/**
 * Reads gold standard keyphrase documents.
 *
 * <ul>
 * <li><code>InputDirectory</code> - path to directory containing the keyphrase test documents</li>
 * <li><code>DataSuffix</code> - The suffix of files containing keyphrase data.</li>
 * <li><code>Language</code> (optional) - language of the input documents</li>
 * <li><code>Encoding</code> (optional) - character encoding of the input files</li>
 * </ul>
 *
 */
public class KeyphraseReader extends JCasCollectionReader_ImplBase {

    private static final String DEFAULT_LANGUAGE = "en";

    public static final String PARAM_INPUTDIR = "InputDirectory";
    @ConfigurationParameter(name=PARAM_INPUTDIR, mandatory=true)
    private String mInputDirectory;

    public static final String PARAM_DATA_SUFFIX = "DataSuffix";
    @ConfigurationParameter(name=PARAM_DATA_SUFFIX, mandatory=true)
    private String mDataSuffix;

    public static final String PARAM_LANGUAGE = "Language";
    @ConfigurationParameter(name=PARAM_LANGUAGE, mandatory=false, defaultValue=DEFAULT_LANGUAGE)
    private String mLanguage;

    public static final String PARAM_ENCODING = "Encoding";
    @ConfigurationParameter(name=PARAM_ENCODING, mandatory=false, defaultValue="UTF-8")
    private String mEncoding;

    private Map<String,File> mDataFiles;
    private int mCurrentIndex;

    private Iterator<String> filenameIterator;

    @Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
        File directory = new File(mInputDirectory.trim());

        // if input directory does not exist or is not a directory, throw exception
        if (!directory.exists() || !directory.isDirectory()) {
            throw new ResourceInitializationException(ResourceConfigurationException.DIRECTORY_NOT_FOUND, new Object[] { PARAM_INPUTDIR,
                    this.getMetaData().getName(), directory.getPath() });
        }

        // get list of files (not subdirectories) in the specified directory
        mInputDirectory = directory.toURI().toString();
        mDataFiles = new TreeMap<String,File>();
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isDirectory()) {
                String filename = files[i].getName();
                // get suffix
                String[] parts = filename.split("\\.");
                if (parts.length < 1) {
                    continue;
                }
                String suffix = parts[parts.length-1];
                String nameWithoutSuffix = filename.substring(0, filename.length() - suffix.length());

                if (suffix.equals(mDataSuffix)) {
                    mDataFiles.put(nameWithoutSuffix, files[i]);
                }
            }
        }

        mCurrentIndex = 0;
        filenameIterator = mDataFiles.keySet().iterator();
    }

    @Override
    public boolean hasNext() {
        return filenameIterator.hasNext();
    }

    @Override
    public void getNext(JCas jcas) throws IOException, CollectionException {

        String filename = filenameIterator.next();
        File dataFile = mDataFiles.get(filename);
        getUimaContext().getLogger().log(Level.INFO, "Processing file " + dataFile.getName());
        InputStream is = new FileInputStream(dataFile);
        {
            byte[] contents = new byte[(int) dataFile.length()];
            is.read(contents);
            String text;
            if (mEncoding != null) {
                text = new String(contents, mEncoding);
            } else {
                text = new String(contents);
            }
            text = text.replaceAll(System.getProperty("line.separator"), " ");
            text = text.replaceAll("\\s{2,}"," ");
            jcas.setDocumentText(text);
        }
        if (is != null) {
            is.close();

        }

        // set language if it was explicitly specified as a configuration parameter
        if (mLanguage != null) {
            jcas.setDocumentLanguage(mLanguage);
        }

        DocumentMetaData docMetaData = DocumentMetaData.create(jcas);
        docMetaData.setDocumentTitle(dataFile.getName());
        docMetaData.setDocumentUri(dataFile.toURI().toString());
        docMetaData.setDocumentId(new Integer(mCurrentIndex).toString());
        docMetaData.setCollectionId(mInputDirectory);
        docMetaData.addToIndexes();

        mCurrentIndex++;
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public Progress[] getProgress() {
        return new Progress[] { new ProgressImpl(mCurrentIndex, mDataFiles.size(), Progress.ENTITIES) };
    }
}
package de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createPrimitiveDescription;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.keyphrases.core.wrapper.kea.KeaKeyphraseExtractorWrapper;

public class KeaExtractor extends KeyphraseExtractor_ImplBase {

	private String keaProperties = "src/main/resources/Kea/models/kea_en.properties";

	private String keaModel = "src/main/resources/Kea/models/kea_en.model";

	/**
	 * Set to something reasonable high, as we limit the number of keyphrases later.
	 */
	private final int MAX_KEYPHRASES = 100000;

	@Override
	protected AnalysisEngineDescription createKeyphraseExtractorAggregate()
			throws ResourceInitializationException {

		return createAggregateDescription(

		        createPreprocessingComponents(getCandidate().getType()),

                createPrimitiveDescription(KeaKeyphraseExtractorWrapper.class,
                        KeaKeyphraseExtractorWrapper.PARAM_KEA_PROPERTIES, getKeaProperties(),
                        KeaKeyphraseExtractorWrapper.PARAM_LANG, getLanguage(),
                        KeaKeyphraseExtractorWrapper.PARAM_MAX_PHRASE_LENGTH, getMaxKeyphraseLength(),
                        KeaKeyphraseExtractorWrapper.PARAM_MODEL, getKeaModel(),
                        KeaKeyphraseExtractorWrapper.PARAM_NUMBER_PHRASES, MAX_KEYPHRASES),

                createPostprocessingComponents()
		);
	}

	public String getKeaProperties() {
		return keaProperties;
	}

	public void setKeaProperties(String keaProperties) {
		this.keaProperties = keaProperties;
	}

	public String getKeaModel() {
		return keaModel;
	}

	public void setKeaModel(String keaModel) {
		this.keaModel = keaModel;
	}

	@Override
    public String getConfigurationDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getConfigurationDetails());
		sb.append("Kea Properties: " + this.getKeaProperties()); sb.append(LF);
		sb.append("Kea Model: " + this.getKeaModel()); sb.append(LF);
		sb.append(LF);
		return sb.toString();
	}
}

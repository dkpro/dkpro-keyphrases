
/* First created by JCasGen Thu Apr 18 17:45:56 CEST 2013 */
package de.tudarmstadt.ukp.dkpro.keyphrases.core.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Thu Apr 18 17:45:56 CEST 2013
 * @generated */
public class KeyphraseCandidate_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (KeyphraseCandidate_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = KeyphraseCandidate_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new KeyphraseCandidate(addr, KeyphraseCandidate_Type.this);
  			   KeyphraseCandidate_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new KeyphraseCandidate(addr, KeyphraseCandidate_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KeyphraseCandidate.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate");
 
  /** @generated */
  final Feature casFeat_keyphrase;
  /** @generated */
  final int     casFeatCode_keyphrase;
  /** @generated */ 
  public String getKeyphrase(int addr) {
        if (featOkTst && casFeat_keyphrase == null)
      jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate");
    return ll_cas.ll_getStringValue(addr, casFeatCode_keyphrase);
  }
  /** @generated */    
  public void setKeyphrase(int addr, String v) {
        if (featOkTst && casFeat_keyphrase == null)
      jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate");
    ll_cas.ll_setStringValue(addr, casFeatCode_keyphrase, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public KeyphraseCandidate_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_keyphrase = jcas.getRequiredFeatureDE(casType, "keyphrase", "uima.cas.String", featOkTst);
    casFeatCode_keyphrase  = (null == casFeat_keyphrase) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_keyphrase).getCode();

  }
}



    
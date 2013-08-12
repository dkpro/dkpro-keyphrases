
/* First created by JCasGen Thu Apr 18 17:46:10 CEST 2013 */
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
import org.apache.uima.jcas.tcas.DocumentAnnotation_Type;

/** 
 * Updated by JCasGen Thu Apr 18 17:46:10 CEST 2013
 * @generated */
public class KeyphraseMetaData_Type extends DocumentAnnotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (KeyphraseMetaData_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = KeyphraseMetaData_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new KeyphraseMetaData(addr, KeyphraseMetaData_Type.this);
  			   KeyphraseMetaData_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new KeyphraseMetaData(addr, KeyphraseMetaData_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KeyphraseMetaData.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
 
  /** @generated */
  final Feature casFeat_goldstandardKeyphrases;
  /** @generated */
  final int     casFeatCode_goldstandardKeyphrases;
  /** @generated */ 
  public int getGoldstandardKeyphrases(int addr) {
        if (featOkTst && casFeat_goldstandardKeyphrases == null)
      jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return ll_cas.ll_getRefValue(addr, casFeatCode_goldstandardKeyphrases);
  }
  /** @generated */    
  public void setGoldstandardKeyphrases(int addr, int v) {
        if (featOkTst && casFeat_goldstandardKeyphrases == null)
      jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    ll_cas.ll_setRefValue(addr, casFeatCode_goldstandardKeyphrases, v);}
    
  
 
  /** @generated */
  final Feature casFeat_nrofKeyphrases;
  /** @generated */
  final int     casFeatCode_nrofKeyphrases;
  /** @generated */ 
  public int getNrofKeyphrases(int addr) {
        if (featOkTst && casFeat_nrofKeyphrases == null)
      jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return ll_cas.ll_getIntValue(addr, casFeatCode_nrofKeyphrases);
  }
  /** @generated */    
  public void setNrofKeyphrases(int addr, int v) {
        if (featOkTst && casFeat_nrofKeyphrases == null)
      jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    ll_cas.ll_setIntValue(addr, casFeatCode_nrofKeyphrases, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public KeyphraseMetaData_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_goldstandardKeyphrases = jcas.getRequiredFeatureDE(casType, "goldstandardKeyphrases", "uima.cas.NonEmptyStringList", featOkTst);
    casFeatCode_goldstandardKeyphrases  = (null == casFeat_goldstandardKeyphrases) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_goldstandardKeyphrases).getCode();

 
    casFeat_nrofKeyphrases = jcas.getRequiredFeatureDE(casType, "nrofKeyphrases", "uima.cas.Integer", featOkTst);
    casFeatCode_nrofKeyphrases  = (null == casFeat_nrofKeyphrases) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_nrofKeyphrases).getCode();

  }
}



    
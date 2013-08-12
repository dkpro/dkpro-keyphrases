
/* First created by JCasGen Wed May 22 14:36:41 CEST 2013 */
package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type;

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
 * Updated by JCasGen Wed May 22 14:36:41 CEST 2013
 * @generated */
public class Disambiguation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Disambiguation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Disambiguation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Disambiguation(addr, Disambiguation_Type.this);
  			   Disambiguation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Disambiguation(addr, Disambiguation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Disambiguation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
 
  /** @generated */
  final Feature casFeat_MajorSense;
  /** @generated */
  final int     casFeatCode_MajorSense;
  /** @generated */ 
  public int getMajorSense(int addr) {
        if (featOkTst && casFeat_MajorSense == null)
      jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_MajorSense);
  }
  /** @generated */    
  public void setMajorSense(int addr, int v) {
        if (featOkTst && casFeat_MajorSense == null)
      jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setRefValue(addr, casFeatCode_MajorSense, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Senses;
  /** @generated */
  final int     casFeatCode_Senses;
  /** @generated */ 
  public int getSenses(int addr) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Senses);
  }
  /** @generated */    
  public void setSenses(int addr, int v) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setRefValue(addr, casFeatCode_Senses, v);}
    
   /** @generated */
  public int getSenses(int addr, int i) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i);
  }
   
  /** @generated */ 
  public void setSenses(int addr, int i, int v) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i, v);
  }
 
 
  /** @generated */
  final Feature casFeat_SourceId;
  /** @generated */
  final int     casFeatCode_SourceId;
  /** @generated */ 
  public long getSourceId(int addr) {
        if (featOkTst && casFeat_SourceId == null)
      jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_SourceId);
  }
  /** @generated */    
  public void setSourceId(int addr, long v) {
        if (featOkTst && casFeat_SourceId == null)
      jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setLongValue(addr, casFeatCode_SourceId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_SourceName;
  /** @generated */
  final int     casFeatCode_SourceName;
  /** @generated */ 
  public String getSourceName(int addr) {
        if (featOkTst && casFeat_SourceName == null)
      jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_SourceName);
  }
  /** @generated */    
  public void setSourceName(int addr, String v) {
        if (featOkTst && casFeat_SourceName == null)
      jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setStringValue(addr, casFeatCode_SourceName, v);}
    
  
 
  /** @generated */
  final Feature casFeat_AnchorId;
  /** @generated */
  final int     casFeatCode_AnchorId;
  /** @generated */ 
  public long getAnchorId(int addr) {
        if (featOkTst && casFeat_AnchorId == null)
      jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_AnchorId);
  }
  /** @generated */    
  public void setAnchorId(int addr, long v) {
        if (featOkTst && casFeat_AnchorId == null)
      jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setLongValue(addr, casFeatCode_AnchorId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_AnchorName;
  /** @generated */
  final int     casFeatCode_AnchorName;
  /** @generated */ 
  public String getAnchorName(int addr) {
        if (featOkTst && casFeat_AnchorName == null)
      jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_AnchorName);
  }
  /** @generated */    
  public void setAnchorName(int addr, String v) {
        if (featOkTst && casFeat_AnchorName == null)
      jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setStringValue(addr, casFeatCode_AnchorName, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public Disambiguation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_MajorSense = jcas.getRequiredFeatureDE(casType, "MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense", featOkTst);
    casFeatCode_MajorSense  = (null == casFeat_MajorSense) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_MajorSense).getCode();

 
    casFeat_Senses = jcas.getRequiredFeatureDE(casType, "Senses", "uima.cas.FSArray", featOkTst);
    casFeatCode_Senses  = (null == casFeat_Senses) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Senses).getCode();

 
    casFeat_SourceId = jcas.getRequiredFeatureDE(casType, "SourceId", "uima.cas.Long", featOkTst);
    casFeatCode_SourceId  = (null == casFeat_SourceId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_SourceId).getCode();

 
    casFeat_SourceName = jcas.getRequiredFeatureDE(casType, "SourceName", "uima.cas.String", featOkTst);
    casFeatCode_SourceName  = (null == casFeat_SourceName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_SourceName).getCode();

 
    casFeat_AnchorId = jcas.getRequiredFeatureDE(casType, "AnchorId", "uima.cas.Long", featOkTst);
    casFeatCode_AnchorId  = (null == casFeat_AnchorId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AnchorId).getCode();

 
    casFeat_AnchorName = jcas.getRequiredFeatureDE(casType, "AnchorName", "uima.cas.String", featOkTst);
    casFeatCode_AnchorName  = (null == casFeat_AnchorName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AnchorName).getCode();

  }
}



    
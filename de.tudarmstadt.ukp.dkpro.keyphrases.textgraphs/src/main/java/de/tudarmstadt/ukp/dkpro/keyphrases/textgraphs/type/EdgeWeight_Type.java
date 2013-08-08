
/* First created by JCasGen Wed May 22 14:44:24 CEST 2013 */
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
 * Updated by JCasGen Wed May 22 14:44:24 CEST 2013
 * @generated */
public class EdgeWeight_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (EdgeWeight_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = EdgeWeight_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new EdgeWeight(addr, EdgeWeight_Type.this);
  			   EdgeWeight_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new EdgeWeight(addr, EdgeWeight_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = EdgeWeight.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
 
  /** @generated */
  final Feature casFeat_MeasureType;
  /** @generated */
  final int     casFeatCode_MeasureType;
  /** @generated */ 
  public String getMeasureType(int addr) {
        if (featOkTst && casFeat_MeasureType == null)
      jcas.throwFeatMissing("MeasureType", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getStringValue(addr, casFeatCode_MeasureType);
  }
  /** @generated */    
  public void setMeasureType(int addr, String v) {
        if (featOkTst && casFeat_MeasureType == null)
      jcas.throwFeatMissing("MeasureType", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setStringValue(addr, casFeatCode_MeasureType, v);}
    
  
 
  /** @generated */
  final Feature casFeat_RelatednessValue;
  /** @generated */
  final int     casFeatCode_RelatednessValue;
  /** @generated */ 
  public double getRelatednessValue(int addr) {
        if (featOkTst && casFeat_RelatednessValue == null)
      jcas.throwFeatMissing("RelatednessValue", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_RelatednessValue);
  }
  /** @generated */    
  public void setRelatednessValue(int addr, double v) {
        if (featOkTst && casFeat_RelatednessValue == null)
      jcas.throwFeatMissing("RelatednessValue", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_RelatednessValue, v);}
    
  
 
  /** @generated */
  final Feature casFeat_MeasureName;
  /** @generated */
  final int     casFeatCode_MeasureName;
  /** @generated */ 
  public String getMeasureName(int addr) {
        if (featOkTst && casFeat_MeasureName == null)
      jcas.throwFeatMissing("MeasureName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getStringValue(addr, casFeatCode_MeasureName);
  }
  /** @generated */    
  public void setMeasureName(int addr, String v) {
        if (featOkTst && casFeat_MeasureName == null)
      jcas.throwFeatMissing("MeasureName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setStringValue(addr, casFeatCode_MeasureName, v);}
    
  
 
  /** @generated */
  final Feature casFeat_WordPair;
  /** @generated */
  final int     casFeatCode_WordPair;
  /** @generated */ 
  public int getWordPair(int addr) {
        if (featOkTst && casFeat_WordPair == null)
      jcas.throwFeatMissing("WordPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getRefValue(addr, casFeatCode_WordPair);
  }
  /** @generated */    
  public void setWordPair(int addr, int v) {
        if (featOkTst && casFeat_WordPair == null)
      jcas.throwFeatMissing("WordPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setRefValue(addr, casFeatCode_WordPair, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Term1;
  /** @generated */
  final int     casFeatCode_Term1;
  /** @generated */ 
  public String getTerm1(int addr) {
        if (featOkTst && casFeat_Term1 == null)
      jcas.throwFeatMissing("Term1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Term1);
  }
  /** @generated */    
  public void setTerm1(int addr, String v) {
        if (featOkTst && casFeat_Term1 == null)
      jcas.throwFeatMissing("Term1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setStringValue(addr, casFeatCode_Term1, v);}
    
  
 
  /** @generated */
  final Feature casFeat_Term2;
  /** @generated */
  final int     casFeatCode_Term2;
  /** @generated */ 
  public String getTerm2(int addr) {
        if (featOkTst && casFeat_Term2 == null)
      jcas.throwFeatMissing("Term2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Term2);
  }
  /** @generated */    
  public void setTerm2(int addr, String v) {
        if (featOkTst && casFeat_Term2 == null)
      jcas.throwFeatMissing("Term2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setStringValue(addr, casFeatCode_Term2, v);}
    
  
 
  /** @generated */
  final Feature casFeat_AnnotationPair;
  /** @generated */
  final int     casFeatCode_AnnotationPair;
  /** @generated */ 
  public int getAnnotationPair(int addr) {
        if (featOkTst && casFeat_AnnotationPair == null)
      jcas.throwFeatMissing("AnnotationPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return ll_cas.ll_getRefValue(addr, casFeatCode_AnnotationPair);
  }
  /** @generated */    
  public void setAnnotationPair(int addr, int v) {
        if (featOkTst && casFeat_AnnotationPair == null)
      jcas.throwFeatMissing("AnnotationPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    ll_cas.ll_setRefValue(addr, casFeatCode_AnnotationPair, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public EdgeWeight_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_MeasureType = jcas.getRequiredFeatureDE(casType, "MeasureType", "uima.cas.String", featOkTst);
    casFeatCode_MeasureType  = (null == casFeat_MeasureType) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_MeasureType).getCode();

 
    casFeat_RelatednessValue = jcas.getRequiredFeatureDE(casType, "RelatednessValue", "uima.cas.Double", featOkTst);
    casFeatCode_RelatednessValue  = (null == casFeat_RelatednessValue) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_RelatednessValue).getCode();

 
    casFeat_MeasureName = jcas.getRequiredFeatureDE(casType, "MeasureName", "uima.cas.String", featOkTst);
    casFeatCode_MeasureName  = (null == casFeat_MeasureName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_MeasureName).getCode();

 
    casFeat_WordPair = jcas.getRequiredFeatureDE(casType, "WordPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WordPair", featOkTst);
    casFeatCode_WordPair  = (null == casFeat_WordPair) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_WordPair).getCode();

 
    casFeat_Term1 = jcas.getRequiredFeatureDE(casType, "Term1", "uima.cas.String", featOkTst);
    casFeatCode_Term1  = (null == casFeat_Term1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Term1).getCode();

 
    casFeat_Term2 = jcas.getRequiredFeatureDE(casType, "Term2", "uima.cas.String", featOkTst);
    casFeatCode_Term2  = (null == casFeat_Term2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Term2).getCode();

 
    casFeat_AnnotationPair = jcas.getRequiredFeatureDE(casType, "AnnotationPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair", featOkTst);
    casFeatCode_AnnotationPair  = (null == casFeat_AnnotationPair) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_AnnotationPair).getCode();

  }
}



    
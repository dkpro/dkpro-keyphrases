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

public class AnnotationPair_Type extends Annotation_Type {

  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}

  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (AnnotationPair_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = AnnotationPair_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new AnnotationPair(addr, AnnotationPair_Type.this);
  			   AnnotationPair_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new AnnotationPair(addr, AnnotationPair_Type.this);
  	  }
    };

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = AnnotationPair.typeIndexID;
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
 

  final Feature casFeat_Annotation1;

  final int     casFeatCode_Annotation1;
 
  public int getAnnotation1(int addr) {
        if (featOkTst && casFeat_Annotation1 == null)
      jcas.throwFeatMissing("Annotation1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Annotation1);
  }
    
  public void setAnnotation1(int addr, int v) {
        if (featOkTst && casFeat_Annotation1 == null)
      jcas.throwFeatMissing("Annotation1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    ll_cas.ll_setRefValue(addr, casFeatCode_Annotation1, v);}
    
  
 

  final Feature casFeat_Annotation2;

  final int     casFeatCode_Annotation2;
 
  public int getAnnotation2(int addr) {
        if (featOkTst && casFeat_Annotation2 == null)
      jcas.throwFeatMissing("Annotation2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Annotation2);
  }
    
  public void setAnnotation2(int addr, int v) {
        if (featOkTst && casFeat_Annotation2 == null)
      jcas.throwFeatMissing("Annotation2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    ll_cas.ll_setRefValue(addr, casFeatCode_Annotation2, v);}
    
  
 

  final Feature casFeat_StringRepresentation1;

  final int     casFeatCode_StringRepresentation1;
  
  public String getStringRepresentation1(int addr) {
        if (featOkTst && casFeat_StringRepresentation1 == null)
      jcas.throwFeatMissing("StringRepresentation1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    return ll_cas.ll_getStringValue(addr, casFeatCode_StringRepresentation1);
  }
    
  public void setStringRepresentation1(int addr, String v) {
        if (featOkTst && casFeat_StringRepresentation1 == null)
      jcas.throwFeatMissing("StringRepresentation1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    ll_cas.ll_setStringValue(addr, casFeatCode_StringRepresentation1, v);}
    
  
 

  final Feature casFeat_StringRepresentation2;

  final int     casFeatCode_StringRepresentation2;

  public String getStringRepresentation2(int addr) {
        if (featOkTst && casFeat_StringRepresentation2 == null)
      jcas.throwFeatMissing("StringRepresentation2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    return ll_cas.ll_getStringValue(addr, casFeatCode_StringRepresentation2);
  }
    
  public void setStringRepresentation2(int addr, String v) {
        if (featOkTst && casFeat_StringRepresentation2 == null)
      jcas.throwFeatMissing("StringRepresentation2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.AnnotationPair");
    ll_cas.ll_setStringValue(addr, casFeatCode_StringRepresentation2, v);}
    
  




  public AnnotationPair_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Annotation1 = jcas.getRequiredFeatureDE(casType, "Annotation1", "uima.tcas.Annotation", featOkTst);
    casFeatCode_Annotation1  = (null == casFeat_Annotation1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Annotation1).getCode();

 
    casFeat_Annotation2 = jcas.getRequiredFeatureDE(casType, "Annotation2", "uima.tcas.Annotation", featOkTst);
    casFeatCode_Annotation2  = (null == casFeat_Annotation2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Annotation2).getCode();

 
    casFeat_StringRepresentation1 = jcas.getRequiredFeatureDE(casType, "StringRepresentation1", "uima.cas.String", featOkTst);
    casFeatCode_StringRepresentation1  = (null == casFeat_StringRepresentation1) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_StringRepresentation1).getCode();

 
    casFeat_StringRepresentation2 = jcas.getRequiredFeatureDE(casType, "StringRepresentation2", "uima.cas.String", featOkTst);
    casFeatCode_StringRepresentation2  = (null == casFeat_StringRepresentation2) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_StringRepresentation2).getCode();

  }
}



    
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

public class Disambiguation_Type extends Annotation_Type {

  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}

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

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Disambiguation.typeIndexID;
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
 
  final Feature casFeat_MajorSense;

  final int     casFeatCode_MajorSense;
  
  public int getMajorSense(int addr) {
        if (featOkTst && casFeat_MajorSense == null)
      jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_MajorSense);
  }

  public void setMajorSense(int addr, int v) {
        if (featOkTst && casFeat_MajorSense == null)
      jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setRefValue(addr, casFeatCode_MajorSense, v);}
    
  
  final Feature casFeat_Senses;

  final int     casFeatCode_Senses;
 
  public int getSenses(int addr) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getRefValue(addr, casFeatCode_Senses);
  }
    
  public void setSenses(int addr, int v) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setRefValue(addr, casFeatCode_Senses, v);}
    

  public int getSenses(int addr, int i) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i);
	return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i);
  }
   
 
  public void setSenses(int addr, int i, int v) {
        if (featOkTst && casFeat_Senses == null)
      jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_Senses), i, v);
  }
 
 

  final Feature casFeat_SourceId;

  final int     casFeatCode_SourceId;
  
  public long getSourceId(int addr) {
        if (featOkTst && casFeat_SourceId == null)
      jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_SourceId);
  }
    
  public void setSourceId(int addr, long v) {
        if (featOkTst && casFeat_SourceId == null)
      jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setLongValue(addr, casFeatCode_SourceId, v);}
    
  
 

  final Feature casFeat_SourceName;

  final int     casFeatCode_SourceName;
 
  public String getSourceName(int addr) {
        if (featOkTst && casFeat_SourceName == null)
      jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_SourceName);
  }
    
  public void setSourceName(int addr, String v) {
        if (featOkTst && casFeat_SourceName == null)
      jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setStringValue(addr, casFeatCode_SourceName, v);}
    
  
 

  final Feature casFeat_AnchorId;

  final int     casFeatCode_AnchorId;
 
  public long getAnchorId(int addr) {
        if (featOkTst && casFeat_AnchorId == null)
      jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getLongValue(addr, casFeatCode_AnchorId);
  }
    
  public void setAnchorId(int addr, long v) {
        if (featOkTst && casFeat_AnchorId == null)
      jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setLongValue(addr, casFeatCode_AnchorId, v);}
    
  
 

  final Feature casFeat_AnchorName;

  final int     casFeatCode_AnchorName;
 
  public String getAnchorName(int addr) {
        if (featOkTst && casFeat_AnchorName == null)
      jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_AnchorName);
  }
    
  public void setAnchorName(int addr, String v) {
        if (featOkTst && casFeat_AnchorName == null)
      jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    ll_cas.ll_setStringValue(addr, casFeatCode_AnchorName, v);}
    
  



  
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



    
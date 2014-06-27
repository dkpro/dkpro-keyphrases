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


public class Sense_Type extends Annotation_Type {

  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}

  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (Sense_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = Sense_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new Sense(addr, Sense_Type.this);
  			   Sense_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new Sense(addr, Sense_Type.this);
  	  }
    };

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = Sense.typeIndexID;
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
 

  final Feature casFeat_Id;

  final int     casFeatCode_Id;
 
  public long getId(int addr) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return ll_cas.ll_getLongValue(addr, casFeatCode_Id);
  }
    
  public void setId(int addr, long v) {
        if (featOkTst && casFeat_Id == null)
      jcas.throwFeatMissing("Id", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    ll_cas.ll_setLongValue(addr, casFeatCode_Id, v);}
    
  
 

  final Feature casFeat_Name;

  final int     casFeatCode_Name;
 
  public String getName(int addr) {
        if (featOkTst && casFeat_Name == null)
      jcas.throwFeatMissing("Name", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return ll_cas.ll_getStringValue(addr, casFeatCode_Name);
  }
    
  public void setName(int addr, String v) {
        if (featOkTst && casFeat_Name == null)
      jcas.throwFeatMissing("Name", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    ll_cas.ll_setStringValue(addr, casFeatCode_Name, v);}
    
  
 

  final Feature casFeat_Counter;

  final int     casFeatCode_Counter;
 
  public int getCounter(int addr) {
        if (featOkTst && casFeat_Counter == null)
      jcas.throwFeatMissing("Counter", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return ll_cas.ll_getIntValue(addr, casFeatCode_Counter);
  }
    
  public void setCounter(int addr, int v) {
        if (featOkTst && casFeat_Counter == null)
      jcas.throwFeatMissing("Counter", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    ll_cas.ll_setIntValue(addr, casFeatCode_Counter, v);}
    
  
 

  final Feature casFeat_Score;

  final int     casFeatCode_Score;
 
  public double getScore(int addr) {
        if (featOkTst && casFeat_Score == null)
      jcas.throwFeatMissing("Score", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_Score);
  }
    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_Score == null)
      jcas.throwFeatMissing("Score", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_Score, v);}
    
  
 

  final Feature casFeat_LinkedSenses;

  final int     casFeatCode_LinkedSenses;
 
  public int getLinkedSenses(int addr) {
        if (featOkTst && casFeat_LinkedSenses == null)
      jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses);
  }
    
  public void setLinkedSenses(int addr, int v) {
        if (featOkTst && casFeat_LinkedSenses == null)
      jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    ll_cas.ll_setRefValue(addr, casFeatCode_LinkedSenses, v);}
    

  public int getLinkedSenses(int addr, int i) {
        if (featOkTst && casFeat_LinkedSenses == null)
      jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    if (lowLevelTypeChecks)
      return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses), i, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses), i);
  return ll_cas.ll_getRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses), i);
  }
   
 
  public void setLinkedSenses(int addr, int i, int v) {
        if (featOkTst && casFeat_LinkedSenses == null)
      jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    if (lowLevelTypeChecks)
      ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses), i, v, true);
    jcas.checkArrayBounds(ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses), i);
    ll_cas.ll_setRefArrayValue(ll_cas.ll_getRefValue(addr, casFeatCode_LinkedSenses), i, v);
  }
 



  public Sense_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_Id = jcas.getRequiredFeatureDE(casType, "Id", "uima.cas.Long", featOkTst);
    casFeatCode_Id  = (null == casFeat_Id) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Id).getCode();

 
    casFeat_Name = jcas.getRequiredFeatureDE(casType, "Name", "uima.cas.String", featOkTst);
    casFeatCode_Name  = (null == casFeat_Name) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Name).getCode();

 
    casFeat_Counter = jcas.getRequiredFeatureDE(casType, "Counter", "uima.cas.Integer", featOkTst);
    casFeatCode_Counter  = (null == casFeat_Counter) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Counter).getCode();

 
    casFeat_Score = jcas.getRequiredFeatureDE(casType, "Score", "uima.cas.Double", featOkTst);
    casFeatCode_Score  = (null == casFeat_Score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Score).getCode();

 
    casFeat_LinkedSenses = jcas.getRequiredFeatureDE(casType, "LinkedSenses", "uima.cas.FSArray", featOkTst);
    casFeatCode_LinkedSenses  = (null == casFeat_LinkedSenses) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_LinkedSenses).getCode();

  }
}



    
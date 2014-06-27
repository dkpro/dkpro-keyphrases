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
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;



public class EdgeWeight extends Annotation {

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(EdgeWeight.class);

  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;

  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected EdgeWeight() {/* intentionally empty block */}
    
  public EdgeWeight(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  

  public EdgeWeight(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  
  public EdgeWeight(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: MeasureType

  public String getMeasureType() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_MeasureType == null)
      jcasType.jcas.throwFeatMissing("MeasureType", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return jcasType.ll_cas.ll_getStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_MeasureType);}
    
  public void setMeasureType(String v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_MeasureType == null)
      jcasType.jcas.throwFeatMissing("MeasureType", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_MeasureType, v);}    
   
    
  //*--------------*
  //* Feature: RelatednessValue


  public double getRelatednessValue() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_RelatednessValue == null)
      jcasType.jcas.throwFeatMissing("RelatednessValue", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_RelatednessValue);}
    
  public void setRelatednessValue(double v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_RelatednessValue == null)
      jcasType.jcas.throwFeatMissing("RelatednessValue", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_RelatednessValue, v);}    
   
    
  //*--------------*
  //* Feature: MeasureName

  public String getMeasureName() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_MeasureName == null)
      jcasType.jcas.throwFeatMissing("MeasureName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return jcasType.ll_cas.ll_getStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_MeasureName);}
    
  public void setMeasureName(String v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_MeasureName == null)
      jcasType.jcas.throwFeatMissing("MeasureName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_MeasureName, v);}    
   
    
  //*--------------*
  //* Feature: WordPair

  public WordPair getWordPair() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_WordPair == null)
      jcasType.jcas.throwFeatMissing("WordPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return (WordPair)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_WordPair)));}
    
  public void setWordPair(WordPair v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_WordPair == null)
      jcasType.jcas.throwFeatMissing("WordPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setRefValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_WordPair, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Term1

  public String getTerm1() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_Term1 == null)
      jcasType.jcas.throwFeatMissing("Term1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return jcasType.ll_cas.ll_getStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_Term1);}
    
  public void setTerm1(String v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_Term1 == null)
      jcasType.jcas.throwFeatMissing("Term1", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_Term1, v);}    
   
    
  //*--------------*
  //* Feature: Term2

  public String getTerm2() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_Term2 == null)
      jcasType.jcas.throwFeatMissing("Term2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return jcasType.ll_cas.ll_getStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_Term2);}
    
  public void setTerm2(String v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_Term2 == null)
      jcasType.jcas.throwFeatMissing("Term2", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setStringValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_Term2, v);}    
   
    
  //*--------------*
  //* Feature: AnnotationPair

  public AnnotationPair getAnnotationPair() {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_AnnotationPair == null)
      jcasType.jcas.throwFeatMissing("AnnotationPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    return (AnnotationPair)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_AnnotationPair)));}
    
  public void setAnnotationPair(AnnotationPair v) {
    if (EdgeWeight_Type.featOkTst && ((EdgeWeight_Type)jcasType).casFeat_AnnotationPair == null)
      jcasType.jcas.throwFeatMissing("AnnotationPair", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.EdgeWeight");
    jcasType.ll_cas.ll_setRefValue(addr, ((EdgeWeight_Type)jcasType).casFeatCode_AnnotationPair, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    
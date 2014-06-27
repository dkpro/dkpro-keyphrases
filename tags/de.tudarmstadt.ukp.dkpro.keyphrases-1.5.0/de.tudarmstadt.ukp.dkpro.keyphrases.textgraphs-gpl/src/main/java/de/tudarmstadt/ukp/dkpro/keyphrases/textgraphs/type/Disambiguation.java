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

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


public class Disambiguation extends Annotation {
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Disambiguation.class);
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;

  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected Disambiguation() {/* intentionally empty block */}
    
  public Disambiguation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  public Disambiguation(JCas jcas) {
    super(jcas);
    readObject();   
  } 
  
  public Disambiguation(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: MajorSense


  public Sense getMajorSense() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_MajorSense == null)
      jcasType.jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return (Sense)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_MajorSense)));}
    
  public void setMajorSense(Sense v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_MajorSense == null)
      jcasType.jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_MajorSense, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Senses

  public FSArray getSenses() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses)));}
    
  
  public void setSenses(FSArray v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses, jcasType.ll_cas.ll_getFSRef(v));}    
    
  public Sense getSenses(int i) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i);
    return (Sense)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i)));}

  public void setSenses(int i, Sense v) { 
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: SourceId


  public long getSourceId() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceId == null)
      jcasType.jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceId);}
    
  public void setSourceId(long v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceId == null)
      jcasType.jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceId, v);}    
   
    
  //*--------------*
  //* Feature: SourceName


  public String getSourceName() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceName == null)
      jcasType.jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceName);}
    
  public void setSourceName(String v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceName == null)
      jcasType.jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceName, v);}    
   
    
  //*--------------*
  //* Feature: AnchorId

  public long getAnchorId() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorId == null)
      jcasType.jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorId);}
    
  public void setAnchorId(long v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorId == null)
      jcasType.jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorId, v);}    
   
    
  //*--------------*
  //* Feature: AnchorName

  public String getAnchorName() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorName == null)
      jcasType.jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorName);}
    

  public void setAnchorName(String v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorName == null)
      jcasType.jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorName, v);}    
  }

    
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


public class Sense extends Annotation {
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Sense.class);
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;

  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected Sense() {/* intentionally empty block */}
    
  public Sense(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  

  public Sense(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  
  public Sense(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   


  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Id


  public long getId() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getLongValue(addr, ((Sense_Type)jcasType).casFeatCode_Id);}
    
  public void setId(long v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setLongValue(addr, ((Sense_Type)jcasType).casFeatCode_Id, v);}    
   
    
  //*--------------*
  //* Feature: Name


  public String getName() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Name == null)
      jcasType.jcas.throwFeatMissing("Name", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Sense_Type)jcasType).casFeatCode_Name);}
    

  public void setName(String v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Name == null)
      jcasType.jcas.throwFeatMissing("Name", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setStringValue(addr, ((Sense_Type)jcasType).casFeatCode_Name, v);}    
   
    
  //*--------------*
  //* Feature: Counter


  public int getCounter() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Counter == null)
      jcasType.jcas.throwFeatMissing("Counter", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sense_Type)jcasType).casFeatCode_Counter);}
    
  public void setCounter(int v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Counter == null)
      jcasType.jcas.throwFeatMissing("Counter", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sense_Type)jcasType).casFeatCode_Counter, v);}    
   
    
  //*--------------*
  //* Feature: Score


  public double getScore() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Score == null)
      jcasType.jcas.throwFeatMissing("Score", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Sense_Type)jcasType).casFeatCode_Score);}
    

  public void setScore(double v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Score == null)
      jcasType.jcas.throwFeatMissing("Score", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Sense_Type)jcasType).casFeatCode_Score, v);}    
   
    
  //*--------------*
  //* Feature: LinkedSenses


  public FSArray getLinkedSenses() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses)));}
    

  public void setLinkedSenses(FSArray v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses, jcasType.ll_cas.ll_getFSRef(v));}    
    

  public Sense getLinkedSenses(int i) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i);
    return (Sense)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i)));}


  public void setLinkedSenses(int i, Sense v) { 
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    
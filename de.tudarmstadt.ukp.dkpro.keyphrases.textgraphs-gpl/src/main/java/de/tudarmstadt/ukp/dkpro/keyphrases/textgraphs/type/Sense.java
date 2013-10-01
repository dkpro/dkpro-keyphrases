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


/** 
 * Updated by JCasGen Wed May 22 14:36:41 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs/src/main/resources/desc/type/Disambiguation.xml
 * @generated */
public class Sense extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Sense.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Sense() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Sense(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Sense(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Sense(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Id

  /** getter for Id - gets The Id of the sense
   * @generated */
  public long getId() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getLongValue(addr, ((Sense_Type)jcasType).casFeatCode_Id);}
    
  /** setter for Id - sets The Id of the sense 
   * @generated */
  public void setId(long v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Id == null)
      jcasType.jcas.throwFeatMissing("Id", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setLongValue(addr, ((Sense_Type)jcasType).casFeatCode_Id, v);}    
   
    
  //*--------------*
  //* Feature: Name

  /** getter for Name - gets The name of the sense
   * @generated */
  public String getName() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Name == null)
      jcasType.jcas.throwFeatMissing("Name", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Sense_Type)jcasType).casFeatCode_Name);}
    
  /** setter for Name - sets The name of the sense 
   * @generated */
  public void setName(String v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Name == null)
      jcasType.jcas.throwFeatMissing("Name", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setStringValue(addr, ((Sense_Type)jcasType).casFeatCode_Name, v);}    
   
    
  //*--------------*
  //* Feature: Counter

  /** getter for Counter - gets 
   * @generated */
  public int getCounter() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Counter == null)
      jcasType.jcas.throwFeatMissing("Counter", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Sense_Type)jcasType).casFeatCode_Counter);}
    
  /** setter for Counter - sets  
   * @generated */
  public void setCounter(int v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Counter == null)
      jcasType.jcas.throwFeatMissing("Counter", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setIntValue(addr, ((Sense_Type)jcasType).casFeatCode_Counter, v);}    
   
    
  //*--------------*
  //* Feature: Score

  /** getter for Score - gets 
   * @generated */
  public double getScore() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Score == null)
      jcasType.jcas.throwFeatMissing("Score", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Sense_Type)jcasType).casFeatCode_Score);}
    
  /** setter for Score - sets  
   * @generated */
  public void setScore(double v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_Score == null)
      jcasType.jcas.throwFeatMissing("Score", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Sense_Type)jcasType).casFeatCode_Score, v);}    
   
    
  //*--------------*
  //* Feature: LinkedSenses

  /** getter for LinkedSenses - gets 
   * @generated */
  public FSArray getLinkedSenses() {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses)));}
    
  /** setter for LinkedSenses - sets  
   * @generated */
  public void setLinkedSenses(FSArray v) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.ll_cas.ll_setRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for LinkedSenses - gets an indexed value - 
   * @generated */
  public Sense getLinkedSenses(int i) {
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i);
    return (Sense)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i)));}

  /** indexed setter for LinkedSenses - sets an indexed value - 
   * @generated */
  public void setLinkedSenses(int i, Sense v) { 
    if (Sense_Type.featOkTst && ((Sense_Type)jcasType).casFeat_LinkedSenses == null)
      jcasType.jcas.throwFeatMissing("LinkedSenses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Sense");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Sense_Type)jcasType).casFeatCode_LinkedSenses), i, jcasType.ll_cas.ll_getFSRef(v));}
  }

    
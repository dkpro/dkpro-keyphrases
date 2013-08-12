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
package de.tudarmstadt.ukp.dkpro.keyphrases.core.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Apr 18 17:45:18 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.core/src/main/resources/desc/type/Keyphrase.xml
 * @generated */
public class Keyphrase extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Keyphrase.class);
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
  protected Keyphrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Keyphrase(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Keyphrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Keyphrase(JCas jcas, int begin, int end) {
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
  //* Feature: score

  /** getter for score - gets 
   * @generated */
  public double getScore() {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets  
   * @generated */
  public void setScore(double v) {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: keyphrase

  /** getter for keyphrase - gets 
   * @generated */
  public String getKeyphrase() {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_keyphrase == null)
      jcasType.jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_keyphrase);}
    
  /** setter for keyphrase - sets  
   * @generated */
  public void setKeyphrase(String v) {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_keyphrase == null)
      jcasType.jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    jcasType.ll_cas.ll_setStringValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_keyphrase, v);}    
  }

    
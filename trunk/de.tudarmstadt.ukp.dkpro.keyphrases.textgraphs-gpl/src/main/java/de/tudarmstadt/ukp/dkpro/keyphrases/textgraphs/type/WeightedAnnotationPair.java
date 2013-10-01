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



/** 
 * Updated by JCasGen Wed May 22 14:44:24 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs/src/main/resources/desc/type/EdgeWeight.xml
 * @generated */
public class WeightedAnnotationPair extends AnnotationPair {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(WeightedAnnotationPair.class);
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
  protected WeightedAnnotationPair() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public WeightedAnnotationPair(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public WeightedAnnotationPair(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public WeightedAnnotationPair(JCas jcas, int begin, int end) {
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
  //* Feature: weight

  /** getter for weight - gets 
   * @generated */
  public double getWeight() {
    if (WeightedAnnotationPair_Type.featOkTst && ((WeightedAnnotationPair_Type)jcasType).casFeat_weight == null)
      jcasType.jcas.throwFeatMissing("weight", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WeightedAnnotationPair");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((WeightedAnnotationPair_Type)jcasType).casFeatCode_weight);}
    
  /** setter for weight - sets  
   * @generated */
  public void setWeight(double v) {
    if (WeightedAnnotationPair_Type.featOkTst && ((WeightedAnnotationPair_Type)jcasType).casFeat_weight == null)
      jcasType.jcas.throwFeatMissing("weight", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.WeightedAnnotationPair");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((WeightedAnnotationPair_Type)jcasType).casFeatCode_weight, v);}    
  }

    
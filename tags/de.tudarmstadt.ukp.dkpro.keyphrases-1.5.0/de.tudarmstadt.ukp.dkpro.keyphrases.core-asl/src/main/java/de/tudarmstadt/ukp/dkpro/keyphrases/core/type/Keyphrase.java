/*******************************************************************************
 * Copyright 2013
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.dkpro.keyphrases.core.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


public class Keyphrase extends Annotation {

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Keyphrase.class);
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected Keyphrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator
   * @param addr The memory address
   * @param type The type being annotated 
   * */
  public Keyphrase(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** 
   * @param jcas The jcas where the type is being annotated 
   * */
  public Keyphrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** 
   * @param jcas The jcas where the type is being annotated
   * @param begin The begin offset
   * @param end The end offset
   * */  
  public Keyphrase(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets
   * @return The score from the keyphrase 
   **/
  public double getScore() {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets
   * @param v the score value  
   * */
  public void setScore(double v) {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: keyphrase

  /** getter for keyphrase - gets
   * @return The keyphrase string
   * */
  public String getKeyphrase() {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_keyphrase == null)
      jcasType.jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_keyphrase);}
    
  /** setter for keyphrase - sets  
   * @param v The keyphrase value 
   **/
  public void setKeyphrase(String v) {
    if (Keyphrase_Type.featOkTst && ((Keyphrase_Type)jcasType).casFeat_keyphrase == null)
      jcasType.jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.Keyphrase");
    jcasType.ll_cas.ll_setStringValue(addr, ((Keyphrase_Type)jcasType).casFeatCode_keyphrase, v);}    
  }

    
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

import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.jcas.cas.NonEmptyStringList;



public class KeyphraseMetaData extends DocumentAnnotation {
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeyphraseMetaData.class);
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected KeyphraseMetaData() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator
   * @param addr The memory address
   * @param type The type being annotated 
   * */
  public KeyphraseMetaData(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /**
   * @param jcas The jcas which the type will be annotated
   * */
  public KeyphraseMetaData(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /**
   *@param jcas Jcas
   *@param begin begin offset
   *@param end end offset 
   * */  
  public KeyphraseMetaData(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: goldstandardKeyphrases

  /** getter for goldstandardKeyphrases - gets
   * @return NonEmptyStringList the list of gold standard keyphrases 
   **/
  public NonEmptyStringList getGoldstandardKeyphrases() {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_goldstandardKeyphrases == null)
      jcasType.jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return (NonEmptyStringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_goldstandardKeyphrases)));}
    
  /** setter for goldstandardKeyphrases - sets
   * @param v the gold standard keyphrases  
   **/
  public void setGoldstandardKeyphrases(NonEmptyStringList v) {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_goldstandardKeyphrases == null)
      jcasType.jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    jcasType.ll_cas.ll_setRefValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_goldstandardKeyphrases, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: nrofKeyphrases

  /** getter for nrofKeyphrases - gets
   * @return the number of keyphrases 
   * */
  public int getNrofKeyphrases() {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_nrofKeyphrases == null)
      jcasType.jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return jcasType.ll_cas.ll_getIntValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_nrofKeyphrases);}
    
  /** setter for nrofKeyphrases - sets
   * @param v the number of keyphrases  
   * */
  public void setNrofKeyphrases(int v) {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_nrofKeyphrases == null)
      jcasType.jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    jcasType.ll_cas.ll_setIntValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_nrofKeyphrases, v);}    
  }

    
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
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.DocumentAnnotation_Type;

public class KeyphraseMetaData_Type extends DocumentAnnotation_Type {
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (KeyphraseMetaData_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = KeyphraseMetaData_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new KeyphraseMetaData(addr, KeyphraseMetaData_Type.this);
  			   KeyphraseMetaData_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new KeyphraseMetaData(addr, KeyphraseMetaData_Type.this);
  	  }
    };

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KeyphraseMetaData.typeIndexID;

  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
 
  final Feature casFeat_goldstandardKeyphrases;
  final int     casFeatCode_goldstandardKeyphrases; 
  public int getGoldstandardKeyphrases(int addr) {
        if (featOkTst && casFeat_goldstandardKeyphrases == null)
      jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return ll_cas.ll_getRefValue(addr, casFeatCode_goldstandardKeyphrases);
  }
    
  public void setGoldstandardKeyphrases(int addr, int v) {
        if (featOkTst && casFeat_goldstandardKeyphrases == null)
      jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    ll_cas.ll_setRefValue(addr, casFeatCode_goldstandardKeyphrases, v);}
    
  
 
  final Feature casFeat_nrofKeyphrases;
  final int     casFeatCode_nrofKeyphrases; 
  public int getNrofKeyphrases(int addr) {
        if (featOkTst && casFeat_nrofKeyphrases == null)
      jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return ll_cas.ll_getIntValue(addr, casFeatCode_nrofKeyphrases);
  }
  public void setNrofKeyphrases(int addr, int v) {
        if (featOkTst && casFeat_nrofKeyphrases == null)
      jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    ll_cas.ll_setIntValue(addr, casFeatCode_nrofKeyphrases, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
   * @param jcas JCas which the type will be annotated
   * @param casType The type which will be annotated
   * */
  public KeyphraseMetaData_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_goldstandardKeyphrases = jcas.getRequiredFeatureDE(casType, "goldstandardKeyphrases", "uima.cas.NonEmptyStringList", featOkTst);
    casFeatCode_goldstandardKeyphrases  = (null == casFeat_goldstandardKeyphrases) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_goldstandardKeyphrases).getCode();

 
    casFeat_nrofKeyphrases = jcas.getRequiredFeatureDE(casType, "nrofKeyphrases", "uima.cas.Integer", featOkTst);
    casFeatCode_nrofKeyphrases  = (null == casFeat_nrofKeyphrases) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_nrofKeyphrases).getCode();

  }
}



    
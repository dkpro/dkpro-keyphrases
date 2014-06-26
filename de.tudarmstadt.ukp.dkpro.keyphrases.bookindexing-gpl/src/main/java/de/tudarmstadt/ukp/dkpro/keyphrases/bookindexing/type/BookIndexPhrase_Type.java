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
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type;

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

public class BookIndexPhrase_Type extends Annotation_Type {
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (BookIndexPhrase_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = BookIndexPhrase_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new BookIndexPhrase(addr, BookIndexPhrase_Type.this);
  			   BookIndexPhrase_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new BookIndexPhrase(addr, BookIndexPhrase_Type.this);
  	  }
    };
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = BookIndexPhrase.typeIndexID;
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
 
  final Feature casFeat_score;
  final int     casFeatCode_score;
  /**
   * @param addr memory address
   * @return the score
   *  */ 
  public double getScore(int addr) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    return ll_cas.ll_getDoubleValue(addr, casFeatCode_score);
  }
  /** 
   * @param addr the memory addr
   * @param v the value
   * */    
  public void setScore(int addr, double v) {
        if (featOkTst && casFeat_score == null)
      jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    ll_cas.ll_setDoubleValue(addr, casFeatCode_score, v);}
    
  
 
  final Feature casFeat_phrase;
  final int     casFeatCode_phrase;
  /** 
   * @param addr the memory address
   * @return The phrase
   * */ 
  public String getPhrase(int addr) {
        if (featOkTst && casFeat_phrase == null)
      jcas.throwFeatMissing("phrase", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    return ll_cas.ll_getStringValue(addr, casFeatCode_phrase);
  }
  /** 
   * @param addr the memory address
   * @param v the phrase
   * */    
  public void setPhrase(int addr, String v) {
        if (featOkTst && casFeat_phrase == null)
      jcas.throwFeatMissing("phrase", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    ll_cas.ll_setStringValue(addr, casFeatCode_phrase, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
   * @param jcas jcas
   * @param casType Cas type
	* */
  public BookIndexPhrase_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_score = jcas.getRequiredFeatureDE(casType, "score", "uima.cas.Double", featOkTst);
    casFeatCode_score  = (null == casFeat_score) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_score).getCode();

 
    casFeat_phrase = jcas.getRequiredFeatureDE(casType, "phrase", "uima.cas.String", featOkTst);
    casFeatCode_phrase  = (null == casFeat_phrase) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_phrase).getCode();

  }
}



    
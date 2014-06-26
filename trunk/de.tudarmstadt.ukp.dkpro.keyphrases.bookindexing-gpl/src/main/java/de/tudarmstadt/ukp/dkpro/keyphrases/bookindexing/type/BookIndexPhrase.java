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
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;

public class BookIndexPhrase extends Annotation {

  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(BookIndexPhrase.class);
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected BookIndexPhrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @param addr addr
   * @param type TOP_Type
   * */
  public BookIndexPhrase(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /**
   *  @param jcas jcas
   *  */
  public BookIndexPhrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /**
   * @param jcas jcas
   * @param begin begin offset
   * @param end end offset
   * */  
  public BookIndexPhrase(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: score

  /** getter for score - gets The individual relevance of the phrase to be referenced in the back-of-the-book index is represented by the value of this feature.
   * @return the book index phrase score
   * */
  public double getScore() {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets The individual relevance of the phrase to be referenced in the back-of-the-book index is represented by the value of this feature.
   *  @param v the score for the book index phrase
   **/
  public void setScore(double v) {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: phrase

  /** getter for phrase - gets The book-index-phrase is a phrase in the book, which is regarded to be relevant enough to be referenced in the back-of-the-book index. The individual relevance is represented by the value of the score feature. A back-of-the-book index entry can refer to the phrase (using a locator) and to other phrases which may be the same strings, but also different strings when they are synonyms or participate in same semantical concept which is denoted by the back-of-the-book index entry.
   * @return the book index phrase itself
   * */
  public String getPhrase() {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_phrase == null)
      jcasType.jcas.throwFeatMissing("phrase", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    return jcasType.ll_cas.ll_getStringValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_phrase);}
    
  /** setter for phrase - sets The book-index-phrase is a phrase in the book, which is regarded to be relevant enough to be referenced in the back-of-the-book index. The individual relevance is represented by the value of the score feature. A back-of-the-book index entry can refer to the phrase (using a locator) and to other phrases which may be the same strings, but also different strings when they are synonyms or participate in same semantical concept which is denoted by the back-of-the-book index entry. 
   * @param v the book index phrase being set*/
  public void setPhrase(String v) {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_phrase == null)
      jcasType.jcas.throwFeatMissing("phrase", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    jcasType.ll_cas.ll_setStringValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_phrase, v);}    
  }

    
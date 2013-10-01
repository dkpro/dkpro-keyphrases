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

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;


/** 
 * Updated by JCasGen Wed May 22 12:58:04 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs/src/main/resources/desc/type/FilteredToken.xml
 * @generated */
public class FilteredToken extends Token {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(FilteredToken.class);
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
  protected FilteredToken() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public FilteredToken(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public FilteredToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public FilteredToken(JCas jcas, int begin, int end) {
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
     
}

    
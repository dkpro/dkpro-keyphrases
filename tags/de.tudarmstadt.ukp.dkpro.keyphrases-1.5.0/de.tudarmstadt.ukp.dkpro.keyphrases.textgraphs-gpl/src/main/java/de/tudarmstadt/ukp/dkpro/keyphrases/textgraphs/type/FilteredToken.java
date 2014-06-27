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



public class FilteredToken extends Token {
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(FilteredToken.class);
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;

  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  protected FilteredToken() {/* intentionally empty block */}
    

  public FilteredToken(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  public FilteredToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  public FilteredToken(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   


  private void readObject() {/*default - does nothing empty block */}
     
}

    
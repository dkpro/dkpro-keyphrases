

/* First created by JCasGen Thu Apr 18 17:45:56 CEST 2013 */
package de.tudarmstadt.ukp.dkpro.keyphrases.core.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Apr 18 17:45:56 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.core/src/main/resources/desc/type/KeyphraseCandidate.xml
 * @generated */
public class KeyphraseCandidate extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeyphraseCandidate.class);
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
  protected KeyphraseCandidate() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public KeyphraseCandidate(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public KeyphraseCandidate(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public KeyphraseCandidate(JCas jcas, int begin, int end) {
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
  //* Feature: keyphrase

  /** getter for keyphrase - gets 
   * @generated */
  public String getKeyphrase() {
    if (KeyphraseCandidate_Type.featOkTst && ((KeyphraseCandidate_Type)jcasType).casFeat_keyphrase == null)
      jcasType.jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate");
    return jcasType.ll_cas.ll_getStringValue(addr, ((KeyphraseCandidate_Type)jcasType).casFeatCode_keyphrase);}
    
  /** setter for keyphrase - sets  
   * @generated */
  public void setKeyphrase(String v) {
    if (KeyphraseCandidate_Type.featOkTst && ((KeyphraseCandidate_Type)jcasType).casFeat_keyphrase == null)
      jcasType.jcas.throwFeatMissing("keyphrase", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseCandidate");
    jcasType.ll_cas.ll_setStringValue(addr, ((KeyphraseCandidate_Type)jcasType).casFeatCode_keyphrase, v);}    
  }

    
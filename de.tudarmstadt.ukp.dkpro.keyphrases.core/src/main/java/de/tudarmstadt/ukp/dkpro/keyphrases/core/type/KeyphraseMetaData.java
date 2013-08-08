

/* First created by JCasGen Thu Apr 18 17:46:10 CEST 2013 */
package de.tudarmstadt.ukp.dkpro.keyphrases.core.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.jcas.cas.NonEmptyStringList;


/** 
 * Updated by JCasGen Thu Apr 18 17:46:10 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.core/src/main/resources/desc/type/KeyphraseMetaData.xml
 * @generated */
public class KeyphraseMetaData extends DocumentAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KeyphraseMetaData.class);
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
  protected KeyphraseMetaData() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public KeyphraseMetaData(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public KeyphraseMetaData(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public KeyphraseMetaData(JCas jcas, int begin, int end) {
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
  //* Feature: goldstandardKeyphrases

  /** getter for goldstandardKeyphrases - gets 
   * @generated */
  public NonEmptyStringList getGoldstandardKeyphrases() {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_goldstandardKeyphrases == null)
      jcasType.jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return (NonEmptyStringList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_goldstandardKeyphrases)));}
    
  /** setter for goldstandardKeyphrases - sets  
   * @generated */
  public void setGoldstandardKeyphrases(NonEmptyStringList v) {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_goldstandardKeyphrases == null)
      jcasType.jcas.throwFeatMissing("goldstandardKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    jcasType.ll_cas.ll_setRefValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_goldstandardKeyphrases, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: nrofKeyphrases

  /** getter for nrofKeyphrases - gets 
   * @generated */
  public int getNrofKeyphrases() {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_nrofKeyphrases == null)
      jcasType.jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    return jcasType.ll_cas.ll_getIntValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_nrofKeyphrases);}
    
  /** setter for nrofKeyphrases - sets  
   * @generated */
  public void setNrofKeyphrases(int v) {
    if (KeyphraseMetaData_Type.featOkTst && ((KeyphraseMetaData_Type)jcasType).casFeat_nrofKeyphrases == null)
      jcasType.jcas.throwFeatMissing("nrofKeyphrases", "de.tudarmstadt.ukp.dkpro.keyphrases.core.type.KeyphraseMetaData");
    jcasType.ll_cas.ll_setIntValue(addr, ((KeyphraseMetaData_Type)jcasType).casFeatCode_nrofKeyphrases, v);}    
  }

    
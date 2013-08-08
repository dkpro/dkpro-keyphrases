

/* First created by JCasGen Wed May 22 14:36:41 CEST 2013 */
package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed May 22 14:36:41 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs/src/main/resources/desc/type/Disambiguation.xml
 * @generated */
public class Disambiguation extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Disambiguation.class);
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
  protected Disambiguation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Disambiguation(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Disambiguation(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Disambiguation(JCas jcas, int begin, int end) {
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
  //* Feature: MajorSense

  /** getter for MajorSense - gets 
   * @generated */
  public Sense getMajorSense() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_MajorSense == null)
      jcasType.jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return (Sense)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_MajorSense)));}
    
  /** setter for MajorSense - sets  
   * @generated */
  public void setMajorSense(Sense v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_MajorSense == null)
      jcasType.jcas.throwFeatMissing("MajorSense", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_MajorSense, jcasType.ll_cas.ll_getFSRef(v));}    
   
    
  //*--------------*
  //* Feature: Senses

  /** getter for Senses - gets 
   * @generated */
  public FSArray getSenses() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return (FSArray)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses)));}
    
  /** setter for Senses - sets  
   * @generated */
  public void setSenses(FSArray v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses, jcasType.ll_cas.ll_getFSRef(v));}    
    
  /** indexed getter for Senses - gets an indexed value - 
   * @generated */
  public Sense getSenses(int i) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i);
    return (Sense)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i)));}

  /** indexed setter for Senses - sets an indexed value - 
   * @generated */
  public void setSenses(int i, Sense v) { 
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_Senses == null)
      jcasType.jcas.throwFeatMissing("Senses", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.jcas.checkArrayBounds(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i);
    jcasType.ll_cas.ll_setRefArrayValue(jcasType.ll_cas.ll_getRefValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_Senses), i, jcasType.ll_cas.ll_getFSRef(v));}
   
    
  //*--------------*
  //* Feature: SourceId

  /** getter for SourceId - gets 
   * @generated */
  public long getSourceId() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceId == null)
      jcasType.jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceId);}
    
  /** setter for SourceId - sets  
   * @generated */
  public void setSourceId(long v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceId == null)
      jcasType.jcas.throwFeatMissing("SourceId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceId, v);}    
   
    
  //*--------------*
  //* Feature: SourceName

  /** getter for SourceName - gets 
   * @generated */
  public String getSourceName() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceName == null)
      jcasType.jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceName);}
    
  /** setter for SourceName - sets  
   * @generated */
  public void setSourceName(String v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_SourceName == null)
      jcasType.jcas.throwFeatMissing("SourceName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_SourceName, v);}    
   
    
  //*--------------*
  //* Feature: AnchorId

  /** getter for AnchorId - gets 
   * @generated */
  public long getAnchorId() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorId == null)
      jcasType.jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorId);}
    
  /** setter for AnchorId - sets  
   * @generated */
  public void setAnchorId(long v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorId == null)
      jcasType.jcas.throwFeatMissing("AnchorId", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setLongValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorId, v);}    
   
    
  //*--------------*
  //* Feature: AnchorName

  /** getter for AnchorName - gets 
   * @generated */
  public String getAnchorName() {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorName == null)
      jcasType.jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorName);}
    
  /** setter for AnchorName - sets  
   * @generated */
  public void setAnchorName(String v) {
    if (Disambiguation_Type.featOkTst && ((Disambiguation_Type)jcasType).casFeat_AnchorName == null)
      jcasType.jcas.throwFeatMissing("AnchorName", "de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.type.Disambiguation");
    jcasType.ll_cas.ll_setStringValue(addr, ((Disambiguation_Type)jcasType).casFeatCode_AnchorName, v);}    
  }

    


/* First created by JCasGen Thu Apr 18 17:31:58 CEST 2013 */
package de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Thu Apr 18 17:31:58 CEST 2013
 * XML source: /srv/workspace42/de.tudarmstadt.ukp.dkpro.keyphrases/de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing/src/main/resources/desc/type/BookIndexPhrase.xml
 * @generated */
public class BookIndexPhrase extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(BookIndexPhrase.class);
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
  protected BookIndexPhrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public BookIndexPhrase(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public BookIndexPhrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public BookIndexPhrase(JCas jcas, int begin, int end) {
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
  //* Feature: score

  /** getter for score - gets The individual relevance of the phrase to be referenced in the back-of-the-book index is represented by the value of this feature.
   * @generated */
  public double getScore() {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    return jcasType.ll_cas.ll_getDoubleValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_score);}
    
  /** setter for score - sets The individual relevance of the phrase to be referenced in the back-of-the-book index is represented by the value of this feature. 
   * @generated */
  public void setScore(double v) {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_score == null)
      jcasType.jcas.throwFeatMissing("score", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    jcasType.ll_cas.ll_setDoubleValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_score, v);}    
   
    
  //*--------------*
  //* Feature: phrase

  /** getter for phrase - gets The book-index-phrase is a phrase in the book, which is regarded to be relevant enough to be referenced in the back-of-the-book index. The individual relevance is represented by the value of the score feature. A back-of-the-book index entry can refer to the phrase (using a locator) and to other phrases which may be the same strings, but also different strings when they are synonyms or participate in same semantical concept which is denoted by the back-of-the-book index entry.
   * @generated */
  public String getPhrase() {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_phrase == null)
      jcasType.jcas.throwFeatMissing("phrase", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    return jcasType.ll_cas.ll_getStringValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_phrase);}
    
  /** setter for phrase - sets The book-index-phrase is a phrase in the book, which is regarded to be relevant enough to be referenced in the back-of-the-book index. The individual relevance is represented by the value of the score feature. A back-of-the-book index entry can refer to the phrase (using a locator) and to other phrases which may be the same strings, but also different strings when they are synonyms or participate in same semantical concept which is denoted by the back-of-the-book index entry. 
   * @generated */
  public void setPhrase(String v) {
    if (BookIndexPhrase_Type.featOkTst && ((BookIndexPhrase_Type)jcasType).casFeat_phrase == null)
      jcasType.jcas.throwFeatMissing("phrase", "de.tudarmstadt.ukp.dkpro.keyphrases.bookindexing.type.BookIndexPhrase");
    jcasType.ll_cas.ll_setStringValue(addr, ((BookIndexPhrase_Type)jcasType).casFeatCode_phrase, v);}    
  }

    
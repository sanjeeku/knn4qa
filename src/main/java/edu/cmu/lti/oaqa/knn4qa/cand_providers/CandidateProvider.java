/*
 *  Copyright 2015 Carnegie Mellon University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cmu.lti.oaqa.knn4qa.cand_providers;

import java.util.*;

import edu.cmu.lti.oaqa.annographix.solr.SolrRes;

public abstract class CandidateProvider {
  public final static String ID_FIELD_NAME     = "DOCNO";
  public final static String TEXT_FIELD_NAME   = "Text_bm25";
      
  public static final String CAND_TYPE_QRELS       = "qrels";
  public static final String CAND_TYPE_SOLR        = "solr";
  public static final String CAND_TYPE_LUCENE      = "lucene";
  public static final String CAND_TYPE_LUCENE_GIZA = "lucene_giza";
  public static final String CAND_TYPE_NMSLIB      = "nmslib";
  public static final String CAND_TYPE_KNN         = "knn_java";
  

  public final static String CAND_PROVID_DESC = "candidate record provider type: " + 
      CandidateProvider.CAND_TYPE_LUCENE + ", " + 
      CandidateProvider.CAND_TYPE_QRELS + ", " + 
      CandidateProvider.CAND_TYPE_SOLR + ", " + 
      CandidateProvider.CAND_TYPE_KNN + ", " + 
      CandidateProvider.CAND_TYPE_NMSLIB;
  
  /**
   * @return  true if {@link #getCandidates(int, Map, int)} can be called by 
   *               several threads simultaneously. 
   *           
   */
  public abstract boolean isThreadSafe();
  
  public abstract String getName();
  
  /**
   * Determines if a QREL label defines a relevant entry.
   * 
   * @param label       the string label (can be null).
   * @param minRelLevel the minimum value to be considered relevant
   * @return true if the relevance is at least minRelLevel or false, if the
   *         the label is null.
   * @throws Exception throws an exception if the label is not numeric
   */
  public static boolean isRelevLabel(String label, int minRelLevel) throws Exception {
    if (null == label) return false;
    int relVal = 0;
    try {
      relVal = Integer.parseInt(label);
    } catch (NumberFormatException e) {
      throw new Exception("Label '" + label + "' is not numeric!");
    }
    return relVal >= minRelLevel;
  }
  
  /**
   * Return a <b>sorted</b> list of candidate records with respective similarity scores +
   * the total number of entries found.
   * 
   * @param     queryNum     an ordinal query number (for debugging purposes).
   * @param     queryData    several pieces of input data, one is typically a bag-of-words query.
   * @param     maxQty       a maximum number of candidate records to return.
   * @return    an array of candidate records (doc ids + scores, no document text) + 
   *            the total number of entries found.
   */
  abstract public CandidateInfo getCandidates(int queryNum, 
                                    Map<String, String> queryData, 
                                    int maxQty)  throws Exception;
}

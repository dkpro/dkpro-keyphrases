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
package de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.util;

import java.util.HashMap;
import java.util.Map;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.FlexCompColMatrix;

import org.junit.Test;

import de.tudarmstadt.ukp.dkpro.keyphrases.textgraphs.util.PageRank;

public class PageRankTest {

    @Test
    public void pageRankTest() {
        PageRank textRank = new PageRank();
        FlexCompColMatrix adjacentMatrix = new FlexCompColMatrix(22, 22);
        String[] nameArray = new String[] { "systems", "compatibility",
                "criteria", "numbers", "natural", "types", "linear", "system",
                "diophantine", "constraints", "equations", "strict",
                "inequations", "nonstrict", "upper", "bounds", "components",
                "minimal", "construction", "algorithms", "solutions", "sets" };
        connect(adjacentMatrix,0,1);
        connect(adjacentMatrix,0,5);
        connect(adjacentMatrix,0,6);
        connect(adjacentMatrix,1,2);
        connect(adjacentMatrix,2,3);
        connect(adjacentMatrix,2,4);
        connect(adjacentMatrix,3,4);
        connect(adjacentMatrix,5,20);
        connect(adjacentMatrix,6,7);
        connect(adjacentMatrix,6,8);
        connect(adjacentMatrix,6,9);
        connect(adjacentMatrix,6,10);
        connect(adjacentMatrix,10,11);
        connect(adjacentMatrix,11,12);
        connect(adjacentMatrix,12,13);
        connect(adjacentMatrix,14,15);
        connect(adjacentMatrix,15,16);
        connect(adjacentMatrix,16,17);
        connect(adjacentMatrix,17,18);
        connect(adjacentMatrix,17,21);
        connect(adjacentMatrix,18,19);
        connect(adjacentMatrix,19,20);
        connect(adjacentMatrix,21,20);
        double d = 0.85;
        double threshold = 0.0001;
        Vector degreeVector = textRank.getDegreeVector(22, adjacentMatrix);
        Vector scoreVector =  textRank.initScoreVector(22);
        DenseVector biasVector = textRank.createPreferenceVector(22, d);

        FlexCompColMatrix transitionMatrix = textRank.createTransitionMatrix(adjacentMatrix, degreeVector, 22);

        textRank.nodeRank(transitionMatrix, d, biasVector, scoreVector, threshold);
        
//        for(int i = 0; i < nameArray.length ; i++){
//            System.out.println(nameArray[i]+"\t"+degreeVector.get(i));
//        }
        
        Map<String,Double> results = new HashMap<String,Double>();
        for(int i = 0; i < nameArray.length ; i++) {
            results.put(nameArray[i], scoreVector.get(i));
        }

        System.out.println(results);
        
    }

    private void connect(FlexCompColMatrix adjacentMatrix,int nodeA,int nodeB) {
        adjacentMatrix.set(nodeA,nodeB,1.0);
        adjacentMatrix.set(nodeB,nodeA,1.0);
    }

}

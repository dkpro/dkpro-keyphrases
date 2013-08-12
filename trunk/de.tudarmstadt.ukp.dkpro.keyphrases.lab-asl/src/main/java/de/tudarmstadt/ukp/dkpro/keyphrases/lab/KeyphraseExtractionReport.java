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
package de.tudarmstadt.ukp.dkpro.keyphrases.lab;

import java.util.Map;

import de.tudarmstadt.ukp.dkpro.lab.reporting.BatchReportBase;
import de.tudarmstadt.ukp.dkpro.lab.reporting.FlexTable;
import de.tudarmstadt.ukp.dkpro.lab.storage.StorageService;
import de.tudarmstadt.ukp.dkpro.lab.storage.impl.PropertiesAdapter;
import de.tudarmstadt.ukp.dkpro.lab.task.Task;
import de.tudarmstadt.ukp.dkpro.lab.task.TaskContextMetadata;

public class KeyphraseExtractionReport
    extends BatchReportBase
{
    public static final String REPORT_FILE_KEY =
            KeyphraseExtractionReport.class.getSimpleName() + ".xls";

    @Override
    public void execute()
        throws Exception
    {
        FlexTable<Object> overviewTable = FlexTable.forClass(Object.class);
        overviewTable.setDefaultValue(-1.0);
        StorageService storageService = getContext().getStorageService();
        for (TaskContextMetadata subcontext : getSubtasks()) {
            Map<String, String> discriminatorConfigs = storageService.retrieveBinary(subcontext.getId(),
                    Task.DISCRIMINATORS_KEY, new PropertiesAdapter()).getMap();
            overviewTable.addToRow(subcontext.getLabel(), discriminatorConfigs);

            if (storageService.containsKey(subcontext.getId(), KeyphraseEvaluatorLab.class.getSimpleName())) {
                EvaluationResultStream resultStream = storageService.retrieveBinary(
                        subcontext.getId(), KeyphraseEvaluatorLab.class.getSimpleName(),
                        new EvaluationResultStream());
                overviewTable.addToRow(subcontext.getLabel(), resultStream.getResults());

            }

        }
        storeBinary(REPORT_FILE_KEY, overviewTable.getExcelWriter());
    }

}

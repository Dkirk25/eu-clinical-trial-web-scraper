package com.clincaloutcome.builder;

import com.clincaloutcome.client.ClinicalPageHelper;
import com.clincaloutcome.model.EUClinical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebBuilder {
    @Autowired
    private ExcelBuilder excelBuilder;

    @Autowired
    private USandEUBuilder usAndEUBuilder;

    @Autowired
    private ClinicalPageHelper clinicalPageHelper;

    public void singleBuilder(final String url, final String pages) {
        Map<Integer, List<List<String>>> mapResults = new HashMap<>();
        List<List<String>> listOfResults = clinicalPageHelper.iterateThroughUrlAndPage(url, pages);
        mapResults.put(0, listOfResults);
        excelBuilder.printFromEUTrialExcelFile(mapResults);
    }

    public void bulkBuilder(final File file) {
        Map<Integer, List<List<String>>> listOfResults = clinicalPageHelper.createUsingBulkFile(file);
        excelBuilder.printFromEUTrialExcelFile(listOfResults);
    }

    public void crossBuilder(final String usTrialFile, final String euTrialFile) {
        List<EUClinical> euList = usAndEUBuilder.extractMatchesFromBothLists(usTrialFile, euTrialFile);
        excelBuilder.printEUListToExcel(euList);
    }
}

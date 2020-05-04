package com.clincaloutcome.builder;

import com.clincaloutcome.client.ClinicalPageHelper;
import com.clincaloutcome.model.EUClinical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
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

    private final Map<String, List<String>> listMap;

    private WebBuilder() {
        listMap = new HashMap<>();

        listMap.put("eudraCT", new ArrayList<>());
        listMap.put("sponsorProtocols", new ArrayList<>());
        listMap.put("startDates", new ArrayList<>());
        listMap.put("sponsorNames", new ArrayList<>());
        listMap.put("fullTitles", new ArrayList<>());
        listMap.put("medicalConditions", new ArrayList<>());
        listMap.put("diseases", new ArrayList<>());
        listMap.put("populationAge", new ArrayList<>());
        listMap.put("genders", new ArrayList<>());
        listMap.put("trialProtocol", new ArrayList<>());
        listMap.put("primaryEndpoints", new ArrayList<>());
        listMap.put("secondaryEndPoints", new ArrayList<>());
        listMap.put("trialResults", new ArrayList<>());
    }

    public void singleBuilder(final String url, final String pages) {
        Map<String, List<String>> listOfResults = clinicalPageHelper.iterateThroughUrlAndPage(url, pages, listMap);
        excelBuilder.printFromEUTrialExcelFile(listOfResults);
    }

    public void bulkBuilder(final File file) {
        Map<String, List<String>> listOfResults = clinicalPageHelper.createUsingBulkFile(file, listMap);
        excelBuilder.printFromEUTrialExcelFile(listOfResults);
    }

    public void crossBuilder(final String usTrialFile, final String euTrialFile) {
        List<EUClinical> euList = usAndEUBuilder.extractMatchesFromBothLists(usTrialFile, euTrialFile);
        excelBuilder.printEUListToExcel(euList);
    }
}

package com.clincaloutcome.builder;

import com.clincaloutcome.model.EUClinical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;


@Component
public class WebBuilder {
    @Autowired
    private ExcelBuilder excelBuilder;

    @Autowired
    private USandEUBuilder usAndEUBuilder;

    @Autowired
    private ClinicalPageHelper clinicalPageHelper;

    public void singleBuilder(final String url, final String pages) {
        // Have this return a map of lists
        clinicalPageHelper.iterateThroughUrlAndPage(url, pages);

        // Pass Map in
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void bulkBuilder(final File file) {
        // Have this return a map of lists
        clinicalPageHelper.createUsingBulkFile(file);
        // Pass Map in
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void crossBuilder(final String usTrialFile, final String euTrialFile) {
        List<EUClinical> euList = usAndEUBuilder.extractMatchesFromBothLists(usTrialFile, euTrialFile);
        excelBuilder.printEUListToExcel(euList);
    }
}

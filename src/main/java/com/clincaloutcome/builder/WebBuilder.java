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
    private USandEUBuilder uSandEUBuilder;

    @Autowired
    private ClinicalPageHelper clinicalPageHelper;

    public void singleBuilder(final String url, final String pages) {
        clinicalPageHelper.iterateThroughUrlAndPage(url, pages);
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void bulkBuilder(final File file) {
        clinicalPageHelper.createUsingBulkFile(file);
        excelBuilder.printFromEUTrialExcelFile();
    }

    public void crossBuilder(final String usTrialFile, final String euTrialFile) {
        List<EUClinical> euList = uSandEUBuilder.extractMatchesFromBothLists(usTrialFile, euTrialFile);
        excelBuilder.printEUListToExcel(euList);
    }
}

package com.clincaloutcome.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WebBuilderTest {
    @Mock
    private ExcelBuilder excelBuilder;

    @Mock
    private USandEUBuilder uSandEUBuilder;

    @Mock
    private ClinicalPageHelper clinicalPageHelper;

    @InjectMocks
    private WebBuilder webBuilder;

    @Before
    public void setUp() {

    }

    @Test
    public void singleBuilder_validateSingleSearch() {
        when(clinicalPageHelper.iterateThroughUrlAndPage(anyString(), anyString(), anyMap())).thenReturn(new HashMap<>());
        String url = "dsasdasad";
        String pages = "2";
        webBuilder.singleBuilder(url, pages);
        verify(excelBuilder).printFromEUTrialExcelFile(new HashMap<>());
    }

    @Test
    public void bulkBuilder_validateBulkSearch() {
        when(clinicalPageHelper.createUsingBulkFile(any(), anyMap())).thenReturn(new HashMap<>());
        webBuilder.bulkBuilder(new File("any.txt"));
        verify(excelBuilder).printFromEUTrialExcelFile(new HashMap<>());
    }

    @Test
    public void crossBuilder_validateCrossSearch() {
        when(uSandEUBuilder.extractMatchesFromBothLists(any(), any())).thenReturn(new ArrayList<>());
        webBuilder.crossBuilder("excelFile.xlsx", "excelFile.xlsx");
        verify(excelBuilder).printEUListToExcel(new ArrayList<>());
    }
}

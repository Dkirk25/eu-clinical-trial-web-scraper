package com.clincaloutcome.builder;

import com.clincaloutcome.client.ClinicalPageHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WebBuilderTest {
    private final File mockFile = mock(File.class);
    @Mock
    private ExcelBuilder excelBuilder;
    @Mock
    private USandEUBuilder uSandEUBuilder;
    @Mock
    private ClinicalPageHelper clinicalPageHelper;
    @InjectMocks
    private WebBuilder webBuilder;
    private List<List<String>> mockList;
    private Map<Integer, List<List<String>>> mapResults;

    @Before
    public void setUp() {
        mockList = new ArrayList<>();
        mapResults = new HashMap<>();
        mockList.add(Collections.singletonList("test data"));
        mapResults.put(0, mockList);
    }

    @Test
    public void singleBuilder_validateSingleSearch() {
        String mockUrl = "pageUrl";
        String mockPageNumber = "3";
        when(clinicalPageHelper.iterateThroughUrlAndPage(mockUrl, mockPageNumber)).thenReturn(mockList);
        webBuilder.singleBuilder(mockUrl, mockPageNumber);
        verify(excelBuilder).printFromEUTrialExcelFile(mapResults);
    }

    @Test
    public void bulkBuilder_validateBulkSearch() {
        Map<Integer, List<List<String>>> map = new HashMap<>();
        when(clinicalPageHelper.createUsingBulkFile(mockFile)).thenReturn(map);
        webBuilder.bulkBuilder(mockFile);
        verify(excelBuilder).printFromEUTrialExcelFile(map);
    }

    @Test
    public void crossBuilder_validateCrossSearch() {
        String usExcel = "usFile.xlsx";
        String euExcel = "euFile.xlsx";
        when(uSandEUBuilder.extractMatchesFromBothLists(usExcel, euExcel)).thenReturn(new ArrayList<>());
        webBuilder.crossBuilder(usExcel, euExcel);
        verify(excelBuilder).printEUListToExcel(new ArrayList<>());
    }
}

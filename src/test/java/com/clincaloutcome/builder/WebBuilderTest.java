package com.clincaloutcome.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    private String url = "dsasdasad";
    private String pages = "2";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void singleBuilder_validateSingleSearch() {

    }
}

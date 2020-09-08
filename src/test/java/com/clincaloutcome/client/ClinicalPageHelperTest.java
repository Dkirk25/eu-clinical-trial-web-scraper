package com.clincaloutcome.client;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ClinicalPageHelperTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void splitValuesCorrectly() {
//        2010-021179-10,  TRO19622 CLEQ 1425-1,  2011-03-15, TROPHOS SA,  An Open-Label Safety Extension Study of TRO19622 in Amyotrophic Lateral Sclerosis (ALS) patients treated with riluzole.,  Amyotrophic Lateral Sclerosis., 12.1, , 10052889, ALS, LLT,  Adults, Elderly,  Male, Female,  DE (Prematurely Ended) BE (Prematurely Ended) GB (Prematurely Ended) ES (Prematurely Ended),  View results]

        List<String> expectedValues = Arrays.asList("2010-021179-10", "TRO19622 CLEQ 1425-1", "2011-03-15", "TROPHOS SA", "An Open-Label Safety Extension Study of TRO19622 in Amyotrophic Lateral Sclerosis (ALS) patients treated with riluzole.", "Amyotrophic Lateral Sclerosis", "12.1", " ", "10052889", "ALS", "LLT", "Adults, Elderly", "DE (Prematurely Ended) BE (Prematurely Ended) GB (Prematurely Ended) ES (Prematurely Ended)", "View results");

        String text = "EudraCT Number: 2010-021179-10 Sponsor Protocol Number: TRO19622 CLEQ 1425-1 Start Date*: 2011-03-15 Sponsor Name:TROPHOS SA Full Title: An Open-Label Safety Extension Study of TRO19622 in Amyotrophic Lateral Sclerosis (ALS) patients treated with riluzole. Medical condition: Amyotrophic Lateral Sclerosis. Disease: Version SOC Term Classification Code Term Level 12.1 10052889 ALS LLT Disease: Version SOC Term Classification Code Term Level 12.1 10052889 ALS LLT Population Age: Adults, Elderly Gender: Male, Female Trial protocol: DE (Prematurely Ended) BE (Prematurely Ended) GB (Prematurely Ended) ES (Prematurely Ended) Trial results: View results";



        //  List<String> actual = Arrays.asList(listOfValues[1]);

        //   Assert.assertEquals(expectedValues, actual);
    }
}

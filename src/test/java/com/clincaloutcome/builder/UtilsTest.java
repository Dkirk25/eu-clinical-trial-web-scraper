package com.clincaloutcome.builder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    @InjectMocks
    private Utils utils;

    @Test
    public void trialParser_validPrimaryPoint() {
        String expected = "The primary endpoint is the change from baseline to the average of the ALSFRS-R total score obtained at Visits 6 and 7 (i.e. after approximately 8 and 12 weeks of double-blind treatment)";
        String exampleString = "E.5.1 Primary end point(s) The primary endpoint is the change from baseline to the average of the ALSFRS-R total score obtained at Visits 6 and 7 (i.e. after approximately 8 and 12 weeks of double-blind treatment)";
        String actual = utils.trailParser(exampleString, "E.5.1 Primary end point");
        Assert.assertEquals(expected, actual);
    }


}

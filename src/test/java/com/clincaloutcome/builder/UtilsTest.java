package com.clincaloutcome.builder;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void trialParser_validPrimaryPoint() {
        String expected = "The primary endpoint is the change from baseline to the average of the ALSFRS-R total score obtained at Visits 6 and 7 (i.e. after approximately 8 and 12 weeks of double-blind treatment)";
        String exampleString = "E.5.1 Primary end point(s) The primary endpoint is the change from baseline to the average of the ALSFRS-R total score obtained at Visits 6 and 7 (i.e. after approximately 8 and 12 weeks of double-blind treatment)";
        String actual = Utils.trailParser(exampleString, "E.5.1 Primary end point");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void nullCheck_EmptyValueReturnsNA() {
        String emptyValue = "";
        Assert.assertEquals("N/A", Utils.nullCheck(emptyValue));
    }

    @Test
    public void nullCheck_NotEmptyValue() {
        String emptyValue = "Test";
        Assert.assertEquals("Test", Utils.nullCheck(emptyValue));
    }

    @Test
    public void isValidFileFormat_EndsWithXlsx() {
        String file = "test.xlsx";
        String type = "xlsx";
        Assert.assertTrue(Utils.isValidFileFormat(file, type));
    }

    @Test
    public void isValidFileFormat_BadEnding() {
        String file = "test.xlsx";
        String type = "123";
        Assert.assertFalse(Utils.isValidFileFormat(file, type));
    }

    @Test
    public void isValidFileFormat_FileIsEmpty() {
        String file = "";
        String type = "123";
        Assert.assertFalse(Utils.isValidFileFormat(file, type));
    }

    @Test
    public void isValidFileFormat_FileIsNull() {
        String file = null;
        String type = "123";
        Assert.assertFalse(Utils.isValidFileFormat(file, type));
    }

    @Test
    public void wordParser_removeColons() {
        String word = "This : is:a: test:";
        Assert.assertEquals("isa test", Utils.wordParser(word));
    }
}

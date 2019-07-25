package com.clincaloutcome.model;

import com.poiji.annotation.ExcelCell;

public class EUClinical {

    @ExcelCell(0)
    private String eudraNumber;
    @ExcelCell(1)
    private String sponsorProtocolNumber;
    @ExcelCell(2)
    private String startDate;
    @ExcelCell(3)
    private String sponsorName;
    @ExcelCell(4)
    private String fullTitle;
    @ExcelCell(5)
    private String medicalCondition;
    @ExcelCell(6)
    private String disease;
    @ExcelCell(7)
    private String populationAge;
    @ExcelCell(8)
    private String gender;
    @ExcelCell(9)
    private String trailProtocol;
    @ExcelCell(10)
    private String trialResult;
    @ExcelCell(11)
    private String primaryEndPoint;
    @ExcelCell(12)
    private String secondaryEndPoint;

}

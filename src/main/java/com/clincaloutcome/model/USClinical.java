package com.clincaloutcome.model;

import com.poiji.annotation.ExcelCell;
import lombok.Data;

@Data
public class USClinical {

    @ExcelCell(0)
    private String rank;
    @ExcelCell(1)
    private String ntcNumber;
    @ExcelCell(2)
    private String title;
    @ExcelCell(3)
    private String acronym;
    @ExcelCell(4)
    private String status;
    @ExcelCell(5)
    private String studyResults;
    @ExcelCell(6)
    private String condition;
    @ExcelCell(7)
    private String intervention;
    @ExcelCell(8)
    private String outcomeMeasure;
    @ExcelCell(9)
    private String sponsor;
    @ExcelCell(10)
    private String gender;
    @ExcelCell(11)
    private String age;
    @ExcelCell(12)
    private String phases;
    @ExcelCell(13)
    private String enrollment;
    @ExcelCell(14)
    private String fundedBy;
    @ExcelCell(15)
    private String studyType;
    @ExcelCell(16)
    private String studyDesign;
    @ExcelCell(17)
    private String otherId;
    @ExcelCell(18)
    private String startDate;
    @ExcelCell(19)
    private String primaryCompletionDate;
    @ExcelCell(20)
    private String completionDate;
    @ExcelCell(21)
    private String firstPosted;
    @ExcelCell(22)
    private String resultFirstPosted;
    @ExcelCell(23)
    private String lastUpdatePosted;
    @ExcelCell(24)
    private String locations;
    @ExcelCell(25)
    private String studyDocuments;
    @ExcelCell(26)
    private String url;
}

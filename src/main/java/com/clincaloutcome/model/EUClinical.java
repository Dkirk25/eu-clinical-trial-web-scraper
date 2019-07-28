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
    private String trialProtocol;
    @ExcelCell(10)
    private String trialResult;
    @ExcelCell(11)
    private String primaryEndPoint;
    @ExcelCell(12)
    private String secondaryEndPoint;

    public String getEudraNumber() {
        return eudraNumber;
    }

    public void setEudraNumber(String eudraNumber) {
        this.eudraNumber = eudraNumber;
    }

    public String getSponsorProtocolNumber() {
        return sponsorProtocolNumber;
    }

    public void setSponsorProtocolNumber(String sponsorProtocolNumber) {
        this.sponsorProtocolNumber = sponsorProtocolNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPopulationAge() {
        return populationAge;
    }

    public void setPopulationAge(String populationAge) {
        this.populationAge = populationAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTrialProtocol() {
        return trialProtocol;
    }

    public void setTrialProtocol(String trialProtocol) {
        this.trialProtocol = trialProtocol;
    }

    public String getTrialResult() {
        return trialResult;
    }

    public void setTrialResult(String trialResult) {
        this.trialResult = trialResult;
    }

    public String getPrimaryEndPoint() {
        return primaryEndPoint;
    }

    public void setPrimaryEndPoint(String primaryEndPoint) {
        this.primaryEndPoint = primaryEndPoint;
    }

    public String getSecondaryEndPoint() {
        return secondaryEndPoint;
    }

    public void setSecondaryEndPoint(String secondaryEndPoint) {
        this.secondaryEndPoint = secondaryEndPoint;
    }
}

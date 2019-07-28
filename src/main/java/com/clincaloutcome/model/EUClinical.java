package com.clincaloutcome.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelCellName;

public class EUClinical {

    @ExcelCellName("EudraCT Number")
    private String eudraNumber;
    @ExcelCellName("Sponsor Protocol Number")
    private String sponsorProtocolNumber;
    @ExcelCellName("Start Date")
    private String startDate;
    @ExcelCellName("Sponsor Name")
    private String sponsorName;
    @ExcelCellName("Full Title")
    private String fullTitle;
    @ExcelCellName("Medical Condition")
    private String medicalCondition;
    @ExcelCellName("Disease")
    private String disease;
    @ExcelCellName("Population Age")
    private String populationAge;
    @ExcelCellName("Gender")
    private String gender;
    @ExcelCellName("Trial Protocol")
    private String trialProtocol;
    @ExcelCellName("Trial Result")
    private String trialResult;
    @ExcelCellName("Primary End Points")
    private String primaryEndPoint;
    @ExcelCellName("Secondary End points")
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

    public void setTrailProtocol(String trialProtocol) {
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

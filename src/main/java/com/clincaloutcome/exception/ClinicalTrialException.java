package com.clincaloutcome.exception;

public class ClinicalTrialException extends RuntimeException {
    public ClinicalTrialException(String message) {
        super(message);
    }

    public ClinicalTrialException(String message, Throwable cause) {
        super(message, cause);
    }
}

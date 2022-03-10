package com.example.pmu_project.exception;

public class EmptyDatabaseException extends Exception{
    public EmptyDatabaseException(String errorMessage) {
        super(errorMessage);
    }
}

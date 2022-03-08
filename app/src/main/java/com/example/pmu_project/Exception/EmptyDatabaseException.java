package com.example.pmu_project.Exception;

public class EmptyDatabaseException extends Exception{
    public EmptyDatabaseException(String errorMessage) {
        super(errorMessage);
    }
}

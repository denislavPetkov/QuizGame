package com.example.pmu_project.Exceptions;

public class EmptyDatabaseException extends Exception{
    public EmptyDatabaseException(String errorMessage) {
        super(errorMessage);
    }
}

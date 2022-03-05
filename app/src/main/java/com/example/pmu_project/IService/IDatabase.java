package com.example.pmu_project.IService;

import android.content.Context;

import java.util.List;

public interface IDatabase {
    public void AddResultRecord (IResults results);
    public List<String> GetResultsRecordsString();
    public void DeleteSavedResults();
}

package com.example.pmu_project.IService;

import android.content.Context;

import com.example.pmu_project.Entity.Question;
import com.example.pmu_project.Exceptions.EmptyDatabaseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDatabase {
    public void LoadDataFromFile ();
    public List<String> GetResultsRecordsString() throws EmptyDatabaseException;
    public void DeleteSavedResults();
    public int GetAllQuestionsInt();
    public void AddAnsweredQuestion(Question question, String userAnswer);
    public Question GetQuestion(int x) throws EmptyDatabaseException;
    public Map<Question, String> GetLastXQuestionsAndAnswers(int x) throws EmptyDatabaseException;
}

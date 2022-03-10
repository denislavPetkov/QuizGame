package com.example.pmu_project.service;

import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;

import java.util.List;
import java.util.Map;

public interface DatabaseService {
    public void LoadDataFromFile ();
    public List<String> GetResultsRecordsString() throws EmptyDatabaseException;
    public void DeleteSavedResults();
    public int GetAllQuestionsInt();
    public void AddAnsweredQuestion(Question question, String userAnswer);
    public Question GetQuestion(int x) throws EmptyDatabaseException;
    public Map<Question, String> GetLastXQuestionsAndAnswers(int x) throws EmptyDatabaseException;
}

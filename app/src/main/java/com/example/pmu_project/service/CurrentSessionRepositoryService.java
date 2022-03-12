package com.example.pmu_project.service;

import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CurrentSessionRepositoryService {
    public int GetAnsweredQuestionsInt();
    public int GetCorrectlyAnsweredQuestionsInt();
    public List<String> GetResultsRecordsString() throws EmptyDatabaseException;
    public void DeleteSavedResults();
    public HashMap<Question, String> GetQuestionsAndAnswers() throws EmptyDatabaseException;
    public void AddAnsweredQuestions(HashMap<Question, String> questionAndAnswers);
    public boolean SavedSession();

}

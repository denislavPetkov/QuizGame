package com.example.pmu_project.IService;

import android.content.Context;

import com.example.pmu_project.Entity.Question;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDatabase {
//    public void AddResultRecord (IResults results);
    public void LoadDataFromFile ()  throws IOException;
    public List<String> GetResultsRecordsString();
    public void DeleteSavedResults();
    public int GetAllQuestionsInt();
    public void AddAnsweredQuestion(Question question, String userAnswer);
    public List<Question> GetCorrectlyAnsweredQuestions();
    public Question GetQuestion(int x);
    public Map<Question, String> GetLastXQuestionsAndAnswers(int x);
}

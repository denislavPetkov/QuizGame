package com.example.pmu_project.IService;

import android.content.Context;

import com.example.pmu_project.Entity.Question;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IDatabase {
    public void LoadDataFromFile ()  throws IOException;
    public List<String> GetResultsRecordsString();
    public void DeleteSavedResults();
    public void AddAnsweredQuestion(Question question, String userAnswer);
    public Question GetQuestion(int x);
    public Map<Question, String> GetLastXQuestionsAndAnswers(int x);
}

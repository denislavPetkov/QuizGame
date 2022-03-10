package com.example.pmu_project.service.impl;

import com.example.pmu_project.service.ResultsService;

public class ResultsServiceImpl implements ResultsService {

    private int correctlyAnsweredQuestionsInt = 0;
    private int answeredQuestions = 1;

    public int GetCorrectlyAnsweredQuestionsInt() {
        return correctlyAnsweredQuestionsInt;
    }

    public void IncrementCorrectlyAnsweredQuestions(){
        correctlyAnsweredQuestionsInt++;
    }

    public void IncrementAnsweredQuestions(){
        answeredQuestions++;
    }

    public int GetAnsweredQuestions() {
        return answeredQuestions;
    }
}

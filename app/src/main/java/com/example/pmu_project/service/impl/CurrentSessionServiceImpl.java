package com.example.pmu_project.service.impl;

import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.service.CurrentSessionService;

import java.util.HashMap;

public class CurrentSessionServiceImpl implements CurrentSessionService {
    private HashMap<Question, String> questionsAndAnswers;

    public CurrentSessionServiceImpl() {
        questionsAndAnswers = new HashMap<>();
    }

    public void AddAnsweredQuestion(Question question, String userAnswer){
            questionsAndAnswers.put(question, userAnswer);
    }

    public HashMap<Question, String> GetAnsweredQuestions(){
        return questionsAndAnswers;
    }
    public int GetAnsweredQuestionsInt(){
        return questionsAndAnswers.size();
    }
}

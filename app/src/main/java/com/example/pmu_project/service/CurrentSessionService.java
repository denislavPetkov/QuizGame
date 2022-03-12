package com.example.pmu_project.service;

import com.example.pmu_project.data.enteties.Question;

import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;

public interface CurrentSessionService {
    public void AddAnsweredQuestion(Question question, String userAnswer);
    public HashMap<Question, String> GetAnsweredQuestions();
    public int GetAnsweredQuestionsInt();
}

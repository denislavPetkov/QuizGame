package com.example.pmu_project.service;

import com.example.pmu_project.data.enteties.Question;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;

public interface CurrentSessionService extends Serializable {
    public void AddAnsweredQuestion(Question question, String userAnswer);
    public HashMap<Question, String> GetAnsweredQuestions();
}

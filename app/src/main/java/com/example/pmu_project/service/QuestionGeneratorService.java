package com.example.pmu_project.service;

import com.example.pmu_project.data.entities.Question;

import java.io.Serializable;

public interface QuestionGeneratorService extends Serializable {
    public Question GetQuestion();
    public int GetAllQuestionsInt();
}

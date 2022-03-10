package com.example.pmu_project.service;

import com.example.pmu_project.data.enteties.Question;

public interface QuestionGeneratorService {
    public Question GetQuestion();
    public int GetAllQuestionsInt();
}

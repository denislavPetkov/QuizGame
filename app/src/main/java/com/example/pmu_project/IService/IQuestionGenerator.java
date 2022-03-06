package com.example.pmu_project.IService;

import com.example.pmu_project.Entity.Question;

public interface IQuestionGenerator {
    public Question GetQuestion();
    public int GetAllQuestionsInt();
}

package com.example.pmu_project.service;

import com.example.pmu_project.data.entities.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;

public interface QuestionRepositoryService {
    public int GetAllQuestionsInt();
    public Question GetQuestion(int x) throws EmptyDatabaseException;
}

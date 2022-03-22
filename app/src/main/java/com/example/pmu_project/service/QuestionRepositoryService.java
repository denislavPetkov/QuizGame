package com.example.pmu_project.service;

import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;

import java.util.List;
import java.util.Map;

public interface QuestionRepositoryService {
    public int GetAllQuestionsInt();
    public Question GetQuestion(int x) throws EmptyDatabaseException;
}

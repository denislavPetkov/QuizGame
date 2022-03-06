package com.example.pmu_project.IService;

import com.example.pmu_project.Entity.Question;

import java.util.List;
import java.util.Map;

public interface IResults {
    public int GetAnsweredQuestions();

    public void IncrementCorrectlyAnsweredQuestions();
    public void IncrementAnsweredQuestions();
}

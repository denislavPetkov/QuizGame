package com.example.pmu_project.Service;

import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.IService.IResults;
import com.example.pmu_project.Entity.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results implements IResults {

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

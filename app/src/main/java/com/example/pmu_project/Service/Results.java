package com.example.pmu_project.Service;

import com.example.pmu_project.IService.IResults;
import com.example.pmu_project.Entity.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Results implements IResults {

//    private static int correctlyAnsweredQuestionsInt = 0;
    private static int allQuestions = 0;
    private static Map<Question,String> wronglyAnsweredQuestions = null;
    private static List<Question> correctlyAnsweredQuestions = null;

    public Results (int allQuestions) {
        this.allQuestions = allQuestions;
        if (wronglyAnsweredQuestions == null){
            wronglyAnsweredQuestions = new HashMap<>();
        }
        if (correctlyAnsweredQuestions == null){
            correctlyAnsweredQuestions = new ArrayList<>();
        }
        return;
    }
    public int GetAnsweredQuestionsInt(){
        return correctlyAnsweredQuestions.size();
    }

    @Override
    public int GetAllQuestions() {
        return allQuestions;
    }

    @Override
    public void AddCorrectlyAnsweredQuestion(Question question) {
        correctlyAnsweredQuestions.add(question);
        return;
    }

    @Override
    public void AddCWronglyAnsweredQuestion(Question question, String givenAnswer) {
        wronglyAnsweredQuestions.put(question, givenAnswer);
        return;
    }

    @Override
    public List<Question> GetCorrectlyAnsweredQuestions() {
        return correctlyAnsweredQuestions;
    }

    @Override
    public Map<Question, String> GetWronglyAnsweredQuestions() {
        return wronglyAnsweredQuestions;
    }

}

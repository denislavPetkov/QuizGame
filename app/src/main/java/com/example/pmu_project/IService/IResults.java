package com.example.pmu_project.IService;

import com.example.pmu_project.Entity.Question;

import java.util.List;
import java.util.Map;

public interface IResults {
    public int GetAnsweredQuestionsInt();
    public int GetAllQuestions();
    public void AddCorrectlyAnsweredQuestion(Question question);
    public void AddCWronglyAnsweredQuestion(Question question,String givenAnswer);
    public List<Question> GetCorrectlyAnsweredQuestions();
    public Map<Question,String> GetWronglyAnsweredQuestions();
}

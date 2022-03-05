package com.example.pmu_project;

import com.example.pmu_project.IService.IQuestionGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator implements IQuestionGenerator {

    public static Question currentQuestion = null;
    private static List<Question> questions = null;

    public QuestionGenerator(){
        GenerateQuestions();
    }
    private void GenerateQuestions() {
        if (questions == null){
            questions = new ArrayList<Question>();
            questions.add(new Question("тест1?", "тест1"));
            questions.add(new Question("тест2?", "тест2"));

        }
    }

    public Question GetQuestion() {
        int x;
        if (questions.size() != 1){
            Random ran = new Random();
            x = ran.nextInt(questions.size()-1);
        } else {
            x = 0;
        }

        return questions.remove(x);
    }
}

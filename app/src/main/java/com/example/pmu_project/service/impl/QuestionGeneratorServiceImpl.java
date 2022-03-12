package com.example.pmu_project.service.impl;



import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.QuestionRepositoryService;
import com.example.pmu_project.service.QuestionGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGeneratorServiceImpl implements QuestionGeneratorService {

    private  List<Question> questions = null;
    private  int numberOfQuestions = 0;

    private QuestionRepositoryService db;

    public QuestionGeneratorServiceImpl() {}

    public QuestionGeneratorServiceImpl(QuestionRepositoryService db) {
        this.db = db;
        GenerateQuestions();
    }

    private void GenerateQuestions(){
            questions = new ArrayList<Question>();
            loadFromDatabase();
            numberOfQuestions = questions.size();
    }

    private void loadFromDatabase(){
        int numberOfQuestionsInDatabase = db.GetAllQuestionsInt();

        int desiredNumOfQuestions = 3;

        int x;

        List<Integer> addedQuestions = new ArrayList<>();

        while (addedQuestions.size() < desiredNumOfQuestions) {
            Random ran = new Random();
            x = ran.nextInt(numberOfQuestionsInDatabase) + 1;
            if (addedQuestions.contains(x)) {
                continue;
            }
            try {
                questions.add(db.GetQuestion(x));
            } catch (EmptyDatabaseException e) {
                e.printStackTrace();
                return;
            }
            addedQuestions.add(x);
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

    public int GetAllQuestionsInt(){
        return numberOfQuestions;
    }


}

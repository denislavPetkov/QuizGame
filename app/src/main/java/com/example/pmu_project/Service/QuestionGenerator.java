package com.example.pmu_project.Service;



import com.example.pmu_project.Entity.Question;
import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.IService.IQuestionGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.json.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionGenerator implements IQuestionGenerator {

    private  List<Question> questions = null;
    private  int numberOfQuestions = 0;

    private  IDatabase db;

    public QuestionGenerator() {

    }

    public QuestionGenerator(IDatabase db) throws IOException {
        this.db = db;
        GenerateQuestions();
    }

    private void GenerateQuestions(){
            questions = new ArrayList<Question>();
            loadFromDatabase();
            numberOfQuestions = questions.size();
    }

    private void loadFromDatabase(){
        int allQuestions = 5;

        int desiredNumOfQuestions = 5;

        int x;

        List<Integer> addedQuestions = new ArrayList<>();

        while (addedQuestions.size() < desiredNumOfQuestions) {
            Random ran = new Random();
            x = ran.nextInt(allQuestions) + 1;
            if (addedQuestions.contains(x)) {
                continue;
            }
            questions.add(db.GetQuestion(x));
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

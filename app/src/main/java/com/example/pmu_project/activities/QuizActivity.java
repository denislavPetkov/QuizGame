package com.example.pmu_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pmu_project.MainActivity;
import com.example.pmu_project.R;
import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.example.pmu_project.service.CurrentSessionService;
import com.example.pmu_project.service.QuestionRepositoryService;
import com.example.pmu_project.service.QuestionGeneratorService;
import com.example.pmu_project.service.impl.CurrentSessionServiceImpl;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;
import com.example.pmu_project.service.impl.QuestionGeneratorServiceImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    public final static String currentSessionDataExtra = "currentSession";

    private TextView questionTextView;
    private TextView answerTextView;

    private Button submitAnswerButton;
    private Button saveResultsGoToMenuButton;

    private final QuestionRepositoryService db = new RepositoryServiceImpl(this);

    private QuestionGeneratorService questionGenerator = null;
    private Question question;
    private CurrentSessionService currentSession;
    private HashMap<Question, String> lastSession;

    private int currentQuestion=0;
    private int allQuestions=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerEditText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);
        saveResultsGoToMenuButton = findViewById(R.id.saveResultsGoToMenuButton);

        Intent intent = getIntent();
        if (lastSession == null){
            lastSession = (HashMap<Question, String>) intent.getSerializableExtra(QuizActivity.currentSessionDataExtra);
        }
        if (lastSession != null) {
            allQuestions = lastSession.size();
            CurrentSessionRepositoryService db = new RepositoryServiceImpl(this);
            currentQuestion = db.GetAnsweredQuestionsInt();
        }

        questionGenerator = new QuestionGeneratorServiceImpl(db);
        currentSession = new CurrentSessionServiceImpl();

        generateQuestion();
        updateTitle();

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSession.AddAnsweredQuestion(question,  answerTextView.getText().toString());
                if (currentQuestion == questionGenerator.GetAllQuestionsInt()) {
                    QuizActivity.this.startActivity(new Intent(QuizActivity.this, ResultsActivity.class).putExtra(currentSessionDataExtra,
                            (Serializable) currentSession.GetAnsweredQuestions()));
                    return;
                }
                generateQuestion();
                answerTextView.setText("");
                updateTitle();
                return;
            }
        });

        saveResultsGoToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSession.AddAnsweredQuestion(question, null);

                if (lastSession == null){
                    for (int i = currentQuestion; i < allQuestions; i++){
                        currentSession.AddAnsweredQuestion( questionGenerator.GetQuestion(), null);
                    }
                }


                CurrentSessionRepositoryService db = new RepositoryServiceImpl(QuizActivity.this);
                db.AddAnsweredQuestions(currentSession.GetAnsweredQuestions());

                QuizActivity.this.startActivity(new Intent(QuizActivity.this, MainActivity.class));
                return;
            }
        });
    }

    private void updateTitle(){
        currentQuestion++;
        setTitle("Въпрос " + currentQuestion + "/" + allQuestions);
    }

    private void generateQuestion() {
        if (lastSession != null){
            generateQuestionFromCurrentSession(lastSession);
            return;
        }
        allQuestions = questionGenerator.GetAllQuestionsInt();
        question = questionGenerator.GetQuestion();
        questionTextView.setText(question.getQuestion());
    }

    private void generateQuestionFromCurrentSession(HashMap<Question, String> lastSession){

        for (Map.Entry<Question, String> entry : lastSession.entrySet()) {
                if (entry.getValue() == null){
                    question = entry.getKey();
                    questionTextView.setText(question.getQuestion());
                    lastSession.remove(entry.getKey());
                    break;
                }
        }

    }

    private int getAllAnsweredQuestions(){
        int x = 0;
        for (Map.Entry<Question, String> entry : lastSession.entrySet()) {
            if (entry.getValue() != null){
                x++;
            }
        }
        return x;
    }

}
package com.example.pmu_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pmu_project.R;
import com.example.pmu_project.data.entities.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.example.pmu_project.service.CurrentSessionService;
import com.example.pmu_project.service.MessageHelperService;
import com.example.pmu_project.service.QuestionRepositoryService;
import com.example.pmu_project.service.QuestionGeneratorService;
import com.example.pmu_project.service.impl.CurrentSessionServiceImpl;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;
import com.example.pmu_project.service.impl.QuestionGeneratorServiceImpl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.WordUtils;

public class QuizActivity extends AppCompatActivity {

    public final static String currentSessionDataExtra = "currentSession";
    public final static String numberOfQuestionsExtra = "numberOfQuestions";

    public final static String currentSessionPreviousState = "currentSessionPreviousState";
    public final static String currentQuestionPreviousState = "currentQuestionPreviousState";
    public final static String allQuestionsPreviousState = "allQuestionsPreviousState";
    public final static String questionPreviousState = "questionPreviousState";
    public final static String questionGeneratorPreviousState = "questionGeneratorPreviousState";

    private TextView questionTextView;
    private TextView answerTextView;

    private Button submitAnswerButton;
    private Button saveResultsGoToMenuButton;

    private final QuestionRepositoryService questionRepository = new RepositoryServiceImpl(this);
    private final CurrentSessionRepositoryService currentSessionRepository = new RepositoryServiceImpl(this);

    private QuestionGeneratorService questionGenerator = null;
    private Question question;
    private CurrentSessionService currentSession;
    private HashMap<Question, String> lastSession;

    private int currentQuestion=0;
    private int allQuestions=0;
    private int numberOfQuestionsInt=0;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(allQuestionsPreviousState, allQuestions);
        savedInstanceState.putInt(currentQuestionPreviousState, currentQuestion);
        savedInstanceState.putSerializable(currentSessionPreviousState, (Serializable) currentSession);
        savedInstanceState.putSerializable(questionPreviousState, question);
            if (currentQuestion != allQuestions && currentSession.GetAnsweredQuestions().size() != allQuestions){
                savedInstanceState.putSerializable(questionGeneratorPreviousState, (Serializable) questionGenerator);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerEditText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);
        saveResultsGoToMenuButton = findViewById(R.id.saveResultsGoToMenuButton);

        Intent intent = getIntent();
        lastSession = (HashMap<Question, String>) intent.getSerializableExtra(QuizActivity.currentSessionDataExtra);
        Serializable questions = intent.getSerializableExtra(QuizActivity.numberOfQuestionsExtra);

        if (savedInstanceState != null){
            currentSession = (CurrentSessionService) savedInstanceState.getSerializable(currentSessionPreviousState);
            currentQuestion = savedInstanceState.getInt(currentQuestionPreviousState);
            allQuestions = savedInstanceState.getInt(allQuestionsPreviousState);
            questionGenerator = (QuestionGeneratorService) savedInstanceState.getSerializable(questionGeneratorPreviousState);

            question = (Question) savedInstanceState.getSerializable(questionPreviousState);
            questionTextView.setText(question.getQuestion());
        } else {
            currentSession = new CurrentSessionServiceImpl();

            if (questions != null){
                numberOfQuestionsInt = (Integer) intent.getSerializableExtra(QuizActivity.numberOfQuestionsExtra);
            }

            if(lastSession == null){
                handleNewQuiz(numberOfQuestionsInt);
            } else {
                handleLastSession();
            }

            generateQuestion();
        }
        updateTitle();


        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSession.AddAnsweredQuestion(question,  WordUtils.capitalize(answerTextView.getText().toString().trim()));
                if (currentQuestion == allQuestions) {
                    QuizActivity.this.startActivity(new Intent(QuizActivity.this, ResultsActivity.class).putExtra(currentSessionDataExtra,
                            currentSession.GetAnsweredQuestions()));
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
                if(lastSession == null) {
                    currentSession.AddAnsweredQuestion(question, null);
                    for (int i = currentQuestion; i < allQuestions; i++) {
                        currentSession.AddAnsweredQuestion(questionGenerator.GetQuestion(), null);
                    }
                }
                currentSessionRepository.AddAnsweredQuestions(currentSession.GetAnsweredQuestions());
                QuizActivity.this.startActivity(new Intent(QuizActivity.this, MainActivity.class));
                MessageHelperService.ShowMessage(QuizActivity.this,"?????????????? ?? ???????????????? ??????????????!");
                return;
            }
        });
    }

    private void handleLastSession() {
        allQuestions = lastSession.size();
        currentQuestion = currentSessionRepository.GetAnsweredQuestionsInt();
    }

    private void handleNewQuiz(int questions) {
        questionGenerator = new QuestionGeneratorServiceImpl(questionRepository,questions);
        allQuestions = questionGenerator.GetAllQuestionsInt();
    }

    private void updateTitle(){
        setTitle("???????????? " + currentQuestion + "/" + allQuestions);
    }

    private void generateQuestion() {
        currentQuestion++;
        if (lastSession != null){
            generateQuestionFromLastSessionSession(lastSession);
            return;
        }
        question = questionGenerator.GetQuestion();
        questionTextView.setText(question.getQuestion());
    }

    private void generateQuestionFromLastSessionSession(HashMap<Question, String> lastSession){

        for (Map.Entry<Question, String> entry : lastSession.entrySet()) {
                if (entry.getValue() == null){
                    question = entry.getKey();
                    questionTextView.setText(question.getQuestion());
                    lastSession.remove(entry.getKey());
                    break;
                }
        }
    }

}
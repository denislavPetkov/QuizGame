package com.example.pmu_project.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pmu_project.R;
import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.service.DatabaseService;
import com.example.pmu_project.service.QuestionGeneratorService;
import com.example.pmu_project.service.ResultsService;
import com.example.pmu_project.service.impl.DatabaseServiceImpl;
import com.example.pmu_project.service.impl.QuestionGeneratorServiceImpl;
import com.example.pmu_project.service.impl.ResultsServiceImpl;

public class QuizActivity extends AppCompatActivity {

    private long delayMs = 1000;

    private TextView questionTextView;
    private TextView answerTextView;
    private Button submitAnswerButton;

    private final DatabaseService db = new DatabaseServiceImpl(this);

    static private QuestionGeneratorService questionGenerator = null;

    static private ResultsService results = null;

    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if (questionGenerator == null) {
            questionGenerator = new QuestionGeneratorServiceImpl(db);
        }
        if (results == null) {
            results = new ResultsServiceImpl();
        }

        setTitle("Въпрос " + results.GetAnsweredQuestions() + "/" + questionGenerator.GetAllQuestionsInt());

        alertDialogBuilder = new AlertDialog.Builder(this);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerEditText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);

        Question question = questionGenerator.GetQuestion();
        questionTextView.setText(question.getQuestion());

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerTextView.getText().toString().equals(question.getAnswer())) {
                    db.AddAnsweredQuestion(question, question.getAnswer());
                    results.IncrementCorrectlyAnsweredQuestions();
                } else {
                    db.AddAnsweredQuestion(question, answerTextView.getText().toString());
                }

                if (results.GetAnsweredQuestions() >= questionGenerator.GetAllQuestionsInt()) {
                    QuizActivity.this.startActivity(new Intent(QuizActivity.this, ResultsActivity.class));
                    return;
                }
                results.IncrementAnsweredQuestions();
                QuizActivity.this.startActivity(new Intent(QuizActivity.this, QuizActivity.class));
                return;
            }
        });
    }

    public static ResultsService GetResults() {
        return results;
    }

    public static void ResetQuestions() {
        questionGenerator = null;
    }

    public static void ResetResults() {
        results = null;
    }
}
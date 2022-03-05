package com.example.pmu_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pmu_project.Entity.Question;
import com.example.pmu_project.IService.IQuestionGenerator;
import com.example.pmu_project.IService.IResults;
import com.example.pmu_project.Service.QuestionGenerator;
import com.example.pmu_project.Service.Results;

public class MainActivity extends AppCompatActivity {

    private long delayMs = 1000;

    private TextView questionTextView;
    private TextView totalQuestionsTextView;
    private TextView answerTextView;
    private Button submitAnswerButton;

    static private IQuestionGenerator questionGeneratorInterface = new QuestionGenerator();
    static private IResults resultsInterface = new Results(questionGeneratorInterface.GetAllQustionsInt());

    private static int currentQuestion = 0;

    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentQuestion++;

        alertDialogBuilder = new AlertDialog.Builder(this);
        totalQuestionsTextView = findViewById(R.id.totalQuestionsTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerEditText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);

        totalQuestionsTextView.setText(currentQuestion + "/" + questionGeneratorInterface.GetAllQustionsInt());

        Question question = questionGeneratorInterface.GetQuestion();
        questionTextView.setText(question.getQuestion());


        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answerTextView.getText().toString().equals(question.getAnswer())){
                    resultsInterface.AddCorrectlyAnsweredQuestion(question);
                } else {
                    resultsInterface.AddCWronglyAnsweredQuestion(question, answerTextView.getText().toString());
                }

                if (currentQuestion == questionGeneratorInterface.GetAllQustionsInt()){
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ResultsActivity.class));
                    return;
                }
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }

    public static IResults GetResults(){
        return resultsInterface;
    }

}
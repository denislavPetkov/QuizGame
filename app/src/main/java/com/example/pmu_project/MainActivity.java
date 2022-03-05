package com.example.pmu_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pmu_project.IService.IMessageHelper;
import com.example.pmu_project.IService.IQuestionGenerator;

public class MainActivity extends AppCompatActivity {

    private long delayMs = 1000;

    private TextView questionTextView;
    private TextView answerTextView;
    private Button submitAnswerButton;

    private IQuestionGenerator questionGenerator = new QuestionGenerator();

    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertDialogBuilder = new AlertDialog.Builder(this);

        questionTextView = findViewById(R.id.questionTextView);
        answerTextView = findViewById(R.id.answerEditText);
        submitAnswerButton = findViewById(R.id.submitAnswerButton);

        Question question = questionGenerator.GetQuestion();
        questionTextView.setText(question.getQuestion());

        submitAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answerTextView.getText().toString().equals(question.getAnswer())){
                    IMessageHelper.showMessage(alertDialogBuilder, "Wrong!", delayMs);
                }
                IMessageHelper.showMessage(alertDialogBuilder, "GJ!", delayMs);
                MainActivity.this.startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
    }


}
package com.example.pmu_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.IService.IQuestionGenerator;
import com.example.pmu_project.Service.Database;
import com.example.pmu_project.Service.QuestionGenerator;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    Button showPreviousResultsButton;
    Button doQuizButton;

    private final IDatabase db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuizActivity.ResetResults();
        QuizActivity.ResetQuestions();

            try {
                db.LoadDataFromFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


        showPreviousResultsButton = findViewById(R.id.showPreviousResultsButton);
        doQuizButton = findViewById(R.id.doQuizButton);

        showPreviousResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, PreviousResultsActivity.class));
                return;
            }
        });

        doQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, new QuizActivity().getClass()));
                return;
            }
        });
    }


}
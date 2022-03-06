package com.example.pmu_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmu_project.Entity.Question;
import com.example.pmu_project.Exceptions.EmptyDatabaseException;
import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.Service.Database;
import com.example.pmu_project.IService.IResults;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    private TextView correctAnswersTextView;

    private ListView helperListView;

    private Button answeredQuestions;
    private Button mainMenuButton;

    private IDatabase db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        helperListView = findViewById(R.id.helperListView);

        answeredQuestions = findViewById(R.id.allAnsweredQuestionsButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);


        try {
            int allQuestionsAnswered = QuizActivity.GetResults().GetAnsweredQuestions();

            Map<Question, String> questionsAndUserAnswers = db.GetLastXQuestionsAndAnswers(allQuestionsAnswered);

            int correctlyAnsweredQuestions = GetCorrectlyAnsweredQuestions(questionsAndUserAnswers);

            correctAnswersTextView.setText(String.valueOf("Правилно отговорени " +
                    correctlyAnsweredQuestions + "  от " +
                    allQuestionsAnswered + " въпроса"));

            answeredQuestions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> questionsAndAnswers = new ArrayList<String>();

                    for (Map.Entry<Question, String> entry : questionsAndUserAnswers.entrySet()) {
                        questionsAndAnswers.add(entry.getKey().toString() + ". Твоят отговор: " + entry.getValue());
                    }
                    ArrayAdapter adapter = new ArrayAdapter<String>(ResultsActivity.this,android.R.layout.simple_list_item_1,questionsAndAnswers);
                    helperListView.setAdapter(adapter);
                }
            });

        } catch (EmptyDatabaseException e) {
            Toast toast = Toast.makeText(this, "Грешка при записването на резултатите!", Toast.LENGTH_SHORT);
            toast.show();
            ResultsActivity.this.startActivity(new Intent(ResultsActivity.this, MainActivity.class));
            return;
        }

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultsActivity.this.startActivity(new Intent(ResultsActivity.this, MainActivity.class));
                return;
            }
        });

    }

    private int GetCorrectlyAnsweredQuestions(Map<Question, String> questionsAndUserAnswers){
        int x = 0;

        for (Map.Entry<Question, String> entry : questionsAndUserAnswers.entrySet()) {
            if (entry.getKey().getAnswer().equals(entry.getValue())){
                x++;
            }
        }

        return x;
    }
}
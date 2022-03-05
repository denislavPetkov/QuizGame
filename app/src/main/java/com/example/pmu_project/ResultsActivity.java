package com.example.pmu_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.Service.Database;
import com.example.pmu_project.Entity.Question;
import com.example.pmu_project.IService.IResults;

import java.util.ArrayList;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    private TextView correctAnswersTextView;

    private ListView helperListView;

    private Button wronglyAnsweredQuestions;
    private Button correctlyAnsweredQuestions;
    private Button mainMenuButton;

    private IDatabase db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        helperListView = findViewById(R.id.helperListView);

        wronglyAnsweredQuestions = findViewById(R.id.wronglyAnsweredQuestionsButton);
        correctlyAnsweredQuestions = findViewById(R.id.correctlyAnsweredQuestionsButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);

        IResults results = QuizActivity.GetResults();
        correctAnswersTextView.setText(String.valueOf("Правилно отговорени " +
                results.GetAnsweredQuestionsInt() + "  от " +
                results.GetAllQuestions() + " въпроса"));

        db.AddResultRecord(results);

        correctlyAnsweredQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> questionsAndAnswers = new ArrayList<String>();
                for (Question q : results.GetCorrectlyAnsweredQuestions()){
                    questionsAndAnswers.add(q.toString());
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(ResultsActivity.this,android.R.layout.simple_list_item_1,questionsAndAnswers);
                helperListView.setAdapter(adapter);
            }
        });

        wronglyAnsweredQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> questionsAndAnswers = new ArrayList<String>();
                Map<Question,String> wronglyAnsweredQuestions = results.GetWronglyAnsweredQuestions();

                for (Map.Entry<Question, String> entry : wronglyAnsweredQuestions.entrySet()) {
                    questionsAndAnswers.add(entry.getKey().toString() + ". Твоят отговор: " + entry.getValue()); // .toString()
                }
                ArrayAdapter adapter = new ArrayAdapter<String>(ResultsActivity.this,android.R.layout.simple_list_item_1,questionsAndAnswers);
                helperListView.setAdapter(adapter);
            }
        });

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultsActivity.this.startActivity(new Intent(ResultsActivity.this, MainActivity.class));
            }
        });

    }
}
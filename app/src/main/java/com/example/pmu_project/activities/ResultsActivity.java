package com.example.pmu_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmu_project.MainActivity;
import com.example.pmu_project.R;
import com.example.pmu_project.data.enteties.Question;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;

import java.util.HashMap;


public class ResultsActivity extends AppCompatActivity {

    private TextView correctAnswersTextView;

    private ListView helperListView;

    private Button mainMenuButton;
    private Button shareResultsButton;

    private int correctlyAnsweredQuestions = 0;

    private CurrentSessionRepositoryService currentSessionRepository = new RepositoryServiceImpl(this);

    private HashMap<Question, String> currentSessionQuestionsAndAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle("Резултати");

        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        helperListView = findViewById(R.id.helperListView);
        mainMenuButton = findViewById(R.id.mainMenuButton);
        shareResultsButton = findViewById(R.id.shareResultsButton);

        handleCurrentSession();
        handleResults();

        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultsActivity.this.startActivity(new Intent(ResultsActivity.this, MainActivity.class));
                return;
            }
        });

        shareResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTwitter(ResultsActivity.this, correctlyAnsweredQuestions);
            }
        });

    }

    private void handleCurrentSession(){
        Intent intent = getIntent();
        if (intent != null)
        {
            currentSessionQuestionsAndAnswers =(HashMap<Question, String>) intent.getSerializableExtra(QuizActivity.currentSessionDataExtra);
            currentSessionRepository.AddAnsweredQuestions(currentSessionQuestionsAndAnswers);
        }
    }

    private void handleResults(){
        try {
            int allQuestionsAnswered = currentSessionRepository.GetAnsweredQuestionsInt();
            correctlyAnsweredQuestions = currentSessionRepository.GetCorrectlyAnsweredQuestionsInt();

            correctAnswersTextView.setText(String.valueOf("Правилно отговорени " +
                    correctlyAnsweredQuestions + "  от " +
                    allQuestionsAnswered + " въпроса"));

            ArrayAdapter adapter = new ArrayAdapter<String>(ResultsActivity.this,
                    android.R.layout.simple_list_item_1, currentSessionRepository.GetResultsRecordsString());

            helperListView.setAdapter(adapter);

        } catch (EmptyDatabaseException e) {
            Toast toast = Toast.makeText(this, "Грешка при записването на резултатите!", Toast.LENGTH_SHORT);
            toast.show();
            ResultsActivity.this.startActivity(new Intent(ResultsActivity.this, MainActivity.class));
            return;
        }
    }

    private void startTwitter(Context context, int correctlyAnsweredQuestions) {
        final String message = "https://twitter.com/compose/tweet?text=Здравейте!" +
                "Успях да отговоря на " + correctlyAnsweredQuestions + " въпроса в играта \"Литературен куиз\"." +
                "\nОпитайте се да отговорите на повече въпроси от мен!";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(message));
        try {
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                context.getPackageManager().getPackageInfo("com.twitter.android.lite", 0);
            } catch (PackageManager.NameNotFoundException e1){
                // do nothing and open a browser
            }

        }
        context.startActivity(intent);
    }

}
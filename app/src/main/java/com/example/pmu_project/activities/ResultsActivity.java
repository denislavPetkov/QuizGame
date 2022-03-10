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
import com.example.pmu_project.service.DatabaseService;
import com.example.pmu_project.service.impl.DatabaseServiceImpl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;



public class ResultsActivity extends AppCompatActivity {

    private TextView correctAnswersTextView;

    private ListView helperListView;

    private Button mainMenuButton;
    private Button shareResultsButton;

    int correctlyAnsweredQuestions = 0;
    private DatabaseService db = new DatabaseServiceImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setTitle("Резултати");

        correctAnswersTextView = findViewById(R.id.correctAnswersTextView);
        
        helperListView = findViewById(R.id.helperListView);

        mainMenuButton = findViewById(R.id.mainMenuButton);
        shareResultsButton = findViewById(R.id.shareResultsButton);


        try {
            int allQuestionsAnswered = QuizActivity.GetResults().GetAnsweredQuestions();

            Map<Question, String> questionsAndUserAnswers = db.GetLastXQuestionsAndAnswers(allQuestionsAnswered);

            correctlyAnsweredQuestions = GetCorrectlyAnsweredQuestions(questionsAndUserAnswers);

            correctAnswersTextView.setText(String.valueOf("Правилно отговорени " +
                    correctlyAnsweredQuestions + "  от " +
                    allQuestionsAnswered + " въпроса"));

            ArrayList<String> questionsAndAnswers = new ArrayList<String>();

            for (Map.Entry<Question, String> entry : questionsAndUserAnswers.entrySet()) {
                questionsAndAnswers.add(entry.getKey().toString() + ". Твоят отговор: " + entry.getValue());
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(ResultsActivity.this,
                    android.R.layout.simple_list_item_1, questionsAndAnswers);
            helperListView.setAdapter(adapter);


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
            }
        });

        shareResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTwitter(ResultsActivity.this, correctlyAnsweredQuestions);
            }
        });

    }

    public static void startTwitter(Context context, int correctlyAnsweredQuestions) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/compose/tweet?text=Hey i managed to answer " + correctlyAnsweredQuestions + " questions.\nTry to see how you can do!"));
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

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
//            Log.wtf(TAG, "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    private int GetCorrectlyAnsweredQuestions(Map<Question, String> questionsAndUserAnswers) {
        int x = 0;

        for (Map.Entry<Question, String> entry : questionsAndUserAnswers.entrySet()) {
            if (entry.getKey().getAnswer().equals(entry.getValue())) {
                x++;
            }
        }

        return x;
    }
}
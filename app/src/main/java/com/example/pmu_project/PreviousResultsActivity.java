package com.example.pmu_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.Service.Database;

import java.util.List;

public class PreviousResultsActivity extends AppCompatActivity {

    ListView previousResultsListView;

    Button goToMenuButton;
    Button deleteSavedResultsButton;

    private IDatabase db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_results);

        previousResultsListView = findViewById(R.id.previousResultsListView);

        goToMenuButton = findViewById(R.id.goToMenuButton);
        deleteSavedResultsButton = findViewById(R.id.deleteSavedResultsButton);

        List<String> previousResults = db.GetResultsRecordsString();
        ArrayAdapter adapter = new ArrayAdapter<String>(PreviousResultsActivity.this,android.R.layout.simple_list_item_1,previousResults);
        previousResultsListView.setAdapter(adapter);

        goToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreviousResultsActivity.this.startActivity(new Intent(PreviousResultsActivity.this, MainActivity.class));
            }
        });

        deleteSavedResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.DeleteSavedResults();
                PreviousResultsActivity.this.startActivity(new Intent(PreviousResultsActivity.this, PreviousResultsActivity.class));
            }
        });
    }
}
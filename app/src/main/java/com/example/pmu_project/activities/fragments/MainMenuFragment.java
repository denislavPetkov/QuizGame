package com.example.pmu_project.activities.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pmu_project.R;
import com.example.pmu_project.activities.QuizActivity;
import com.example.pmu_project.activities.ResultsActivity;
import com.example.pmu_project.activities.adapters.MyItemRecyclerViewAdapter;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MainMenuFragment extends Fragment {

    private Button doQuizButton;
    private Button deleteSavedResultsButton;
    private Button continueCurrentSessionButton;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        CurrentSessionRepositoryService db = new RepositoryServiceImpl(this.getContext());

        doQuizButton = view.findViewById(R.id.doQuizButton);
        deleteSavedResultsButton = view.findViewById(R.id.deleteSavedResultsButton);
        continueCurrentSessionButton = view.findViewById(R.id.continueCurrentSessionButton);

        if (!db.SavedSession() ){
            continueCurrentSessionButton.setBackgroundColor(Color.parseColor("#808080"));
            continueCurrentSessionButton.setEnabled(false);
        }

        doQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.DeleteSavedResults();
                MainMenuFragment.this.startActivity(new Intent(MainMenuFragment.this.getContext(), QuizActivity.class));
                return;
            }
        });

        deleteSavedResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.DeleteSavedResults();

                RecyclerView recyclerView = HistoryFragment.GetRecyclerView();

                try {
                    List<String> previousResults = db.GetResultsRecordsString();
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(previousResults));

                } catch (EmptyDatabaseException e) {
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(Collections.emptyList()));
                    // history cleared msg
                }

                continueCurrentSessionButton.setBackgroundColor(Color.parseColor("#808080"));
                continueCurrentSessionButton.setClickable(false);

                return;
            }
        });

        continueCurrentSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainMenuFragment.this.startActivity(new Intent(MainMenuFragment.this.getContext(), QuizActivity.class).putExtra(QuizActivity.currentSessionDataExtra,
                            (Serializable) db.GetQuestionsAndAnswers()));
                } catch (EmptyDatabaseException e) {
                    e.printStackTrace();
                }
                // open quiz with extra
                // in quiz check for extra for info like currentQuestion and left queistions to answer
            }
        });

        return view;
    }

}
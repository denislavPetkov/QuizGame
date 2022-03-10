package com.example.pmu_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.Service.Database;

public class MainMenuFragment extends Fragment {

    Button doQuizButton;
    Button deleteSavedResultsButton;

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

        IDatabase db = new Database(this.getContext());

        doQuizButton = view.findViewById(R.id.doQuizButton);
        deleteSavedResultsButton = view.findViewById(R.id.deleteSavedResultsButton);

        doQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuFragment.this.startActivity(new Intent(MainMenuFragment.this.getContext(), QuizActivity.class));
                return;
            }
        });

        deleteSavedResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.DeleteSavedResults();
                MainMenuFragment.this.startActivity(new Intent(MainMenuFragment.this.getContext(), MainActivity.class));
                return;
            }
        });

        return view;
    }

}
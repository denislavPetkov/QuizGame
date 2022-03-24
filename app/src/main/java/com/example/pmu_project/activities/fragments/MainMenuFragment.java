package com.example.pmu_project.activities.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pmu_project.R;
import com.example.pmu_project.activities.QuizActivity;
import com.example.pmu_project.activities.adapters.MyItemRecyclerViewAdapter;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.example.pmu_project.service.MessageHelperService;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class MainMenuFragment extends Fragment {

    private Button doQuizButton;
    private Button deleteSavedResultsButton;
    private Button continueCurrentSessionButton;
    private String greyColorString = "#808080";

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

        CurrentSessionRepositoryService currentSessionRepository = new RepositoryServiceImpl(this.getContext());

        doQuizButton = view.findViewById(R.id.doQuizButton);
        deleteSavedResultsButton = view.findViewById(R.id.deleteSavedResultsButton);
        continueCurrentSessionButton = view.findViewById(R.id.continueCurrentSessionButton);

        if (!currentSessionRepository.SavedSession()){
            setButtonDisabled(continueCurrentSessionButton);
            if (currentSessionRepository.GetAnsweredQuestionsInt()<=0){
                setButtonDisabled(deleteSavedResultsButton);
            }
        }

        doQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] numberOfQuestions = {"5","10","15"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuFragment.this.getContext());

                builder.setTitle("Избери броят на въпросите!").setCancelable(true);
                builder.setItems(numberOfQuestions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which){
                            case 0:
                                startQuizWithQuestions(5, currentSessionRepository);
                                return;
                            case 1:
                                startQuizWithQuestions(10, currentSessionRepository);
                                return;
                            case 2:
                                startQuizWithQuestions(15, currentSessionRepository);
                                return;
                        }
                    }
                });

                builder.create().show();
                return;
            }
        });

        deleteSavedResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSessionRepository.DeleteSavedResults();

                RecyclerView recyclerView = HistoryFragment.GetRecyclerView();

                try {
                    List<String> previousResults = currentSessionRepository.GetResultsRecordsString();
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(previousResults));

                } catch (EmptyDatabaseException e) {
                    recyclerView.setAdapter(new MyItemRecyclerViewAdapter(Collections.emptyList()));
                }

                MessageHelperService.ShowMessage(MainMenuFragment.this.getContext(),"Историята е изтрита!");
                setButtonDisabled(continueCurrentSessionButton);
                setButtonDisabled(deleteSavedResultsButton);

                return;
            }
        });

        continueCurrentSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MainMenuFragment.this.startActivity(new Intent(MainMenuFragment.this.getContext(), QuizActivity.class).putExtra(QuizActivity.currentSessionDataExtra,
                            (Serializable) currentSessionRepository.GetQuestionsAndAnswers()));
                } catch (EmptyDatabaseException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private void startQuizWithQuestions(int numberOfQuestions, CurrentSessionRepositoryService currentSessionRepository){
        MainMenuFragment.this.startActivity(new Intent(MainMenuFragment.this.getContext(), QuizActivity.class).putExtra(QuizActivity.numberOfQuestions, numberOfQuestions));
        currentSessionRepository.DeleteSavedResults();
    }

    private void setButtonDisabled(Button button){
        button.setBackgroundColor(Color.parseColor(greyColorString));
        button.setEnabled(false);
    }

}
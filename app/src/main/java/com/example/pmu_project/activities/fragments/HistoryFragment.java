package com.example.pmu_project.activities.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pmu_project.activities.adapters.MyItemRecyclerViewAdapter;
import com.example.pmu_project.R;
import com.example.pmu_project.exception.EmptyDatabaseException;
import com.example.pmu_project.service.CurrentSessionRepositoryService;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;

import java.util.List;

public class HistoryFragment extends Fragment {


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    private static RecyclerView recyclerView;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance(int columnCount) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            CurrentSessionRepositoryService currentSessionRepository = new RepositoryServiceImpl(this.getContext());

            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            try {
                List<String> previousResults = currentSessionRepository.GetResultsRecordsString();
                recyclerView.setAdapter(new MyItemRecyclerViewAdapter(previousResults));

            } catch (EmptyDatabaseException e) {
                e.printStackTrace();
            }
        }
        return view;
    }

    public static RecyclerView GetRecyclerView(){
        return recyclerView;
    }

}
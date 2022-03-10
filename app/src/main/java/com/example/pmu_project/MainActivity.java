package com.example.pmu_project;

import android.os.Bundle;

import com.example.pmu_project.activities.QuizActivity;
import com.example.pmu_project.activities.adapters.SectionsPagerAdapter;
import com.example.pmu_project.service.DatabaseService;
import com.example.pmu_project.service.impl.DatabaseServiceImpl;
import com.example.pmu_project.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final DatabaseService db = new DatabaseServiceImpl(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

    private void init() {
        db.LoadDataFromFile();
        QuizActivity.ResetResults();
        QuizActivity.ResetQuestions();
    }
}
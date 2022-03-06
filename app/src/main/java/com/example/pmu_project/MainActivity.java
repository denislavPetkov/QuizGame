package com.example.pmu_project;

import android.os.Bundle;

import com.example.pmu_project.IService.IDatabase;
import com.example.pmu_project.Service.Database;
import com.example.pmu_project.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final IDatabase db = new Database(this);

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
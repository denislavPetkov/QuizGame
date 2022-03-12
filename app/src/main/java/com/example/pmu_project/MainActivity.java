package com.example.pmu_project;

import android.os.Bundle;

import com.example.pmu_project.activities.adapters.SectionsPagerAdapter;
import com.example.pmu_project.service.GeneralRepositoryService;
import com.example.pmu_project.service.QuestionRepositoryService;
import com.example.pmu_project.service.impl.RepositoryServiceImpl;
import com.example.pmu_project.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final GeneralRepositoryService generalRepository = new RepositoryServiceImpl(this);

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
        generalRepository.LoadDataFromFile();
    }


}
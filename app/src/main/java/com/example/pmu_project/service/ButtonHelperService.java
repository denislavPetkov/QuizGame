package com.example.pmu_project.service;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public interface ButtonHelperService {
    public static void buttonDelayedAction(Button button, Context packageContext , Class activity, long delayMillis) {
        button.postDelayed(new Runnable() {
            @Override
            public void run() {
                packageContext.startActivity(new Intent(packageContext,activity));
            }
        }, delayMillis);
    }
}

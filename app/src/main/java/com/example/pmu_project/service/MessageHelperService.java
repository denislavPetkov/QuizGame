package com.example.pmu_project.service;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public interface MessageHelperService {
    public static void ShowMessage(Context packageContext, String message) {
        Toast.makeText(packageContext,
                message,
                Toast.LENGTH_LONG)
        .show();
    }
}

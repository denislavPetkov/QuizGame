package com.example.pmu_project.IService;

import android.os.Handler;

import androidx.appcompat.app.AlertDialog;

public interface IMessageHelper {
    public static void showMessage(AlertDialog.Builder alertDialogBuilder, String ss, long delayMillis) {
        alertDialogBuilder.setMessage(ss);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.cancel();
            }
        }, delayMillis);
    }
}

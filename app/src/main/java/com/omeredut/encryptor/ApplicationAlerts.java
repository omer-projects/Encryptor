package com.omeredut.encryptor;

import android.app.Dialog;
import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class ApplicationAlerts {

    public static void simpleAlert(Context context, String title, String message){
        AlertDialog.Builder simpleAlert = new AlertDialog.Builder(context);
        simpleAlert.setTitle(title)
                .setMessage(message);
        simpleAlert.show();
    }



    public static Dialog progressBarAlert(Context context){
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_bar_layout);
        return dialog;
    }

}

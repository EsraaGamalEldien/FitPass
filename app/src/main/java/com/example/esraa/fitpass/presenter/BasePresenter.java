package com.example.esraa.fitpass.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.esraa.fitpass.R;

public class BasePresenter implements IBasePresenter {

    private ProgressDialog progressdialog;
    private DialogInterface dialogInterface;

    public BasePresenter() {

    }


    public void showErrorAlert(Context context, String msg) {
        hideProgressDialog();
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialogInterface = dialog;
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .show();

    }

    public void hideErrorAlert() {
        if (dialogInterface != null) {
            dialogInterface.dismiss();
        }
    }

    public void showProgressDialog(Context context) {
        progressdialog = new ProgressDialog(context);
        progressdialog.setMessage(context.getString(R.string.please_wait));
        progressdialog.show();
    }

    public void hideProgressDialog() {
        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.dismiss();
        }
    }
}

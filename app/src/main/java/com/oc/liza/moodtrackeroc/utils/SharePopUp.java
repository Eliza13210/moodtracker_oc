package com.oc.liza.moodtrackeroc.utils;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.controler.MainActivity;
import com.oc.liza.moodtrackeroc.model.Mood;

import java.util.Objects;

public class SharePopUp {

    private Context context;
    private String number = "";
    private String address = "";
    private Mood mood;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;


    public SharePopUp(Context context, Mood mood) {
        this.context = context;
        this.mood = mood;
        sharePopUp();
    }

    //Share mood by sms or e-mail
    private void sharePopUp() {

        AlertDialog.Builder buildShare = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);
        buildShare.setTitle("Choissisez votre mode de partage");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputShare = new EditText(context);
        inputShare.setTextColor(Color.DKGRAY);
        inputShare.setHintTextColor(Color.LTGRAY);
        inputShare.setHint("N° de téléphone");
        inputShare.setInputType(InputType.TYPE_CLASS_PHONE);
        inputShare.setSelection(0);
        layout.addView(inputShare);

        final EditText inputEmail = new EditText(context);
        inputEmail.setTextColor(Color.DKGRAY);
        inputEmail.setHintTextColor(Color.LTGRAY);
        inputEmail.setHint("Adresse e-mail");
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(inputEmail);
        buildShare.setView(layout);

        // Set up the buttons
        buildShare.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                number = inputShare.getText().toString();
                address = inputEmail.getText().toString();

                if (address.isEmpty()) {
                    checkPermission();
                    dialog.dismiss();
                } else {
                    sendEmail(address);
                    dialog.dismiss();
                }
            }
        });
        buildShare.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = buildShare.create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    private void sendEmail(String address) {
        Intent send = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(address) +
                "?subject=" + Uri.encode("Je voudrais partager mon humeur") +
                "&body=" + Uri.encode(mood.toString());
        Uri uri = Uri.parse(uriText);
        send.setData(uri);
        context.startActivity(Intent.createChooser(send, "Send mail"));

    }

    private void sendSMS() {
        try {
            SmsManager.getDefault().sendTextMessage(number, null, mood.toString(), null, null);
            Toast.makeText(context, "SMS envoyé", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Le message n'a pas été envoyé ", Toast.LENGTH_SHORT).show();
        }

    }

    public void permissionGranted() {
        sendSMS();
    }

    private void checkPermission() {
        //check if permission to send sms and if not so, ask for permission
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((MainActivity) context,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // If permission is already granted send SMS
            sendSMS();
        }
    }


}

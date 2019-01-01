package com.oc.liza.moodtrackeroc.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;

import java.util.Calendar;
import java.util.Objects;

public class CommentPopUp {

    private Context context;
    private int mood;
    private MoodListManager manager;

    public CommentPopUp(Context context, int mood) {
        this.context = context;
        this.mood = mood;
        manager = new MoodListManager(context);
        popUpDialog();
    }

    //Comment pop up dialog
    private void popUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert);

        //Create a text field
        final EditText input = new EditText(context);
        builder.setTitle("Commentaire");
        input.setTextColor(Color.BLACK);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.requestFocus();
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                //When user validate; create a mood and save it
                String mComment = input.getText().toString();
                Calendar c = Calendar.getInstance();

                Mood mMood = new Mood(mood, c, mComment);
                manager.addMood(mMood);

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.show();
    }

}

package com.oc.liza.moodtrackeroc.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;

import java.util.Calendar;
import java.util.List;

public class MoodWeek {

    private List<Mood> moodList;
    private String[] bg_color;
    private int cMonth;
    private int cDay;
    private Context ctx;

    public MoodWeek(List<Mood> moodList, Context ctx) {
        this.moodList = moodList;
        this.ctx = ctx;
        bg_color = ctx.getResources().getStringArray(R.array.colorArray);
    }

    /**
     * This method serves to place the mood on the correct textview, depending on how many days ago the mood was saved
     *
     * @param daysAgo  refers to how many days have passed since the mood was saved
     * @param textView refers to the corresponding text view where it will be shown
     */

    public void matchDates(int daysAgo, TextView textView) {

        //Compare the mood date to the text view's date
        for (int i = 0; i < moodList.size(); i++) {
            int cMoodDay = moodList.get(i).getDate().get(Calendar.DAY_OF_MONTH);
            int cMoodMonth = moodList.get(i).getDate().get(Calendar.MONTH);

            //get todays date
            getDate(daysAgo);

            //If dates are matched; Set the background color
            if (cMoodDay == cDay && cMoodMonth == cMonth) {

                Mood mood = moodList.get(i);
                int mInt = mood.getMood();

                setColor(textView, mInt);
                setWidth(textView, mInt);

                //check if there's a comment and show icon in that case
                if (mood.getComment() != null) {
                    setComment(mood, textView);
                }
            }
        }

    }

    /**
     * Get the date for each day of the week
     *
     * @param daysAgo refers to how many days ago from todays date
     */
    private void getDate(int daysAgo) {
        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, daysAgo);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Set textview background color depending on which mood is selected
     *
     * @param textView refers to the textview you want to change background color for
     * @param mInt     refers to the mood identified by a number and will be used to pick the right color from the array
     */

    private void setColor(TextView textView, int mInt) {

        String color = bg_color[mInt];
        textView.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * Set textview width depending on which mood
     *
     * @param textView the textview you want to change
     * @param mInt     the mood identified by a number
     */

    private void setWidth(TextView textView, int mInt) {

        Display display = ((Activity) ctx).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int dividedWidth = width / 5;
        width = mInt * dividedWidth + dividedWidth;
        textView.setWidth(width);

    }

    /**
     * Set comment image in the textview
     *
     * @param mood     refers to the object mood that holds the comment
     * @param textView refers to the textview you want to change
     */
    private void setComment(Mood mood, TextView textView) {
        final String comment = mood.getComment();
        Drawable image = ctx.getResources().getDrawable(R.drawable.ic_comment_black_48px);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds(0, 0, h, w);
        textView.setCompoundDrawables(null, null, image, null);

        //make image clickable and show the comment
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(ctx.getApplicationContext(), comment, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

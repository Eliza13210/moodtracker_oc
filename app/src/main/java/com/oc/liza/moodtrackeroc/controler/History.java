package com.oc.liza.moodtrackeroc.controler;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class History extends AppCompatActivity {
    private List<Mood> mMoodList=new ArrayList<>();
    private String[] bg_color={"#ffde3c50", "#ff9b9b9b", "#a5468ad9","#ffb8e986","#fff9ec4f"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoodList = (ArrayList<Mood>) getIntent().getSerializableExtra("mMoodList");
        setContentView(R.layout.activity_history);

        TextView statistics = findViewById(R.id.statistics);
        TextView tvOne = findViewById(R.id.first);
        TextView tvTwo = findViewById(R.id.second);
        TextView tvThree = findViewById(R.id.third);
        TextView tvFour = findViewById(R.id.fourth);
        TextView tvFive = findViewById(R.id.fifth);
        TextView tvSix = findViewById(R.id.sixt);
        TextView tvSeven = findViewById(R.id.seventh);

        getStatistic();

        moodWeek(-7, tvOne);
        moodWeek(-6, tvTwo);
        moodWeek(-5, tvThree);
        moodWeek(-4, tvFour);
        moodWeek(-3, tvFive);
        moodWeek(-2, tvSix);
        moodWeek(-1, tvSeven);
}

        private void getStatistic(){



        }

    /**
     * This method serves to set the background color representing the mood selected for the corresponding
     * day on the seven TextViews representing the last week.
     *
     * @param daysAgo refers to how many days has passed since the mood was saved
     * @param textView refers to the corresponding text view where it will be shown
     */

        private void moodWeek(int daysAgo, TextView textView ){
          try{
                //Compare the mood date to the text view's date
                for(int i=0;i<mMoodList.size();i++){
                    int cMoodDay = mMoodList.get(i).getDate().get(Calendar.DAY_OF_MONTH);
                    int cMoodMonth= mMoodList.get(i).getDate().get(Calendar.MONTH);

                    Calendar c=Calendar.getInstance();
                    c.add(Calendar.DATE, daysAgo);
                    int c2Month = c.get(Calendar.MONTH);
                    int c2Day=c.get(Calendar.DAY_OF_MONTH);

                //If dates are matched; Set the background color and comment to the corresponding textview

                if(cMoodDay == c2Day && cMoodMonth == c2Month){

                    Mood mood = mMoodList.get(i);
                    int mInt = mood.getMood();
                    int bgColor = Color.parseColor(bg_color[mInt]);
                    textView.setBackgroundColor(bgColor);
                    if(mood.getComment().length()>0) {
                        final String comment = mood.getComment();
                        Drawable image = getResources().getDrawable(R.drawable.ic_comment_black_48px);
                        int h = image.getIntrinsicHeight();
                        int w = image.getIntrinsicWidth();
                        image.setBounds(0, 0, h, w);
                        textView.setCompoundDrawables(null, null, image, null);
                        textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View textView) {
                            Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                }
                }
            }catch(IndexOutOfBoundsException e) {
              String str="Aucun humeur sauvgardé pour ce jour";
                textView.setText(str);
            }
        }

}


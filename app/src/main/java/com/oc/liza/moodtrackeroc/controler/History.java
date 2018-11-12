package com.oc.liza.moodtrackeroc.controler;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class History extends AppCompatActivity {
    private String filename = "moodsList.txt";
    private TextView tvOne;
    private TextView tvTwo;
    private TextView tvThree;
    private TextView tvFour;
    private TextView tvFive;
    private TextView tvSix;
    private TextView tvSeven;
    private List<Mood> mMoodList=new ArrayList<>();
    private String[] bg_color={"#ffde3c50", "#ff9b9b9b", "#a5468ad9","#ffb8e986","#fff9ec4f"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Mood> questions = new ArrayList<Mood>();
        mMoodList = (ArrayList<Mood>) getIntent().getSerializableExtra("mMoodList");
        setContentView(R.layout.activity_history);

        tvOne=findViewById(R.id.first);
        tvTwo=findViewById(R.id.second);
        tvThree=findViewById(R.id.third);
        tvFour=findViewById(R.id.fourth);
        tvFive=findViewById(R.id.fifth);
        tvSix=findViewById(R.id.sixt);
        tvSeven=findViewById(R.id.seventh);

        moodWeek(-7,tvOne);
        moodWeek(-6, tvTwo);
        moodWeek(-5,tvThree);
        moodWeek(-4,tvFour);
        moodWeek(-3,tvFive);
        moodWeek(-2,tvSix);
        moodWeek(-1,tvSeven);
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
                        final String comment = mood.getComment().toString();
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
                textView.setText("Aucun humeur sauvgardé pour ce jour");
            }
        }

}

package com.oc.liza.moodtrackeroc;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.model.Mood;

import java.util.ArrayList;
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

        moodWeek(1,tvOne);
        moodWeek(2, tvTwo);
        moodWeek(3,tvThree);
        moodWeek(4,tvFour);
        moodWeek(5,tvFive);
        moodWeek(6,tvSix);
        moodWeek(7,tvSeven);

    }

    /**
     * This method serves to set the correct background color on the seven
     * TextViews representing the last week. The mood is represented by the corresponding
     * color and is set to the corresponding day.
     *
     * @param listNumber refers to the place of the object in the list of Moods
     * @param textView refers to the corresponding textview where it will be shown
     */

        private void moodWeek(int listNumber, TextView textView ){

            try{
                Mood mood = mMoodList.get(listNumber);
                int mInt = mood.getMood();
                int bgColor = Color.parseColor(bg_color[mInt]);
                textView.setBackgroundColor(bgColor);
                if(mood.getComment().length()>0){
                    final String comment = mood.getComment().toString();
                    Drawable image = getResources().getDrawable(R.drawable.ic_comment_black_48px);
                    int h=image.getIntrinsicHeight();
                    int w=image.getIntrinsicWidth();
                    image.setBounds(0,0,h, w);
                    textView.setCompoundDrawables(null,null,image, null);
                    textView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View textView) {
                        Toast.makeText(getApplicationContext(),comment, Toast.LENGTH_SHORT).show();
                    }
                });
                }
            }catch(IndexOutOfBoundsException e) {
                textView.setText("Aucun humeur sauvgardé pour ce jour");
            }
        }


}


package com.oc.liza.moodtrackeroc.controler;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.utils.MoodListManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class History extends AppCompatActivity {

    private List<Mood> mMoodList = new ArrayList<>();
    private String[] bg_color;
    private MoodListManager mMoodListManager = new MoodListManager(this);

    // 1 - Adding @BindView in order to indicate to ButterKnife to get & serialise it
    @BindView(R.id.first)
    TextView tvOne;
    @BindView(R.id.second)
    TextView tvTwo;
    @BindView(R.id.third)
    TextView tvThree;
    @BindView(R.id.fourth)
    TextView tvFour;
    @BindView(R.id.fifth)
    TextView tvFive;
    @BindView(R.id.sixt)
    TextView tvSix;
    @BindView(R.id.seventh)
    TextView tvSeven;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoodList = mMoodListManager.getMoodList();
        bg_color = getResources().getStringArray(R.array.colorArray);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        initHistory();
    }

    private void initHistory() {

        moodWeek(-7, tvOne);
        moodWeek(-6, tvTwo);
        moodWeek(-5, tvThree);
        moodWeek(-4, tvFour);
        moodWeek(-3, tvFive);
        moodWeek(-2, tvSix);
        moodWeek(-1, tvSeven);
    }

    /**
     * This method serves to
     * - set the background color representing the mood selected
     * -set the width of the text view
     * -show if there is a comment saved
     *
     * @param daysAgo  refers to how many days have passed since the mood was saved
     * @param textView refers to the corresponding text view where it will be shown
     */

    private void moodWeek(int daysAgo, TextView textView) {
        try {
            //Compare the mood date to the text view's date
            for (int i = 0; i < mMoodList.size(); i++) {
                int cMoodDay = mMoodList.get(i).getDate().get(Calendar.DAY_OF_MONTH);
                int cMoodMonth = mMoodList.get(i).getDate().get(Calendar.MONTH);

                Calendar c = Calendar.getInstance();

                c.add(Calendar.DATE, daysAgo);
                int c2Month = c.get(Calendar.MONTH);
                int c2Day = c.get(Calendar.DAY_OF_MONTH);

                //If dates are matched; Set the background color
                if (cMoodDay == c2Day && cMoodMonth == c2Month) {

                    Mood mood = mMoodList.get(i);
                    int mInt = mood.getMood();
                    String color = bg_color[mInt];
                    textView.setBackgroundColor(Color.parseColor(color));

                    //set the width
                    Display display = getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int dividedWidth = width / 5;
                    width = mInt * dividedWidth + dividedWidth;
                    textView.setWidth(width);

                    //check if there's a comment and show icon in that case
                    if (mood.getComment().length() > 0) {
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
        } catch (IndexOutOfBoundsException e) {
            textView.setText("Erreur: " + e);
        }
    }

}


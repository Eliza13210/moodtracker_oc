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
    private int cMonth;
    private int cDay;

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

        TextView[] textviewList = {tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven};
        int j = -7;
        for (int i = 0; i < textviewList.length; i++) {
            moodWeek(j, textviewList[i]);
            j++;
        }
    }

    /**
     * This method serves to place the mood on the correct textview, depending on how many days ago the mood was saved
     *
     * @param daysAgo  refers to how many days have passed since the mood was saved
     * @param textView refers to the corresponding text view where it will be shown
     */

    private void moodWeek(int daysAgo, TextView textView) {

        //Compare the mood date to the text view's date
        for (int i = 0; i < mMoodList.size(); i++) {
            int cMoodDay = mMoodList.get(i).getDate().get(Calendar.DAY_OF_MONTH);
            int cMoodMonth = mMoodList.get(i).getDate().get(Calendar.MONTH);

            //get todays date
            getDate(daysAgo);

            //If dates are matched; Set the background color
            if (cMoodDay == cDay && cMoodMonth == cMonth) {

                Mood mood = mMoodList.get(i);
                int mInt = mood.getMood();

                setColor(textView, mInt);
                setWidth(textView, mInt);

                //check if there's a comment and show icon in that case
                if (mood.getComment()!=null) {
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

        Display display = getWindowManager().getDefaultDisplay();
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
        Drawable image = getResources().getDrawable(R.drawable.ic_comment_black_48px);
        int h = image.getIntrinsicHeight();
        int w = image.getIntrinsicWidth();
        image.setBounds(0, 0, h, w);
        textView.setCompoundDrawables(null, null, image, null);

        //make image clickable and show the comment
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View textView) {
                Toast.makeText(getApplicationContext(), comment, Toast.LENGTH_SHORT).show();
            }
        });
    }

}


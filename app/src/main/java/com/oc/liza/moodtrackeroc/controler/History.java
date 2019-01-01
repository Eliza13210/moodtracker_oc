package com.oc.liza.moodtrackeroc.controler;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.oc.liza.moodtrackeroc.R;
import com.oc.liza.moodtrackeroc.model.Mood;
import com.oc.liza.moodtrackeroc.utils.MoodListManager;
import com.oc.liza.moodtrackeroc.utils.MoodWeek;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class History extends AppCompatActivity {

    private List<Mood> mMoodList = new ArrayList<>();
    private Context ctx = this;
    private MoodListManager mMoodListManager = new MoodListManager(this);

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
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        initHistory();
    }


    private void initHistory() {
        MoodWeek moodWeek = new MoodWeek(mMoodList, ctx);
        TextView[] textviewList = {tvOne, tvTwo, tvThree, tvFour, tvFive, tvSix, tvSeven};
        int j = -7;

        // The matchDates method will be called upon for the last 7 days in order to associate every day with a textview
        for (int i = 0; i < textviewList.length; i++) {
            moodWeek.matchDates(j, textviewList[i]);
            j++;
        }
    }


}


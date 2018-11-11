package com.oc.liza.moodtrackeroc.view;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oc.liza.moodtrackeroc.R;

public class ScreenSlide extends PagerAdapter {

    private int[] img_resources={R.drawable.smiley_sad,R.drawable.smiley_disappointed,R.drawable.smiley_normal,R.drawable.smiley_happy, R.drawable.smiley_super_happy};
    private String[] bg_color={"#ffde3c50", "#ff9b9b9b", "#a5468ad9","#ffb8e986","#fff9ec4f"};
    private Context ctx;
    private LayoutInflater layoutInflater;


    public ScreenSlide(Context ctx){
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return img_resources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view==(RelativeLayout)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.slide_screen, container, false);
        RelativeLayout bg=item_view.findViewById(R.id.content);
        int bgColor=Color.parseColor(bg_color[position]);
        bg.setBackgroundColor(bgColor);
        ImageView imageView=item_view.findViewById(R.id.imageView);
        imageView.setImageResource(img_resources[position]);
        container.addView(item_view);

        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }


}

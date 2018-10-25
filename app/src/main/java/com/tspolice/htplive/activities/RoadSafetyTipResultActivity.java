package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tspolice.htplive.R;

public class RoadSafetyTipResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_safety_tip_result);

        ImageView img_tip = findViewById(R.id.img_tip);
        TextView tv_tip_text = findViewById(R.id.tv_tip_text);

        String tip;
        int position;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                tip = null;
                position = 0;
            } else {
                tip = extras.getString("roadSafetyTip");
                position = getIntent().getIntExtra("position", 0);
            }
        } else {
            tip = (String) savedInstanceState.getSerializable("roadSafetyTip");
            position = (int) savedInstanceState.getSerializable("position");
        }

        if (position == 0) {
            tv_tip_text.setText(R.string.tip_helmet);
            img_tip.setImageResource(R.drawable.ic_helmet);
        } else if (position == 1) {
            tv_tip_text.setText(R.string.tip_seat_belt);
            img_tip.setImageResource(R.drawable.ic_seatbelt_200x150);
        } else if (position == 2) {
            tv_tip_text.setText(R.string.tip_speeding);
            img_tip.setImageResource(R.drawable.ic_speeding_200x150);
        }

    }
}

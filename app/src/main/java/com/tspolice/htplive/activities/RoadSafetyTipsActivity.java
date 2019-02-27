package com.tspolice.htplive.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.CommonRecyclerAdapter;
import com.tspolice.htplive.adapters.MyRecyclerViewItemDecoration;
import com.tspolice.htplive.models.CommonModel;
import com.tspolice.htplive.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class RoadSafetyTipsActivity extends AppCompatActivity {

    private List<CommonModel> mCommonList;
    private CommonModel mCommonModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_safety_tips);

        RecyclerView mRecyclerView = findViewById(R.id.mRecyclerViewRoadSafetyTips);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        //mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));

        mCommonList = new ArrayList<>();
        CommonModel model = new CommonModel();
        model.setTip("Helmet");
        mCommonList.add(model);
        CommonModel model2 = new CommonModel();
        model2.setTip("Seat Belt");
        mCommonList.add(model2);
        CommonModel model3 = new CommonModel();
        model3.setTip("Speeding");
        mCommonList.add(model3);

        mRecyclerView.setAdapter(new CommonRecyclerAdapter(""+Constants.ROAD_SAFETY_TIPS, mCommonList,
                new CommonRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(CommonModel item, int position) {
                        mCommonModel = mCommonList.get(position);
                        Intent intent = new Intent(RoadSafetyTipsActivity.this, RoadSafetyTipResultActivity.class);
                        intent.putExtra("roadSafetyTip", mCommonModel.getTip());
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                }, RoadSafetyTipsActivity.this));
    }
}

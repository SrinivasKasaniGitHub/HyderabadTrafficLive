package com.tspolice.htplive.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tspolice.htplive.R;
import com.tspolice.htplive.adapters.AlertsAdapter;
import com.tspolice.htplive.adapters.MyRecyclerViewItemDecoration;
import com.tspolice.htplive.models.AlertsModel;
import com.tspolice.htplive.utils.UiHelper;

import java.util.ArrayList;
import java.util.List;

public class AlertsActivity extends AppCompatActivity {

    private UiHelper mUiHelper;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        initViews();

        initObjects();

        mUiHelper.showToastLong("Push notifications");

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setSelected(true);
        mRecyclerView.addItemDecoration(new MyRecyclerViewItemDecoration(this, DividerItemDecoration.VERTICAL, 8));
        List<AlertsModel> mAlertsList = new ArrayList<>(30);
        for (int i = 0; i < 30; i++) {
            AlertsModel model = new AlertsModel();
            model.setAlertText("Dt: 12–09–2018 at 09:22 hrs - Slow movement of traffic from Karkhana to Secunderabad club. " +
                    "And the extension code Slow movement of traffic from Karkhana to Secunderabad club");
            model.setAlertUpdatedDt("Updated Dt: 12–09–2018 09:25hrs");
            mAlertsList.add(model);
        }
        AlertsAdapter mAlertsAdapter = new AlertsAdapter(mAlertsList, this);
        mRecyclerView.setAdapter(mAlertsAdapter);
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.mRecyclerViewAlerts);
    }

    private void initObjects() {
        mUiHelper = new UiHelper(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}

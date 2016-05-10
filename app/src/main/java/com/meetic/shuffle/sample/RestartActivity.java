package com.meetic.shuffle.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.sample.adapter.TestAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestartActivity extends AppCompatActivity {

    @Bind(R.id.shuffle) Shuffle shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restart);
        ButterKnife.bind(this);
        TestAdapter testAdapter = new TestAdapter();
        testAdapter.setDisplayText(true);
        shuffle.setShuffleAdapter(testAdapter);
    }

    @OnClick(R.id.restart)
    public void onClickRestart() {
        shuffle.restartShuffling();
    }

}

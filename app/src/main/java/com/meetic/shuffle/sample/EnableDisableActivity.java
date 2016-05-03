package com.meetic.shuffle.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.sample.adapter.TestAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnableDisableActivity extends AppCompatActivity {

    @Bind(R.id.shuffle) Shuffle shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_disable);
        ButterKnife.bind(this);
        shuffle.setShuffleAdapter(new TestAdapter());
    }

    @OnClick(R.id.enable)
    public void onClickEnable() {
        shuffle.enable(true);
    }

    @OnClick(R.id.disable)
    public void onClickDisable() {
        shuffle.enable(false);
    }

}

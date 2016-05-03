package com.meetic.shuffle.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.sample.adapter.TestAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HorizontalActivity extends AppCompatActivity {

    @Bind(R.id.shuffle) Shuffle shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal);
        ButterKnife.bind(this);
        shuffle.setShuffleAdapter(new TestAdapter());
    }

    @OnClick(R.id.left)
    public void onClickLeft() {
        shuffle.swipeLeft(1000);
    }

    @OnClick(R.id.right)
    public void onClickRight() {
        shuffle.swipeRight(1000);
    }

}

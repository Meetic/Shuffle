package com.meetic.shuffle.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.ShuffleViewAnimator;
import com.meetic.shuffle.sample.adapter.TestAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VerticalActivity extends AppCompatActivity {

    @Bind(R.id.shuffle) Shuffle shuffle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical);
        ButterKnife.bind(this);
        shuffle.setShuffleAdapter(new TestAdapter());

        shuffle.setViewAnimator(new ShuffleViewAnimator()
            .setPushTopAnimateViewStackScaleUp(false)
            .setPushBottomAnimateViewStackScaleUp(false)
        );
    }

}

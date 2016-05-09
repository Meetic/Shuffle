package com.meetic.shuffle.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.sample.adapter.TestInnerScrollAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HorizontalInlineWrapContentInnerScrollActivity extends AppCompatActivity {

    @Bind(R.id.shuffle) Shuffle shuffle;
    private TestInnerScrollAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_inline_wrap_content_inner_scroll);
        ButterKnife.bind(this);

        adapter = new TestInnerScrollAdapter();
        shuffle.setShuffleAdapter(adapter);

        shuffle.addVerticalScrollListener(new Shuffle.OnScrollChangeListener() {

            private float prevScroll = 0;

            @Override
            public void onScrollChange(View v, float scrollX, float scrollY, float oldScrollX, float oldScrollY) {
                ScrollView sv = (ScrollView) v.findViewById(R.id.innerScroll);

                if (prevScroll != 0 && prevScroll != scrollY)
                    sv.scrollBy(0, prevScroll > scrollX ? -10 : 10);

                prevScroll = scrollY;
            }
        });
    }

}

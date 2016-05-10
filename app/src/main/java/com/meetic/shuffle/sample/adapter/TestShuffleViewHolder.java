package com.meetic.shuffle.sample.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 10/05/2016.
 */
public class TestShuffleViewHolder extends Shuffle.ViewHolder {
    @Bind(R.id.image) ImageView imageView;
    @Bind(R.id.text) TextView textView;

    public TestShuffleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(int position) {
        textView.setText(String.valueOf(position));
    }
}

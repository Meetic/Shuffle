package com.meetic.shuffle.sample.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    int[] colors = new int[]{
        Color.parseColor("#C62828"),
        Color.parseColor("#6A1B9A"),
        Color.parseColor("#1565C0"),
        Color.parseColor("#37474F"),
        Color.parseColor("#00695C"),
        Color.parseColor("#D84315"),
        Color.parseColor("#9E9D24"),
        Color.parseColor("#5D4037"),
        Color.parseColor("#F9A825"),
    };

    @Bind(R.id.image) ImageView imageView;
    @Bind(R.id.text) TextView textView;

    public TestShuffleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(int position) {
        imageView.setImageDrawable(new ColorDrawable(colors[position % colors.length]));
        textView.setText(String.valueOf(position));
    }
}

package com.meetic.shuffle.sample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meetic.shuffle.Shuffle;
import com.meetic.shuffle.sample.R;

public class TestWrapContentAdapter extends Shuffle.Adapter<Shuffle.ViewHolder> {

    @Override
    public Shuffle.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bal_tmp_cell_custom_height, viewGroup, false);
        return new Shuffle.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Shuffle.ViewHolder viewHolder, int position) {
    }

    @Override
    public int getItemCount() {
        return 30;
    }
}

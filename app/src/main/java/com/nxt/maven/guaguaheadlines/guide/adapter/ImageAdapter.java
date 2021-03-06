package com.nxt.maven.guaguaheadlines.guide.adapter;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nxt.maven.guaguaheadlines.R;

/**
 * Created by Jan Maven on 2017/7/17.
 * Email:cyjiang_11@163.com
 * Description:
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private TypedArray images;

    public ImageAdapter(TypedArray dataSet) {
        images = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageDrawable(images.getDrawable(position));
    }

    @Override
    public int getItemCount() {
        return images.length();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ViewHolder(View view) {
            super(view);
            image = (ImageView) view;
        }
    }
}

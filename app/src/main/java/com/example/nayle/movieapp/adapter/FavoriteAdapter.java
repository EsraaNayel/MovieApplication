package com.example.nayle.movieapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.nayle.movieapp.R;

/**
 * Created by Nayle on 1/29/2016.
 */
public class FavoriteAdapter extends BaseAdapter {
    public FavoriteAdapter() {
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        //view=inflater.inflate(R.layout.movie_item,null);
        final ImageView Iv= (ImageView) view.findViewById(R.id.main_ImageView);
        return null;
    }
}

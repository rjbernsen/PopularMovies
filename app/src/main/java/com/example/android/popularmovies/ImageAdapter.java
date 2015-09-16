package com.example.android.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/*
This is the adapter used by the MainActivityFragment
to load the posters and return them in an ImageView
object.
*/
public class ImageAdapter extends BaseAdapter {
    private Context ctx;
    private String[] imageUrls;

    public ImageAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public int getCount() {
        return this.imageUrls.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setImages(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {

            convertView = LayoutInflater.from(ctx).inflate(R.layout.image_view, null);
            imageView = (ImageView) convertView.findViewById(R.id.poster_view);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso
                .with(ctx)
                .load(imageUrls[position])
                .into(imageView);

        return imageView;
    }
}
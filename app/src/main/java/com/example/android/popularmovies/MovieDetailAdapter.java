package com.example.android.popularmovies;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
This is the adapter used by the MovieItemActivity class
to load the movie details in the MovieDetailFragment and
return them in an ListView object.
*/
public class MovieDetailAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<String> movieDetails = new ArrayList<>();

    public MovieDetailAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public int getCount() {
        return movieDetails.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void setDetails(ArrayList<String> movieDetails) {
        this.movieDetails = movieDetails;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        ImageView imageView = null;
        View returnView;
        boolean isTextView = true;
        boolean needToRebuildView = false;

        if (position == 0) {
            isTextView = false;
        }

        if (position > 0 && convertView != null && convertView instanceof ImageView) {
            needToRebuildView = true;
        }

        if (convertView == null || position <= 1 || needToRebuildView) {

            convertView = LayoutInflater.from(ctx).inflate(R.layout.movie_detail_item_view, null);

            if (isTextView) {
                textView = (TextView) convertView.findViewById(R.id.movie_detail_textview);
            } else {
                imageView = (ImageView) convertView.findViewById(R.id.movie_detail_imageview);
            }
        } else {
            if (isTextView) {
                textView = (TextView) convertView;
            } else {
                imageView = (ImageView) convertView;
            }
        }

        if (isTextView) {
            String text = movieDetails.get(position);
            int wordStart = 0;
            int wordEnd = text.indexOf("~");

            text = text.substring(wordStart, wordEnd) + text.substring(wordEnd + 1);

            final SpannableStringBuilder formattedString = new SpannableStringBuilder(text);
            formattedString.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    wordStart,
                    wordEnd,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            textView.setText(formattedString);

            returnView = textView;
        } else {
            String url = movieDetails.get(position);

            Picasso
                    .with(ctx)
                    .load(url)
                    .into(imageView);

            returnView = imageView;
        }

        return returnView;
    }

}
package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/*
This is a simple container for the data returned from
the movie db. It contains all of the MovieItem objects
that are instantiated using the JSON string from the
web service call.
*/
public class MovieList extends ArrayList<MovieItem> {
    private Context ctx;
    private String jsonString;
    private String posterPathUrl;
    private String posterSize;

    private String logTag = MovieList.class.getSimpleName();

    public MovieList(Context ctx, String jsonString) throws JSONException {
        this.ctx = ctx;
        this.jsonString = jsonString;

        this.buildFromJson();
    }

    public String[] getPosterUrls() {
        String[] urlList = new String[this.size()];

        try {
            for (int i = 0; i < this.size(); i++) {
                MovieItem mi = this.get(i);

                Uri builtUri = Uri.parse(posterPathUrl).buildUpon()
                        .appendEncodedPath(posterSize)
                        .appendEncodedPath(mi.getPosterPath())
                        .build();

                URL url = new URL(builtUri.toString());
                urlList[i] = url.toString();
            }
        } catch (MalformedURLException e) {
            Log.e(logTag, "URL formation problem");
        }

        return urlList;

    }

    private void buildFromJson() throws JSONException {

        final String RESULTS = this.ctx.getString(R.string.json_attr_results);

        posterPathUrl = this.ctx.getString(R.string.image_base_url);
        posterSize = this.ctx.getString(R.string.image_size);

        JSONObject movieJson = new JSONObject(this.jsonString);
        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        int numberOfMovies = movieArray.length();

        for(int i = 0; i < numberOfMovies; i++) {
            // Get the JSON object representing the movie.
            JSONObject currentMovie = movieArray.getJSONObject(i);

            this.add(
                    new MovieItem(ctx,currentMovie)
            );
        }
    }
}

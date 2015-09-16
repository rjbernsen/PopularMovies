package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

/*
This is a simple object that contains the details
for one movie
*/
public class MovieItem {

    private LinkedHashMap<String,String> attributes = new LinkedHashMap<>();
    private LinkedHashMap<String,String> attributeLabels = new LinkedHashMap<>();
    private String posterPath;
    String posterPathKey;
    private String posterUrlKey;


    public MovieItem(Context ctx,JSONObject movieJson) throws JSONException {

        this.init(ctx,movieJson);
    }

    public String getPosterPath() {
        return posterPath;
    }

    public ArrayList<String> getMovieItemAttributes() {
        ArrayList<String> values = new ArrayList<>();
        Set<String> keys = attributes.keySet();

        for (String curKey : keys) {
            // The tilde is added in the string so that the
            // calling class can set the label to BOLD.
            if (curKey.equals(posterUrlKey)) {
                values.add(attributes.get(curKey));
            } else {
                values.add(attributeLabels.get(curKey) + "~\n" + attributes.get(curKey));
            }
        }

        return values;
    }

    private void init(Context ctx, JSONObject movieJson) throws JSONException {
        posterPathKey = ctx.getString(R.string.json_attr_poster_path);

        String[] attrKeys = ctx.getResources().getStringArray(R.array.json_attributes);
        String[] attrLabels = ctx.getResources().getStringArray(R.array.json_attribute_labels);

        String posterPathUrl = ctx.getString(R.string.image_base_url);
        String posterSize = ctx.getString(R.string.image_size);

        // Get the poster path and remove the leading slash!
        posterPath = (movieJson.getString(posterPathKey)).substring(1);

        posterUrlKey = ctx.getString(R.string.poster_url_key);

        try {
            Uri builtUri = Uri.parse(posterPathUrl).buildUpon()
                    .appendEncodedPath(posterSize)
                    .appendEncodedPath(posterPath)
                    .build();

            URL url = new URL(builtUri.toString());

            attributes.put(posterUrlKey,url.toString());
            attributeLabels.put(posterUrlKey,"N/A"); // This label is never used!

        } catch (MalformedURLException e) {
            Log.e("MovieItem.init()", "URL formation problem");
        }

        for (int i = 0; i < attrKeys.length; i++) {
            attributes.put(attrKeys[i], movieJson.getString(attrKeys[i]));
            attributeLabels.put(attrKeys[i], attrLabels[i]);
        }
    }
}


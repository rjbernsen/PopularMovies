package com.example.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
This is a utility class that encapsulates all the work
required to fetch the data from the movie db site.
*/
public final class DataFetcher {

    private DataFetcher() {}

    public static String getMovies(Context ctx, String listType) {

        String logTag = DataFetcher.class.getSimpleName() + " getMovies()";

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String jsonString;

        try {
            // Construct the URL for the MovieDB query
            Uri builtUri = Uri.parse(ctx.getString(R.string.base_url)).buildUpon()
                    .appendEncodedPath(ctx.getString(R.string.db_type))
                    .appendEncodedPath(listType)
                    .appendQueryParameter(ctx.getString(R.string.api_key_query_param), ctx.getString(R.string.api_key))
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
                buffer.append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            jsonString = buffer.toString();

            return jsonString;

        } catch (IOException e) {
            Log.e(logTag, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(logTag, "Error closing stream", e);
                }
            }
        }
    }
}

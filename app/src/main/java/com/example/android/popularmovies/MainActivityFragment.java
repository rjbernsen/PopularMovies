package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.Arrays;

/*
This is the activity fragment that contains the GridView
that displays the posters. It also provides the "onClick"
listener for displaying the details when a poster is
clicked.
*/
public class MainActivityFragment extends Fragment {

    public MovieList movieList;
    private ImageAdapter movieImageAdapter;
    private TextView mainPageTitle;
    private GridView movieGrid;
    private String currentTitleText;

    public MainActivityFragment() {

    }
    @Override
    public void onResume() {
        super.onResume();

        updateMovieList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movie_list, menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);

        mainPageTitle = (TextView) rootView.findViewById(R.id.main_page_title_textview);
        movieGrid = (GridView) rootView.findViewById(R.id.movie_grid);
        movieImageAdapter = new ImageAdapter(getActivity());

        mainPageTitle.setText("By Most Popular");

        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                MovieItem movieItem = movieList.get(position);

                intent.putStringArrayListExtra(Intent.EXTRA_TEXT, movieItem.getMovieItemAttributes());

                startActivity(intent);
            }
        });

        updateMovieList();

        return rootView;
    }

    private void updateMovieList() {
        FetchMovieListTask movieListTask = new FetchMovieListTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_key_default));
        movieListTask.execute(sortOrder);
    }

    public class FetchMovieListTask extends AsyncTask<String, Void, String[]> {

        /**
         * Take the String representing the movie list in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */

        @Override
        protected String[] doInBackground(String... params) {

            String logTag = FetchMovieListTask.class.getSimpleName() + " doInBackground()";
            
            String listType;
            String jsonString;
            String[] posterUrls = null;

            String[] listTypes = new String[] {
                    getString(R.string.list_type_popular),
                    getString(R.string.list_type_top_rated)
            };

            // If there's no list type, get the default.
            if (params.length == 0) {
                listType = getString(R.string.list_type_default);
            } else {
                if (Arrays.asList(listTypes).contains(params[0])) {
                    listType = params[0];
                } else {
                    Log.e(logTag, "List type \"" + params[0] + "\" is not a valid type");
                    return null;
                }
            }

            if (listType.equals(getString(R.string.pref_sort_order_key_popular))) {
                currentTitleText = "By " + getString(R.string.pref_sort_order_value_popular);
            } else if (listType.equals(getString(R.string.pref_sort_order_key_top_rated))) {
                currentTitleText = "By " + getString(R.string.pref_sort_order_value_top_rated);
            } else {
                currentTitleText = "By " + getString(R.string.pref_sort_order_value_default);
            }

            jsonString = DataFetcher.getMovies(getActivity(),listType);

            try {
                movieList = new MovieList(getActivity(),jsonString);
                posterUrls = movieList.getPosterUrls();
            } catch (JSONException e) {
                Log.e(logTag, e.getMessage(), e);
                e.printStackTrace();
            }

            return posterUrls;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mainPageTitle.setText(currentTitleText);
                movieImageAdapter.setImages(result);
                movieImageAdapter.notifyDataSetChanged();
                movieGrid.setAdapter(movieImageAdapter);
            }
        }
    }

}

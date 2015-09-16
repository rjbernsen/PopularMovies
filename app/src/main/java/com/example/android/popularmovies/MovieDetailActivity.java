package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_item_view);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new MovieDetailFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MovieDetailFragment extends Fragment {

        public MovieDetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.activity_movie_detail, container, false);
            Intent intent = getActivity().getIntent();

            ListView movieDetailList = (ListView) rootView.findViewById(R.id.movie_detail_list_view);
            MovieDetailAdapter movieDetailAdapter = new MovieDetailAdapter(getActivity());

            movieDetailAdapter.setDetails(intent.getStringArrayListExtra(Intent.EXTRA_TEXT));
            movieDetailList.setAdapter(movieDetailAdapter);

            return rootView;
        }
    }
}

package com.example.myandroidapplication.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myandroidapplication.Model.movie.Movie;
import com.example.myandroidapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TvShowsFragment extends Fragment implements MovieAdapter.OnListItemClickListener {

    RecyclerView movieList;
    ArrayList<Movie> movies;
    private static String POPULAR_MOVIE_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=33da4b0735e9ca10f0db031928a139eb";


    public TvShowsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tv_shows, container, false);

        movieList = root.findViewById(R.id.tvShowsRecycleView);
        movieList.hasFixedSize();
        movieList.setLayoutManager(new LinearLayoutManager(getActivity()));

        movies = new ArrayList<>();
        GetData getData = new GetData();
        getData.execute();

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        int movieNumber = clickedItemIndex + 1;
        Toast.makeText(getContext(), "Privet:  " + movieNumber, Toast.LENGTH_SHORT).show();
    }

    public class GetData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(POPULAR_MOVIE_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = is.read();

                    while (data != -1) {
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject movieFromJson = jsonArray.getJSONObject(i);
                    Movie movie = new Movie();
                    movie.setId(String.valueOf(movieFromJson.getInt("id")));
                    movie.setName(movieFromJson.getString("title"));
                    movie.setOverview(movieFromJson.getString("overview"));
                    movie.setRating(String.valueOf(movieFromJson.getDouble("vote_average")));
                    movie.setPosterImage(movieFromJson.getString("poster_path"));
                    movie.setBackdropImage(movieFromJson.getString("backdrop_path"));
                    movie.setAdult(movieFromJson.getBoolean("adult"));
                    movie.setOriginalLanguage(movieFromJson.getString("original_language"));
                    movie.setOriginalLanguage(movieFromJson.getString("original_language"));

                    movies.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            putDataIntoRecycleView(movies);
        }
    }

    private void putDataIntoRecycleView(List<Movie> movies) {
        MovieAdapter adapter = new MovieAdapter(movies, getContext(), this);
        movieList.setAdapter(adapter);
    }
}
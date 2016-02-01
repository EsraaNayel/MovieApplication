package com.example.nayle.movieapp;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nayle.movieapp.Database.DBHelper;
import com.example.nayle.movieapp.adapter.DetailAdapter;
import com.example.nayle.movieapp.data.com.example.Result;
import com.example.nayle.movieapp.datamovie.Review;
import com.example.nayle.movieapp.datamovie.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nayle on 12/10/2015.
 */
public class DetaildActivity extends Activity {
    private ArrayList<Trailer> trailerArrayList;
    private ArrayList<Review> ReviewArrayList;
    private List<Trailer> trailers;
    ListView LV;
    DBHelper dbHelper;
    int id = 0;
    String PosterPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper = new DBHelper(this);

        LV = (ListView) findViewById(R.id.main_ListView);
        final Result result = getResultData();
        new TrailersTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/videos?api_key=c6260798b95be3f1643000b02177cf03");

        addHeader(result);

        ImageButton btnFavorite = (ImageButton) findViewById(R.id.btn_favorite);
        final View.OnClickListener dbButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "favorite btn ", Toast.LENGTH_LONG).show();
                boolean check = dbHelper.checkIFexist(id, PosterPath);//,null ,null, null, null,null);
                if (check == false) {

                    dbHelper.insert(id, PosterPath);//,null ,null, null, null,null );
                } else {

                    dbHelper.delete(id,PosterPath);
                }

            }

        };
        btnFavorite.setOnClickListener(dbButtonListener);

    }


    private void addHeader(Result result) {
        id=result.getId();
        String movDesc = result.getOverview();
        String imgPath = result.getPosterPath();
        PosterPath=imgPath;
        String movTitle = result.getTitle();
        String movDate = result.getReleaseDate();
        float movRate = result.getVoteAverage();

        View view = LayoutInflater.from(this).inflate(R.layout.test_header, null);

        TextView tvName = (TextView) view.findViewById(R.id.movie_name);
        ImageView IV = (ImageView) view.findViewById(R.id.movie_thumb);
        TextView tvDesc = (TextView) view.findViewById(R.id.descrition);
        TextView tvDate = (TextView) view.findViewById(R.id.release_date);
        RatingBar rbRate = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView tvRating = (TextView) view.findViewById(R.id.rating);

        rbRate.setEnabled(false);
        LayerDrawable stars = (LayerDrawable) rbRate.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

        Picasso.with(this).load("http://image.tmdb.org/t/p/" + "w342" + imgPath).into(IV);//w342

        tvName.setText(movTitle);
        tvDesc.setText("Description:\n" + movDesc);
        tvDate.setText("Release Date:\n" + movDate);
        rbRate.setRating(movRate / 2);

        tvRating.setText(movRate + "");

        // btnFavorite.setOnClickListener(dbButtonListener);
        LV.addHeaderView(view);
    }

    private Result getResultData() {
        Result result = (Result) getIntent().getExtras().get("movie");
        return result;
    }

    class TrailersTask extends AsyncTask<String, String, List<Trailer>> {
        @Override
        protected List<Trailer> doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String FinalJsonTrailers = buffer.toString();
                System.out.println("Trailers  " + FinalJsonTrailers);
                JSONObject jsono = new JSONObject(FinalJsonTrailers);
                JSONArray jsona = jsono.getJSONArray("results");
                trailerArrayList = new ArrayList<>();
                for (int i = 0; i < jsona.length(); i++) {

                    JSONObject FinalObject = jsona.getJSONObject(i);
                    Trailer trailer = new Trailer();
                    trailer.setName(FinalObject.getString("name"));
                    trailer.setUrl(FinalObject.getString("key"));

                    trailerArrayList.add(trailer);
                }
                return trailerArrayList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<Trailer> trailers) {
            super.onPostExecute(trailers);
            Log.d("trailer", trailers.size() + "");
            DetaildActivity.this.trailers = trailers;
            Result result = getResultData();
            new ReviewsTask().execute("http://api.themoviedb.org/3/movie/" + result.getId() + "/reviews?api_key=c6260798b95be3f1643000b02177cf03");

        }
    }

    class ReviewsTask extends AsyncTask<String, String, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... params) {

            HttpURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String FinalJsonReviews = buffer.toString();

                System.out.println("Reviews " + FinalJsonReviews);
                JSONObject jsono = new JSONObject(FinalJsonReviews);
                JSONArray jsona = jsono.getJSONArray("results");

                ReviewArrayList = new ArrayList<>();
                for (int i = 0; i < jsona.length(); i++) {
                    JSONObject FinalObject = jsona.getJSONObject(i);
                    Review review = new Review();
                    review.setAuthor(FinalObject.getString("author"));
                    review.setContent(FinalObject.getString("content"));
                    ReviewArrayList.add(review);
                }
                return ReviewArrayList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);

            DetailAdapter adapter = new DetailAdapter(DetaildActivity.this, trailers, reviews);
            LV.setAdapter(adapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dbHelper.openDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbHelper.close();
    }
}
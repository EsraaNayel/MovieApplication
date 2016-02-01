package com.example.nayle.movieapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import com.example.nayle.movieapp.Database.DBHelper;
import com.example.nayle.movieapp.adapter.MainAdapter;
import com.example.nayle.movieapp.data.com.example.Result;

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


public class MainActivity extends ActionBarActivity {
    public GridView gridView;
    private ArrayList<Result> moviemodellist;
    MainAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.main_gridview);



        new JSONTask().execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c6260798b95be3f1643000b02177cf03");


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(MainActivity.this, DetaildActivity.class);
                Result result = moviemodellist.get(position);
                i.putExtra("movie", result);
                startActivity(i);
            }
        });
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>ASYNCTASK Connection<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    class JSONTask extends AsyncTask<String, String, List<Result>> {
        @Override
        protected List<Result> doInBackground(String... params) {
            HttpURLConnection connection ;
            BufferedReader reader ;
            try {
              URL  url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String FinalJson = buffer.toString();
                JSONObject jsono = new JSONObject(FinalJson);
                JSONArray jsona = jsono.getJSONArray("results");

//<<<<<<<<<<<<<<<<<<<<<<<<<< Parsing JSON >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                moviemodellist = new ArrayList<>();
                for (int i = 0; i < jsona.length(); i++) {
                    JSONObject FinalObject = jsona.getJSONObject(i);
                    Result moviemodel = new Result();

                    moviemodel.setPosterPath(FinalObject.getString("poster_path"));
                    moviemodel.setId(FinalObject.getInt("id"));
                    moviemodel.setOverview(FinalObject.getString("overview"));
                    moviemodel.setOriginalTitle(FinalObject.getString("original_title"));
                    moviemodel.setReleaseDate(FinalObject.getString("release_date"));
                    moviemodel.setTitle(FinalObject.getString("title"));
                    moviemodel.setPopularity(FinalObject.getDouble("popularity"));
                    //moviemodel.setVoteCount(FinalObject.getInt("vote_count"));
                    moviemodel.setVoteAverage((float) FinalObject.getDouble("vote_average"));

                    moviemodellist.add(moviemodel);
                }
                return moviemodellist;

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
        public void onPostExecute(List<Result> result) {
            super.onPostExecute(result);
            adapter = new MainAdapter(MainActivity.this, result);
            gridView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater().inflate(R.menu.menu_main,spinner);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.highest_rated) {

            JSONTask MovieTask = new JSONTask();
            MovieTask.execute("http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=36238a089d7b9497ccba2af9e2b8cc06");
            return true;
        }

        if (id == R.id.most_popular) {

            JSONTask MovieTask = new JSONTask();
            MovieTask.execute("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=c6260798b95be3f1643000b02177cf03");
            return true;
        }

        if (id == R.id.favorit_List) {

            DBHelper dbHelper = new DBHelper(getApplicationContext());
            List<Result> movieMainList=dbHelper.getAllMovie();

            adapter = new MainAdapter(this,movieMainList);
            gridView.setAdapter(adapter);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
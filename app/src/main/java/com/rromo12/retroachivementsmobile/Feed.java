package com.rromo12.retroachivementsmobile;


import android.content.SharedPreferences;
import android.net.Network;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Feed extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userName;
        if(getIntent().getExtras() == null) {
            userName = getUserPreferences();
        } else {
            //Do stuff with intent data here
            userName = getIntent().getStringExtra("user_name");
        }
        setupRecyclerView();
        //setupNavDrawer();
        //Fetch data and populate View
        fetchFeedData(userName);

    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_uname_key))) {
            String userName = sharedPreferences.getString(getString(R.string.pref_uname_key),"None");
            fetchFeedData(userName);
        }
    }

    public void fetchFeedData(String userName) {
        boolean userExists = NetworkUtils.userExists(userName);
        TextView error_message = (TextView) findViewById(R.id.error_message);
        if(userExists){
            //Fetch User Feed
            getSupportActionBar().setTitle(userName+getString(R.string.my_feed));
            URL url = NetworkUtils.buildUserFeedUrl(userName);
            new JsonTask().execute(url);
            mRecyclerView.setVisibility(View.VISIBLE);
            error_message.setVisibility(View.GONE);
        }
        else{
            mRecyclerView.setVisibility(View.GONE);
            error_message.setText(getString(R.string.error));
            error_message.setVisibility(View.VISIBLE);

            //Set error message
        }


    }


    public class JsonTask extends AsyncTask<URL, Void, String> {
        //



        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String jsonText = null;
            try {
                jsonText = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonText;
        }
        @Override
        protected void onPostExecute(String jsonText) {
            // Create Adapter from json Text
            FeedAdapter adapter;
            try {
                adapter = new FeedAdapter(jsonText);

                mRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Set Adapter

        }
    }

}

package com.rromo12.retroachivementsmobile;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class RecentAchievements extends BaseActivity {

    TextView tv_uname;
    ImageView iv_user;

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
        getSupportActionBar().setTitle(userName+getString(R.string.recent_achievements));

        //Fetch data and populate View
        fetchFeedData(userName);
    }

    protected String getUserPreferences(){
        //Get User Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Get user name and api key from preferences
        return preferences.getString(getString(R.string.pref_uname_key), "None");

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
        if(userExists) {
            URL url = NetworkUtils.buildUserFeedUrl(userName);
            new JsonTask().execute(url);
            getSupportActionBar().setTitle(userName+getString(R.string.recent_achievements));
            error_message.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

        }
        else{
            mRecyclerView.setVisibility(View.GONE);
            error_message.setText(getString(R.string.error));
            error_message.setVisibility(View.VISIBLE);
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
            RecentAchievementsAdapter adapter;
            try {
                adapter = new RecentAchievementsAdapter(jsonText);

                mRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}

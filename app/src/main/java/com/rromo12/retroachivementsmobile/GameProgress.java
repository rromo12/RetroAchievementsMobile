package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class GameProgress extends BaseActivity{
        int gameId;
        String userName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_game_progress);

            getUserPrefs();
            setupRecyclerView();
            //Fetch data and populate View
            fetchGameProgressData(userName,gameId);

        }

        private void getUserPrefs(){
            //Get User Preferences
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            preferences.registerOnSharedPreferenceChangeListener(this);
            //Get user name and api key from preferences
            if(getIntent().hasExtra("user_name")) {
                userName = getIntent().getStringExtra("user_name");
            } else {
                //Do stuff with intent data here
                userName = preferences.getString(getString(R.string.pref_uname_key), "None");
            }

            gameId = this.getIntent().getIntExtra("GameID",228);

        }


        private void fetchGameProgressData(String userName,int gameID){
                URL url = NetworkUtils.buildUserGameProgressUrl(userName,gameID);
                new GameProgress_JsonTask().execute(url);
        }

        private void selectItemFromDrawer(int position) {
            if(position == 0) {
                Intent intent = new Intent(this, Feed.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
            else if(position == 1) {
                Intent intent = new Intent(this, RecentlyPlayed.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
            else if(position == 2) {
                Intent intent = new Intent(this, GameProgress.class);
                intent.putExtra("GameID", 228);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }


        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if(key.equals(getString(R.string.pref_uname_key))) {
                userName = sharedPreferences.getString(getString(R.string.pref_uname_key),"None");
                fetchGameProgressData(userName,gameId);
            }
        }

        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);
            //Left Navigation Menu Icon
            //mDrawerToggle.syncState();
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            //Left Navigation Menu
            if(item.getItemId() == android.R.id.home){
                this.finish();
                return true;
            }
            else if(id == R.id.action_settings)
            {
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        }

public class GameProgress_JsonTask extends AsyncTask<URL, Void, JSONObject> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected JSONObject doInBackground(URL... params) {
        URL searchUrl = params[0];
        String jsonText = null;
        try {
            jsonText = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject ret = null;
        try {
            ret = new JSONObject(jsonText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
    @Override
    protected void onPostExecute(JSONObject json) {
        // Create Adapter from json Text
        //Recently Played Adapter
        GameProgressAdapter adapter;
        try {
            getSupportActionBar().setTitle(json.getString("Title") + " (" + json.getString("NumAwardedToUser") + "/" + json.getString("NumAchievements") +")" ) ;
            adapter = new GameProgressAdapter(json);
            mRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}



}


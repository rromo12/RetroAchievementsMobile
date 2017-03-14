package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class RecentlyPlayed extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_played);
        String userName;
        setupRecyclerView();
        if(getIntent().getExtras() == null) {
            userName = getUserPreferences();
        } else {
            //Do stuff with intent data here
            userName = getIntent().getStringExtra("user_name");
        }
        //Fetch data and populate
        getSupportActionBar().setTitle(userName + getString(R.string.recently_played));
        fetchRecentlyPlayedData(userName);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_uname_key))) {
            String userName = sharedPreferences.getString(getString(R.string.pref_uname_key),"None");
            fetchRecentlyPlayedData(userName);
        }
    }


    public void fetchRecentlyPlayedData(String userName) {
        //Fetch User Image
        //URL user_image = NetworkUtils.buildUserPicURL(userName);
        //new NetworkUtils.DownLoadImageTask(iv_user).execute(user_image);

        //Fetch User Feed
        boolean userExists = NetworkUtils.userExists(userName);
        TextView error_message = (TextView) findViewById(R.id.error_message);
        if(userExists){
            URL url = NetworkUtils.buildUserRecentlyPlayedUrl(userName,10);
            new RecentlyPlayed_JsonTask().execute(url);
            getSupportActionBar().setTitle(userName + getString(R.string.recently_played));
            mRecyclerView.setVisibility(View.VISIBLE);
            error_message.setVisibility(View.GONE);
        }
        else{
            mRecyclerView.setVisibility(View.GONE);
            error_message.setText(getString(R.string.error));
            error_message.setVisibility(View.VISIBLE);
        }

    }


    public class RecentlyPlayed_JsonTask extends AsyncTask<URL, Void, String> implements RecentlyPlayedAdapter.ListItemClickListener{

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
            //Recently Played Adapter
            RecentlyPlayedAdapter adapter;
            try {
                adapter = new RecentlyPlayedAdapter(jsonText,this);
                mRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        @Override
        public  void onListItemClick(int gameId){
            Intent intent = new Intent(getBaseContext(), GameProgress.class);
            intent.putExtra("GameID", gameId);
            intent.putExtra("child", 1);
            startActivity(intent);
        }
    }




}

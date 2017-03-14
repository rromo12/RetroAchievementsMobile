package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import java.io.IOException;
import java.net.URL;

public class MyGames extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_games);
        String userName;
        setupRecyclerView();
        if(getIntent().getExtras() == null) {
            userName = getUserPreferences();
        } else {
            //Do stuff with intent data here
            userName = getIntent().getStringExtra("user_name");
        }
        getSupportActionBar().setTitle(userName + getString(R.string.my_games));
        //Fetch data and populate View
        fetchRecentlyPlayedData(userName);
    }

    public void fetchRecentlyPlayedData(String userName) {
        //Fetch User Image
        //URL user_image = NetworkUtils.buildUserPicURL(userName);
        //new NetworkUtils.DownLoadImageTask(iv_user).execute(user_image);

        boolean userExists = NetworkUtils.userExists(userName);
        TextView error_message = (TextView) findViewById(R.id.error_message);
        if(userExists) {
            //Fetch User Feed
            new MyGames_JsonTask().execute(userName);
            getSupportActionBar().setTitle(userName + getString(R.string.my_games));
            mRecyclerView.setVisibility(View.VISIBLE);
            error_message.setVisibility(View.GONE);
        } else{
            mRecyclerView.setVisibility(View.GONE);
            error_message.setText(getString(R.string.error));
            error_message.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_uname_key))) {
            String userName = sharedPreferences.getString(getString(R.string.pref_uname_key),"None");
            fetchRecentlyPlayedData(userName);
        }
    }

    public class MyGames_JsonTask extends AsyncTask<String, Void, String> implements MyGamesAdapter.ListItemClickListener{
        String userName;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            userName = params[0];
            URL searchUrl = NetworkUtils.buildUserRecentlyPlayedUrl(userName, 200);
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
            MyGamesAdapter adapter;
            try {
                adapter = new MyGamesAdapter(jsonText , this,userName);
                mRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        @Override
        public  void onListItemClick(int gameId,String userName){
            Intent intent = new Intent(getBaseContext(), GameProgress.class);
            intent.putExtra("user_name", userName);
            intent.putExtra("GameID", gameId);
            intent.putExtra("child", 1);
            startActivity(intent);
        }
    }





}

package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class Top10 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);
        getSupportActionBar().setTitle(getString(R.string.top_10));
        setupRecyclerView();
        fetchTop10Data();
    }

    private void fetchTop10Data(){
        URL url = NetworkUtils.buildTop10Url();
        new JsonTask().execute(url);
    }



    public class JsonTask extends AsyncTask<URL, Void, String> implements Top10Adapter.ListItemClickListener{
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
            Top10Adapter adapter;
            try {
                adapter = new Top10Adapter(jsonText,this);
                mRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public  void onListItemClick(String userName){
            Intent intent = new Intent(getBaseContext(), UserProfile.class);
            intent.putExtra("user_name", userName);
            intent.putExtra("child", 1);
            startActivity(intent);
        }
    }

}

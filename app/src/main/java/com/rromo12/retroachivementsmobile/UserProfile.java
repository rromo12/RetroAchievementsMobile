package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class UserProfile extends BaseActivity  implements AdapterView.OnItemClickListener {
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() == null) {
            userName = getUserPreferences();
        } else {
            //Do stuff with intent data here
            userName = getIntent().getStringExtra("user_name");
        }

        setContentView(R.layout.activity_user_profile);
        //
        getSupportActionBar().setTitle(userName + getString(R.string.my_profile));
        fetchData(userName);
        setupActionList();
    }

    private void fetchData(String userName) {
        boolean userExists = NetworkUtils.userExists(userName);
        TextView error_message = (TextView) findViewById(R.id.error_message);
        RelativeLayout content_view = (RelativeLayout) findViewById(R.id.content_frame);
        if(userExists){
            //Fetch User Feed
            ImageView userImage = (ImageView) findViewById(R.id.user_image);
            TextView userN = (TextView) findViewById(R.id.tv_userName);
            userN.setText(userName);
            URL image = NetworkUtils.buildUserPicURL(userName);
            Picasso.with(userImage.getContext()).load(image.toString()).resize(128,128).into(userImage);
            new UserProfile_JsonTask().execute(userName);
            content_view.setVisibility(View.VISIBLE);
            error_message.setVisibility(View.GONE);
        }
        else{
            //Set error message
            content_view.setVisibility(View.GONE);
            error_message.setText(getString(R.string.error));
            error_message.setVisibility(View.VISIBLE);
        }




    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_uname_key))) {
            userName = sharedPreferences.getString(getString(R.string.pref_uname_key),"None");
            fetchData(userName);
        }
    }
    private void setupActionList() {
        ListView list = (ListView) findViewById(R.id.userProfileActions);
        final String[] values = getResources().getStringArray(R.array.userProfileActionsArray);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values); // You have set     the previous array to an adapter that can be now setted to a list.

        list.setAdapter(adapter); //
        list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, Feed.class);
                intent.putExtra("user_name", userName);
                intent.putExtra("child",1);
                startActivity(intent);
                break;

            case 1:
                //Recently Played
                intent = new Intent(this, RecentlyPlayed.class);
                intent.putExtra("user_name", userName);
                intent.putExtra("child",1);
                startActivity(intent);
                break;
            case 2:
                //Recent Achievements
                intent = new Intent(this, RecentAchievements.class);
                intent.putExtra("user_name", userName);
                intent.putExtra("child",1);
                startActivity(intent);
                break;
            case 3:
                //All Games
                intent = new Intent(this, MyGames.class);
                intent.putExtra("user_name", userName);
                intent.putExtra("child",1);
                startActivity(intent);
                break;

        }
    }

    public class UserProfile_JsonTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL searchUrl = NetworkUtils.buildUserSummaryUrl(userName);
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
            //Set Info As Necessary
            JSONObject userData;
            try {
                userData = new JSONObject(jsonText);
                //Check if id is null, if so set error message, usernot found

                TextView rankTextView = (TextView) findViewById(R.id.tv_userRank);
                TextView userTextView = (TextView) findViewById(R.id.tv_userName);
                TextView mottoTextView = (TextView) findViewById(R.id.tv_motto);
                TextView retroRatioTextView = (TextView) findViewById(R.id.tv_retroratio);
                //Set Rank
                String RankString ="Rank: " +userData.getString("Rank");
                rankTextView.setText(RankString);
                //SetMemberJoinDate
                String JoinDate = "Member Since: " + Utils.convertDateString(userData.getString("MemberSince"));
                //SetLastLogin
                String LastLogin = "Last Login: " + Utils.convertDateString(userData.getString("LastLogin"));
                //Set Retro Ratio (TotalTruePoints/TotalPoints) to 2 decimal places
                double retroRatio = Math.round((userData.getDouble("TotalTruePoints")/userData.getDouble("TotalPoints"))*100)/100.0;
                String retroRatioText = "Total Points: " + (int) userData.getDouble("TotalPoints") + "   Retro Ratio: " + retroRatio;
                retroRatioTextView.setText(retroRatioText);
                //Set Motto
                String Motto = "Motto: " + userData.getString("Motto");
                mottoTextView.setText(Motto);
                String Status = userData.getString("Status");
                //Set  Online Status
                String userNameTVText = userName + " (" + Status +")";
                userTextView.setText(userNameTVText);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
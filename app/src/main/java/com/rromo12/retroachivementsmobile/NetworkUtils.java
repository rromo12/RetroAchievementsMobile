package com.rromo12.retroachivementsmobile;


import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    private final static String USERNAME = "";
    private final static String APIKEY = "";
    private final static String RETROACHIEVEMENTS_BASE_URL =
            "http://retroachievements.org/API/";
    private final static String PARAM_USER = "z";
    private final static String PARAM_KEY = "y";
    private final static String PARAM_GAMEID = "g";
    private final static String PARAM_MEMBER = "u";
    private final static String PARAM_COUNT = "c";

    static boolean userExists(String UserName){
        boolean ret=false;

        try {
            ret = new NetworkUtils.JsonTask().execute(UserName).get().booleanValue();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }
    static URL buildUserSummaryUrl(String UserName) {
        Uri builtUri = Uri.parse(RETROACHIEVEMENTS_BASE_URL+"API_GetUserSummary.php?").buildUpon()
                .appendQueryParameter(PARAM_USER, USERNAME)
                .appendQueryParameter(PARAM_KEY, APIKEY)
                .appendQueryParameter(PARAM_MEMBER,UserName)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildTop10Url() {
        Uri builtUri = Uri.parse(RETROACHIEVEMENTS_BASE_URL+"API_GetTopTenUsers.php?").buildUpon()
                .appendQueryParameter(PARAM_USER, USERNAME)
                .appendQueryParameter(PARAM_KEY, APIKEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildUserFeedUrl(String UserName) {
        Uri builtUri = Uri.parse(RETROACHIEVEMENTS_BASE_URL+"API_GetFeed.php?").buildUpon()
                .appendQueryParameter(PARAM_USER, USERNAME)
                .appendQueryParameter(PARAM_KEY, APIKEY)
                .appendQueryParameter(PARAM_MEMBER,UserName)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildUserGameProgressUrl(String UserName, int GameID) {
        Uri builtUri = Uri.parse(RETROACHIEVEMENTS_BASE_URL+"API_GetGameInfoAndUserProgress.php?").buildUpon()
                .appendQueryParameter(PARAM_USER, USERNAME)
                .appendQueryParameter(PARAM_KEY, APIKEY)
                .appendQueryParameter(PARAM_MEMBER,UserName)
                .appendQueryParameter(PARAM_GAMEID,Integer.toString(GameID))
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildUserRecentlyPlayedUrl(String UserName,int count) {
        String c = Integer.toString(count);
        Uri builtUri = Uri.parse(RETROACHIEVEMENTS_BASE_URL+"API_GetUserRecentlyPlayedGames.php?").buildUpon()
                .appendQueryParameter(PARAM_USER, USERNAME)
                .appendQueryParameter(PARAM_KEY, APIKEY)
                .appendQueryParameter(PARAM_MEMBER,UserName)
                .appendQueryParameter(PARAM_COUNT, c)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();


            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    static URL buildAchievementBadgeURL(String badgeID) {
        String url_string = "http://i.retroachievements.org/Badge/" + badgeID + ".png";
        Uri builtUri = Uri.parse(url_string);
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildAchievementBadgeLockURL(String badgeID) {
        String url_string = "http://i.retroachievements.org/Badge/" + badgeID + "_lock.png";
        Uri builtUri = Uri.parse(url_string);
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildUserPicURL(String userName) {

        String user_image_url_string = "http://retroachievements.org/UserPic/" + userName  +".png";
        Uri builtUri = Uri.parse(user_image_url_string);
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    static URL buildGameIconURL(String IconPath ){
        String url_string = "http://i.retroachievements.org" + IconPath;
        Uri builtUri = Uri.parse(url_string);
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    static public class JsonTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String userName = params[0];
            URL searchUrl = NetworkUtils.buildUserSummaryUrl(userName);
            String jsonText = null;
            String joindate="";
            JSONObject data = null;
            try {
                jsonText = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //return jsonText and convert to JSON Object;
            try {
                data = new JSONObject(jsonText);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                joindate = data.getString("MemberSince");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(joindate.equals("null")){
                return false;
            }
            else{
                return true;
            }


        }
    }

}

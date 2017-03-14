package com.rromo12.retroachivementsmobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;

/**
 * Created by Romob on 3/8/2017.
 */

public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    RecyclerView mRecyclerView;
    public DrawerLayout drawerLayout;
    public ListView drawerList;
    String[] items;
    private ActionBarDrawerToggle drawerToggle;
    boolean childActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("child")) {
            childActivity=true;

        } else {
            childActivity = false;
        }
    }


    protected void setUpBackButton(){
        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    protected void onCreateDrawer()
    {
        // R.id.drawer_layout should be in every activity with exactly the same id.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        items = getResources().getStringArray(R.array.navigation_drawer_string_array);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(R.string.menu);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                items)); //Items is an ArrayList or array with the items you want to put in the Navigation Drawer.

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
                // Do what you want when an item from the Navigation Drawer is clicked
                selectItemFromDrawer(pos);
            }
        });
        drawerToggle.syncState();
    }

    protected String getUserPreferences(){
        //Get User Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        //Get user name and api key from preferences
        String userName = preferences.getString(getString(R.string.pref_uname_key), "None");
        return userName;
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_uname_key))) {
            String userName = sharedPreferences.getString(getString(R.string.pref_uname_key),"None");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    protected void setupRecyclerView(){
        //Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItemFromDrawer(int position) {
        //getSupportActionBar().setTitle(items[position]);
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, Feed.class);
                if (this.getClass() == Feed.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            case 1:
                intent = new Intent(this, RecentAchievements.class);
                if (this.getClass() == RecentAchievements.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            case 2:
                intent = new Intent(this, RecentlyPlayed.class);
                if (this.getClass() == RecentlyPlayed.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            case 3:
                intent = new Intent(this, MyGames.class);
                if (this.getClass() == MyGames.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            case 4:
                intent = new Intent(this, Top10.class);
                if (this.getClass() == Top10.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            case 5:
                //My Profile
                intent = new Intent(this, UserProfile.class);
                if (this.getClass() == UserProfile.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            case 6:
                //Find Player
                intent = new Intent(this, FindPlayer.class);
                if (this.getClass() == FindPlayer.class) {
                    drawerLayout.closeDrawers();
                    return;
                }
                startActivity(intent);
                finish();
                break;
            default:
                return;


        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //Left Navigation Menu
        if(childActivity){
            if(id == android.R.id.home){
                this.finish();
                return true;
            }
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        //Settings Menu
        if(id == R.id.action_settings)
        {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        if(childActivity){
            setUpBackButton();
        }
        else{
            onCreateDrawer();
        }

    }



}

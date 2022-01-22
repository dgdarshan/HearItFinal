package com.example.music.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.music.R;
import com.example.music.adapters.SectionsPageAdapter;
import com.example.music.fragments.SongListFragment;
import com.example.music.fragments.SongPlayerFragment;

public class MusicPlayerActivity extends MainServiceActivity {
    public static final String TAG = MusicPlayerActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

    }

    @Override
    protected void onResume() {
        super.onResume();
        removeNotification();
    }

    public void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(12302);
    }

    @Override
    public void onServiceConnected() {
        Log.d(TAG,"onService Connected");
        handleAllView();
    }

    public void handleAllView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Fragment songPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (songPlayerFragment == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.main_content, new SongPlayerFragment(), "SongPlayer").commit();
            Log.d(TAG, "songPlayerFragment Fragment new created");
        } else {
            Log.d(TAG, "songPlayerFragment Fragment reused ");
        }

    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        SongListFragment songListFragment;
        songListFragment = new SongListFragment();
        adapter.addFragment(songListFragment, "All Songs");
        viewPager.setAdapter(adapter);
    }

    public Fragment findWithId(int id) {
        return getSupportFragmentManager().findFragmentById(id);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent k = new Intent(this, SettingsActivity.class);
            startActivity(k);
            return true;
        } else if (id == R.id.searchSongItem) {
            Intent search = new Intent(this, SearchActivity.class);
            startActivity(search);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}


package com.example.music.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.music.services.MusicService;

public abstract class MusicServiceFragment extends Fragment {
    public static final String TAG = "MusicServiceFragment";
    ServiceConnection serviceConnection;
    public MusicService musicService;
    Intent playIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
                musicService = binder.getService();
                MusicServiceFragment.this.onServiceConnected(musicService);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d(TAG,"onServiceDisconnected");
                MusicServiceFragment.this.onServiceDisconnected();
            }
        };
    }

    // this acts like a callback.
    public abstract void onServiceConnected(MusicService musicService);
    public abstract void onServiceDisconnected();

    @Override
    public void onStart() {
        super.onStart();
        playIntent = new Intent(getActivity(), MusicService.class);
        playIntent.setAction("");
        getActivity().bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(playIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onStop()");
        MusicServiceFragment.this.onServiceDisconnected();
        getActivity().stopService(playIntent);
        getActivity().unbindService(serviceConnection);

        }
    }

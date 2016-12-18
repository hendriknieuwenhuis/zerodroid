package com.example.hendriknieuwenhuis.zerodroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bono.api.ChangeListener;
import com.bono.api.ClientExecutor;
import com.bono.api.MPDClient;

import java.util.EventObject;


public class MainActivity extends AppCompatActivity {

    private Playback playback;

    private PlaylistPresenter playlistPresenter;

    private MPDClient mpdClient;

    protected ZApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        app = (ZApplication)getApplication();
        mpdClient = app.getMpdClient();
        playback = new Playback(this, mpdClient);
        mpdClient.getServerMonitor().addMonitorListener(new Listener());

        playlistPresenter = new PlaylistPresenter(this, mpdClient);

        mpdClient.getServerMonitor().addMonitorListener(playlistPresenter);


        if (getIntent().getType() != null && getIntent().getAction().equals(Intent.ACTION_SEND)) {
            new Soundcloud(null, this, getIntent());
        }
    }

    public  void buttonClicked(View view) {
        playback.buttonClicked(view);
    }

    private class Listener implements ChangeListener {

        @Override
        public void stateChanged(EventObject eventObject) {
            System.out.println((String)eventObject.getSource());
        }
    }

}

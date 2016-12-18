package com.example.hendriknieuwenhuis.zerodroid;

import android.app.Application;
import android.content.res.Configuration;
import android.os.AsyncTask;

import com.bono.api.MPDClient;

import java.io.IOException;

/**
 * Created by hendriknieuwenhuis on 09/09/16.
 */
// mpdclient
// init client
// start server monitor.
// methods voor comunicatie! naar activity
public class ZApplication extends Application {

    private MPDClient mpdClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mpdClient = new MPDClient();
        mpdClient.setHost("192.168.2.4");
        mpdClient.setPort(6600);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mpdClient.initMonitor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mpdClient.runMonitor();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("onterminate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("onconfigurationchanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("onlowmemory");
    }

    public MPDClient getMpdClient() {
        return mpdClient;
    }


}

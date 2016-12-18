package com.example.hendriknieuwenhuis.zerodroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;


import com.bono.api.ACKException;

import com.bono.api.DefaultCommand;
import com.bono.api.Endpoint;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Created by hendriknieuwenhuis on 07/05/16.
 *
 * add a soundcloud url to the server to play.
 */
public class Soundcloud extends AsyncTask<Intent, Void, String> {

    private static String HTTP = "http://soundcloud.com/";
    private static String HTTPS = "https://soundcloud.com/";


    private Context context;

    private boolean error = false;

    Soundcloud(Object config, Activity activity, Intent intent) {
        //this.config = config;
        this.context = activity;
        execute(intent);
    }

    @Override
    protected String doInBackground(Intent... intent) {
        String url = null;
        Collection feedback = new ArrayList();
        String reply = "";
        String type = intent[0].getType();

        if ("text/plain".equals(type)) {

            // obtain url, by reading the shared
            // info send from the soundcloud app,
            // reading last line first.
            String[] sharedText = intent[0].getStringExtra(Intent.EXTRA_TEXT).split("\n");
            if (sharedText != null) {
                for (int i = sharedText.length - 1; i >= 0; i--) {
                    String s = sharedText[i];
                    if (s.startsWith("http")) {
                        url = Soundcloud.loadUrl(s);
                        break;
                    }
                }
            }
        }
        // load the url.
        Endpoint endpoint = new Endpoint("192.168.2.4", 6600);
        try {
            feedback = endpoint.command(new DefaultCommand("load", url));
            reply = Arrays.toString(feedback.toArray());
        } catch (ACKException ack) {
            reply = "caught " + ack.getMessage();
            error = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reply;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (error) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog alertDialog = alertDialogBuilder.create();
            try {
                alertDialog.setMessage(get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            alertDialog.show();
        }
    }

    public static String loadUrl(String http) {
        String param = "";
        int httpIndex = 0;
        if (http.contains(HTTP)) {
            httpIndex = http.lastIndexOf(HTTP) + HTTP.length();
        } else if (http.contains(HTTPS)) {
            httpIndex = http.lastIndexOf(HTTPS) + HTTPS.length();
        }
        param = "soundcloud://url/" + http.substring(httpIndex);

        return param;
    }
}

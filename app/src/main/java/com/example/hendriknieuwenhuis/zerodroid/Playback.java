package com.example.hendriknieuwenhuis.zerodroid;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.bono.api.ChangeListener;

import com.bono.api.ClientExecutor;
import com.bono.api.DefaultCommand;
import com.bono.api.MPDClient;
import com.bono.api.Player;
import com.bono.api.Status;
import com.bono.api.protocol.MPDPlayback;
import com.bono.api.protocol.MPDStatus;

import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 09/05/16.
 */
public class Playback implements ChangeListener {

    private Button next;

    private Button pause;

    private Button previous;

    private ClientExecutor clientExecutor;



    private MPDClient mpdClient;

    private Player player;

    private Status status;

    public Playback(Activity activity, MPDClient mpdClient) {
        this.mpdClient = mpdClient;
        this.player = mpdClient.getPlayer();
        this.status = mpdClient.getStatus();
        initPlayer(activity);
    }

    @Deprecated
    public Playback(Activity activity, ClientExecutor clientExecutor, Status status) {
        this.clientExecutor = clientExecutor;
        this.status = status;
        initPlayer(activity);
    }

    private void initPlayer(Activity mainActivity) {
        previous = (Button) mainActivity.findViewById(R.id.previousButton);
        pause = (Button) mainActivity.findViewById(R.id.pauseButton);
        next = (Button) mainActivity.findViewById(R.id.nextButton);
        //playerControl = new PlayerControl(dbExecutor);
        System.out.println("Initiated!");
    }

    public void buttonClicked(View view) {
        Button b = (Button) view;
        String hint = (String) b.getHint();

        if (hint.equals("previous")) {
            try {
                //clientExecutor.execute(new DefaultCommand(MPDPlayback.PREVIOUS));
                mpdClient.getPlayer().previous();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (hint.equals("pause")) {
            // if ststus is pause or stop the button goes play.
            // else if status is play button goes pause.
            //System.out.println(status.getState());

            if (status.getState() == Status.STOP_STATE) {
                try {
                    //clientExecutor.execute(new DefaultCommand(MPDPlayback.PLAY));
                    mpdClient.getPlayer().play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (status.getState() == Status.PAUSE_STATE) {
                try {
                    //clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, "0"));
                    mpdClient.getPlayer().pause(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    //clientExecutor.execute(new DefaultCommand(MPDPlayback.PAUSE, "1"));
                    mpdClient.getPlayer().pause(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (hint.equals("next")) {
            try {
                //clientExecutor.execute(new DefaultCommand(MPDPlayback.NEXT));
                mpdClient.getPlayer().next();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void stateChanged(EventObject eventObject) {
        String s = (String) eventObject.getSource();
        if (s.equals("player")) {
            try {
                //status.populate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

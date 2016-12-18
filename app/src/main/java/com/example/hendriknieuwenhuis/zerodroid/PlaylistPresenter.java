package com.example.hendriknieuwenhuis.zerodroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bono.api.ACKException;
import com.bono.api.ChangeListener;
import com.bono.api.MPDClient;
import com.bono.api.Player;
import com.bono.api.Playlist;
import com.bono.api.Song;

import java.io.IOException;
import java.util.EventObject;

/**
 * Created by hendriknieuwenhuis on 24/05/16.
 */
public class PlaylistPresenter implements ChangeListener {

    private Activity activity;

    private Player player;

    private Playlist playlist;

    private ListView playlistView;

    private PlaylistAdapter playlistAdapter;

    public PlaylistPresenter(Activity activity, MPDClient mpdClient) {
        this.activity = activity;
        this.player = mpdClient.getPlayer();
        this.playlist = mpdClient.getPlaylist();
        playlistView = (ListView) this.activity.findViewById(R.id.listView);
        initPlaylist();
        playlistAdapter = new PlaylistAdapter(activity, playlist);
        playlistView.setAdapter(playlistAdapter);
        playlistView.setOnItemClickListener(new PlaylistItemListener());
    }

    public void initPlaylist() {
        try {
            playlist.queryPlaylist();
        } catch (ACKException ack) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setMessage(ack.getMessage());
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stateChanged(EventObject eventObject) {
        String s = (String) eventObject.getSource();
        if (s.equals("playlist")) {
            initPlaylist();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playlistAdapter.notifyDataSetChanged();
                    playlistView.invalidateViews();
                    playlistView.refreshDrawableState();
                }
            });
        }
    }

    private class PlaylistItemListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Song song = playlist.getSong(position);
            try {
                player.playId(song.getId());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

}

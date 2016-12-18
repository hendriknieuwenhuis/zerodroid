package com.example.hendriknieuwenhuis.zerodroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bono.api.Playlist;
import com.bono.api.Song;


/**
 * Created by hendriknieuwenhuis on 25/05/16.
 */
public class PlaylistAdapter extends BaseAdapter {

    private Context context;
    private Playlist playlist;
    private static LayoutInflater inflater = null;



    public PlaylistAdapter(Context context, Playlist playlist) {
        this.context = context;
        this.playlist = playlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return playlist.getSize();
    }

    @Override
    public Object getItem(int position) {
        return playlist.getSong(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = (View) inflater.inflate(R.layout.playlist_layout, null);
        }



        Song song = playlist.getSong(position);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);


        title.setText(song.getTitle());

        if (song.getArtist() == null || song.getArtist().isEmpty()) {
            artist.setText(song.getName());
        } else {
            artist.setText(song.getArtist());
        }

        /*
        ImageButton button = (ImageButton) convertView.findViewById(R.id.icon);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hier gaat de popup menu geopend worden
                // popup menu bevat .....
                System.out.println("click!");
            }
        });*/

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}

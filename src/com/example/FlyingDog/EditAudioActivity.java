package com.example.FlyingDog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.FlyingDog.ui.adapters.ArtCollectionSpinnerAdapter;
import com.example.FlyingDog.ui.adapters.NamedDataSpinnerAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.AudioComparators;
import com.utils.framework.collections.ListWithNullFirstItem;
import com.utilsframework.android.UiLoopEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by CM on 8/30/2014.
 */
public class EditAudioActivity extends Activity {
    private static final int ADDITIONAL_ARTISTS_MAX_COUNT = 4;
    private static final String AUDIO_ID_KEY = "AUDIO_ID_KEY";
    private AudioDataManager audioDataManager;
    private Audio audio;
    private ArtCollectionSpinnerAdapter<Artist> artistListAdapter;
    private ArtCollectionSpinnerAdapter<Album> albumListAdapter;
    private Spinner artistSelector;

    public static void start(Context context, long audioId) {
        Intent intent = new Intent(context, EditAudioActivity.class);
        intent.putExtra(AUDIO_ID_KEY, audioId);
        context.startActivity(intent);
    }

    private void updateArtistSelectedIndex(int position) {
        Artist artist = artistListAdapter.getElement(position);
        if(artist == null){
            albumListAdapter.setElements(Collections.<Album>singletonList(null));
        } else {
            List<Album> albums = audioDataManager.getAlbumsOfArtist(artist);
            if (artist.isLocal()) {
                Collections.sort(albums, AudioComparators.namedData());
            }

            ListWithNullFirstItem<Album> list = new ListWithNullFirstItem<Album>(albums);
            albumListAdapter.setElements(list);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_audio);

        long audioId = getIntent().getLongExtra(AUDIO_ID_KEY, -1);
        if(audioId < 0){
            throw new IllegalArgumentException("audioId < 0");
        }

        audioDataManager = FlyingDog.getInstance().getAudioDataManager();
        audio = audioDataManager.getSongById(audioId);

        albumListAdapter = new ArtCollectionSpinnerAdapter<Album>(this, "Unknown album");
        albumListAdapter.setElements(Collections.<Album>singletonList(null));
        artistListAdapter = new ArtCollectionSpinnerAdapter<Artist>(this, "Unknown artist");

        final List<Artist> artists = audioDataManager.getArtists();
        Collections.sort(artists, AudioComparators.namedData());
        final Artist audioArtist = audioDataManager.getArtistById(audio.getArtistId());
        int index = artists.indexOf(audioArtist);
        if(index < 0){
            throw new RuntimeException("Broken database!");
        }

        artists.remove(audioArtist);
        artists.add(0, audioArtist);

        new AsyncTask<Void, Void, List<Artist>>(){
            @Override
            protected List<Artist> doInBackground(Void... params) {
                String name = audio.getName();
                return audioDataManager.getSuggestedArtistsByTrackNameFromInternet(name,
                        ADDITIONAL_ARTISTS_MAX_COUNT);
            }

            @Override
            protected void onPostExecute(List<Artist> additionalArtists) {
                artists.addAll(1, additionalArtists);
                artistListAdapter.notifyDataSetChanged();
            }
        }.execute();

        artistListAdapter.setElements(new ListWithNullFirstItem<Artist>(artists));

        final Spinner albumSelector = (Spinner) findViewById(R.id.album_name_spinner);
        artistSelector = (Spinner) findViewById(R.id.artist_name_spinner);

        artistSelector.setAdapter(artistListAdapter);
        albumSelector.setAdapter(albumListAdapter);

        artistSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateArtistSelectedIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        artistSelector.setSelection(1);
        updateArtistSelectedIndex(1);

        Album audioAlbum = audioDataManager.getAlbumById(audio.getAlbumId());
        index = albumListAdapter.getElements().indexOf(audioAlbum);
        if(index < 0){
            throw new RuntimeException("Broken database!");
        }
        albumSelector.setSelection(index);

        EditText audioNameEditText = (EditText) findViewById(R.id.name);
        audioNameEditText.setText(audio.getName());
    }
}

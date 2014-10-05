package com.example.FlyingDog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.FlyingDog.ui.adapters.AlbumsSuggestionsAdapter;
import com.example.FlyingDog.ui.adapters.ArtCollectionSpinnerAdapter;
import com.example.FlyingDog.ui.adapters.ArtistsSuggestionsAdapter;
import com.example.FlyingDog.ui.adapters.AudiosSuggestionsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.MediaUpdatingService;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.AudioComparators;
import com.utils.framework.collections.ListWithNullFirstItem;
import com.utilsframework.android.threading.Tasks;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.TextChangedAfterTimeListener;
import com.utilsframework.android.view.UiMessages;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/**
 * Created by CM on 8/30/2014.
 */
public class EditAudioActivity extends Activity {
    private static final String AUDIO_ID_KEY = "AUDIO_ID_KEY";

    private AudioDataManager audioDataManager;
    private AutoCompleteTextView artistNameEditText;
    private Audio editingAudio;
    private AutoCompleteTextView albumNameEditText;
    private AutoCompleteTextView audioNameEditText;
    private ArtistsSuggestionsAdapter artistsSuggestionsAdapter;

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, EditAudioActivity.class);
        intent.putExtra(AUDIO_ID_KEY, id);
        context.startActivity(intent);
    }

    private void updateArtist() {

    }

    private void initViews() {
        artistNameEditText = (AutoCompleteTextView) findViewById(R.id.artist_name);
        artistsSuggestionsAdapter = new ArtistsSuggestionsAdapter(this, audioDataManager);
        artistNameEditText.setAdapter(artistsSuggestionsAdapter);
        artistNameEditText.setText(editingAudio.getArtistName());

        albumNameEditText = (AutoCompleteTextView) findViewById(R.id.album_name);
        albumNameEditText.setAdapter(new AlbumsSuggestionsAdapter(this, audioDataManager));
        albumNameEditText.setText(editingAudio.getAlbumName());

        audioNameEditText = (AutoCompleteTextView) findViewById(R.id.name);
        audioNameEditText.setAdapter(new AudiosSuggestionsAdapter(this, audioDataManager));
        final String editingAudioName = editingAudio.getName();
        audioNameEditText.setText(editingAudioName);
        artistsSuggestionsAdapter.setTrackName(editingAudioName);
        audioNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String audioName = audioNameEditText.getText().toString();
                editingAudio.setName(audioName);
                artistsSuggestionsAdapter.setTrackName(audioName);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long audioId = getIntent().getLongExtra(AUDIO_ID_KEY, -1);
        if(audioId < 0){
            throw new RuntimeException();
        }

        setContentView(R.layout.edit_audio);

        audioDataManager = FlyingDog.getInstance().getAudioDataManager();
        editingAudio = audioDataManager.getSongById(audioId);
        if(editingAudio == null){
            throw new RuntimeException("Broken database!");
        }

        initViews();
    }
}

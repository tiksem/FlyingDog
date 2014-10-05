package com.example.FlyingDog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import com.example.FlyingDog.ui.adapters.AlbumsSuggestionsAdapter;
import com.example.FlyingDog.ui.adapters.ArtistsSuggestionsAdapter;
import com.example.FlyingDog.ui.adapters.AudiosSuggestionsAdapter;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Audio;
import com.utils.framework.CollectionUtils;
import com.utilsframework.android.threading.AsyncOperationCallback;
import com.utilsframework.android.threading.OnComplete;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.GuiUtilities;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by CM on 8/30/2014.
 */
public class EditAudioActivity extends Activity {
    private static final String AUDIO_ID_KEY = "AUDIO_ID_KEY";

    private AudioDataManager audioDataManager;
    private AutoCompleteTextView artistNameEditText;
    private Audio editingAudio;
    private Audio audioBeforeEditing;
    private AutoCompleteTextView albumNameEditText;
    private AutoCompleteTextView audioNameEditText;
    private ArtistsSuggestionsAdapter artistsSuggestionsAdapter;

    private Runnable onDestroyAction = new Runnable() {
        @Override
        public void run() {
            editingAudio.cloneDataFrom(audioBeforeEditing);
        }
    };

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, EditAudioActivity.class);
        intent.putExtra(AUDIO_ID_KEY, id);
        context.startActivity(intent);
    }

    private void commitChangesToDataBaseAndFinish() {
        Alerts.runAsyncOperationWithCircleLoading(this, R.string.please_wait,
                new AsyncOperationCallback<Object>() {
                    @Override
                    public Object runOnBackground() {
                        audioDataManager.commitAudioChangesToDataBase(editingAudio);
                        return null;
                    }

                    @Override
                    public void onFinish(Object result) {
                        onDestroyAction = null;
                        finish();
                    }
                });
    }

    private void updateAlbum(final OnComplete onComplete) {
        Alerts.runAsyncOperationWithCircleLoading(this, R.string.please_wait,
                new AsyncOperationCallback<Boolean>() {
                    @Override
                    public Boolean runOnBackground() {
                        return audioDataManager.tryFillAlbum(editingAudio);
                    }
                    @Override
                    public void onFinish(Boolean success) {
                        if(success){
                            albumNameEditText.setText(editingAudio.getAlbumName());
                        }

                        if(onComplete != null){
                            onComplete.onFinish();
                        }
                    }
                });
    }

    private void onAudioNameChanged() {
        String audioName = audioNameEditText.getText().toString();
        editingAudio.setName(audioName);
        artistsSuggestionsAdapter.setTrackName(audioName);
    }

    private void onArtistNameChanged() {
        String artistName = artistNameEditText.getText().toString();
        editingAudio.setArtistName(artistName);
    }

    private void onOk() {
        if(!artistNameEditText.getText().toString().equals(editingAudio.getArtistName()) ||
                !audioNameEditText.getText().toString().equals(editingAudio.getName())) {
            updateAlbum(new OnComplete() {
                @Override
                public void onFinish() {
                    commitChangesToDataBaseAndFinish();
                }
            });
        } else {
            commitChangesToDataBaseAndFinish();
        }

        onArtistNameChanged();
        onAudioNameChanged();
    }

    private void onCancel() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (onDestroyAction != null) {
            onDestroyAction.run();
        }
    }

    private void initViews() {
        artistNameEditText = (AutoCompleteTextView) findViewById(R.id.artist_name);
        artistsSuggestionsAdapter = new ArtistsSuggestionsAdapter(this, audioDataManager);
        artistNameEditText.setAdapter(artistsSuggestionsAdapter);
        artistNameEditText.setText(editingAudio.getArtistName());
        artistNameEditText.setThreshold(0);

        albumNameEditText = (AutoCompleteTextView) findViewById(R.id.album_name);
        albumNameEditText.setAdapter(new AlbumsSuggestionsAdapter(this, audioDataManager));
        albumNameEditText.setText(editingAudio.getAlbumName());

        audioNameEditText = (AutoCompleteTextView) findViewById(R.id.name);
        audioNameEditText.setAdapter(new AudiosSuggestionsAdapter(this, audioDataManager));
        final String editingAudioName = editingAudio.getName();
        audioNameEditText.setText(editingAudioName);
        artistsSuggestionsAdapter.setTrackName(editingAudioName);

        GuiUtilities.setEditTextFocusChangedListener(audioNameEditText,
                new GuiUtilities.EditTextFocusListener() {
                    @Override
                    public void onFocusEnter() {
                        audioNameEditText.showDropDown();
                    }

                    @Override
                    public void onFocusLeave(boolean textChanged, String textBefore) {
                        if (textChanged) {
                            onAudioNameChanged();
                            updateAlbum(null);
                        }
                    }
                });
        GuiUtilities.setEditTextFocusChangedListener(artistNameEditText,
                new GuiUtilities.EditTextFocusListener() {
                    @Override
                    public void onFocusEnter() {
                        artistNameEditText.showDropDown();
                    }

                    @Override
                    public void onFocusLeave(boolean textChanged, String textBefore) {
                        if(textChanged){
                            onArtistNameChanged();
                            updateAlbum(null);
                        }
                    }
                });

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOk();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
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
        audioBeforeEditing = new Audio(editingAudio);

        initViews();
    }
}

package com.example.FlyingDog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Audio;

/**
 * Created by CM on 8/30/2014.
 */
public class EditAudioActivity extends Activity {
    private static final String AUDIO_ID_KEY = "AUDIO_ID_KEY";
    private AudioDataManager audioDataManager;
    private Audio audio;

    public static void start(Context context, long audioId) {
        Intent intent = new Intent(context, EditAudioActivity.class);
        intent.putExtra(AUDIO_ID_KEY, audioId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long audioId = getIntent().getLongExtra(AUDIO_ID_KEY, -1);
        if(audioId < 0){
            throw new IllegalArgumentException("audioId < 0");
        }

        audioDataManager = FlyingDog.getInstance().getAudioDataManager();
        audio = audioDataManager.getSongById(audioId);

        setContentView(R.layout.edit_audio);
    }
}

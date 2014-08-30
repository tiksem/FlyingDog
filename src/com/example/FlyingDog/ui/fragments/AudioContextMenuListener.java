package com.example.FlyingDog.ui.fragments;

import android.content.Context;
import android.view.MenuItem;
import com.example.FlyingDog.EditAudioActivity;
import com.example.FlyingDog.R;
import com.tiksem.media.data.Audio;

/**
 * Created by CM on 8/30/2014.
 */
public class AudioContextMenuListener implements MenuItem.OnMenuItemClickListener {
    private Audio audio;
    private Context context;

    public AudioContextMenuListener(Audio audio, Context context) {
        this.audio = audio;
        this.context = context;
    }

    private void onEdit() {
        EditAudioActivity.start(context, audio.getId());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                onEdit();
                break;
        }

        return true;
    }
}

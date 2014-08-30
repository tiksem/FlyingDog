package com.example.FlyingDog.ui;

import android.content.Context;
import com.example.FlyingDog.R;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.NamedData;

import java.util.List;

/**
 * Created by CM on 8/30/2014.
 */
public class CreateArtistAlert extends CreateMediaAlert {
    public CreateArtistAlert(AudioDataManager audioDataManager, Context context) {
        super(audioDataManager, context);
    }

    @Override
    protected List<? extends NamedData> getNamedDataList(AudioDataManager audioDataManager) {
        return audioDataManager.getArtists();
    }

    @Override
    protected int getMediaNameId() {
        return R.string.artist;
    }

    @Override
    protected void addMediaToDataBase(AudioDataManager audioDataManager, String mediaName) {
        audioDataManager.addArtist(mediaName);
    }
}

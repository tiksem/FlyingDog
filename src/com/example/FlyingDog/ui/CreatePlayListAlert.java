package com.example.FlyingDog.ui;

import android.content.Context;
import com.example.FlyingDog.R;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.NamedData;

import java.util.List;

/**
 * Created by CM on 8/29/2014.
 */
public class CreatePlayListAlert extends CreateMediaAlert {
    public CreatePlayListAlert(AudioDataManager audioDataManager, Context context) {
        super(audioDataManager, context);
    }

    @Override
    protected List<? extends NamedData> getNamedDataList(AudioDataManager audioDataManager) {
        return audioDataManager.getPlayLists();
    }

    @Override
    protected int getMediaNameId() {
        return R.string.play_list;
    }

    @Override
    protected void addMediaToDataBase(AudioDataManager audioDataManager, String mediaName) {
        audioDataManager.addPlayList(mediaName);
    }
}
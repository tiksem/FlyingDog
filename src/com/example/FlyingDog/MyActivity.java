package com.example.FlyingDog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import com.example.FlyingDog.setup.ImageLoaderConfigFactory;
import com.example.FlyingDog.ui.adapters.SongsAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tiksem.media.local.AndroidAudioDataBase;
import com.tiksem.media.local.LocalAudioDataBase;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView listView = new ListView(this);
        setContentView(listView);
        ImageLoader.getInstance().init(ImageLoaderConfigFactory.getCommonImageLoaderConfig(this));
        LocalAudioDataBase audioDataBase = new AndroidAudioDataBase(getContentResolver());
        SongsAdapter adapter = new SongsAdapter(this);
        listView.setAdapter(adapter);
        adapter.setElements(audioDataBase.getSongs());
    }
}

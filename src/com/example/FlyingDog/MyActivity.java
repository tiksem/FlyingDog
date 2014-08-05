package com.example.FlyingDog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.tiksem.media.local.AndroidAudioDataBase;
import com.tiksem.media.local.LocalAudioDataBase;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LocalAudioDataBase audioDataBase = new AndroidAudioDataBase(getContentResolver());
        Log.i("MyActivity", audioDataBase.getSongs().toString());
    }
}

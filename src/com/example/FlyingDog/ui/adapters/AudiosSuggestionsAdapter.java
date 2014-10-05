package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.example.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.suggestions.ArtistSuggestionsProvider;
import com.tiksem.media.search.suggestions.AudioSuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */

public class AudiosSuggestionsAdapter
        extends SuggestionsAdapter<Audio, ArtCollectionHolder> {
    private static final String NULL_ITEM_TEXT = "Unknown artist";
    private static final int MAX_ARTISTS_COUNT = 10;

    public AudiosSuggestionsAdapter(Context context, AudioDataManager audioDataManager) {
        super(new ArtCollectionSpinnerAdapter<Audio>(context, NULL_ITEM_TEXT),
                new AudioSuggestionsProvider(audioDataManager, MAX_ARTISTS_COUNT));
    }
}

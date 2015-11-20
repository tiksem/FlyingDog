package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.suggestions.AudioSuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */

public class AudiosSuggestionsAdapter
        extends SuggestionsAdapter<Audio, ArtCollectionHolder> {
    private static final String NULL_ITEM_TEXT = "Unknown artist";
    private static final int MAX_ARTISTS_COUNT = 10;

    private final AudioSuggestionsProvider suggestionsProvider;
    private String artistName;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
        suggestionsProvider.setArtistName(artistName);
    }

    public AudiosSuggestionsAdapter(Context context, AudioDataManager audioDataManager) {
        setViewArrayAdapter(new ArtCollectionSpinnerAdapter<Audio>(context, NULL_ITEM_TEXT));
        suggestionsProvider = new AudioSuggestionsProvider(audioDataManager, MAX_ARTISTS_COUNT);
        setSuggestionsProvider(suggestionsProvider);
    }
}

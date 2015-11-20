package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Artist;
import com.tiksem.media.search.suggestions.ArtistSuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */
public class ArtistsSuggestionsAdapter
        extends SuggestionsAdapter<Artist, ArtCollectionHolder> {
    private static final String NULL_ITEM_TEXT = "Unknown artist";
    private static final int MAX_ARTISTS_COUNT = 10;
    private ArtistSuggestionsProvider suggestionsProvider;

    public void setTrackName(String trackName) {
        suggestionsProvider.setTrackName(trackName);
        notifyDataSetChanged();
    }

    public ArtistsSuggestionsAdapter(Context context, AudioDataManager audioDataManager) {
        setViewArrayAdapter(new ArtCollectionSpinnerAdapter<Artist>(context, NULL_ITEM_TEXT));
        suggestionsProvider = new ArtistSuggestionsProvider(audioDataManager, MAX_ARTISTS_COUNT);
        setSuggestionsProvider(suggestionsProvider);
    }
}

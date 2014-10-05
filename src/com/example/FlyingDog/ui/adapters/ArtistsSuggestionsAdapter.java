package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.example.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Artist;
import com.tiksem.media.search.suggestions.ArtistSuggestionsProvider;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;
import com.utilsframework.android.adapters.ViewArrayAdapter;

/**
 * Created by CM on 10/5/2014.
 */
public class ArtistsSuggestionsAdapter
        extends SuggestionsAdapter<Artist, ArtCollectionHolder> {
    private static final String NULL_ITEM_TEXT = "Unknown artist";
    private static final int MAX_ARTISTS_COUNT = 10;

    public ArtistsSuggestionsAdapter(Context context, AudioDataManager audioDataManager) {
        super(new ArtCollectionSpinnerAdapter<Artist>(context, NULL_ITEM_TEXT),
                new ArtistSuggestionsProvider(audioDataManager, MAX_ARTISTS_COUNT));
    }
}

package com.tiksem.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.FlyingDog.ui.adapters.holders.ArtCollectionHolder;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Artist;
import com.tiksem.media.search.InternetSearchEngine;
import com.tiksem.media.search.suggestions.ArtistSuggestionsProvider;
import com.utils.framework.suggestions.SuggestionsProvider;
import com.utilsframework.android.adapters.SuggestionsAdapter;

/**
 * Created by CM on 10/5/2014.
 */
public class ArtistsSuggestionsAdapter
        extends EditAudioStringSuggestionsAdapter {
    public ArtistsSuggestionsAdapter(Context context, SuggestionsProvider<String> suggestionsProvider) {
        super(context, suggestionsProvider);
    }
}

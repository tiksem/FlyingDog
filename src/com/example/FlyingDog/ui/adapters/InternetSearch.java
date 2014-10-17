package com.example.FlyingDog.ui.adapters;

import android.content.Context;
import com.tiksem.media.AudioDataManager;
import com.tiksem.media.data.Album;
import com.tiksem.media.data.Artist;
import com.tiksem.media.data.Audio;
import com.utilsframework.android.adapters.SuggestionsAdapter;

import java.util.List;

/**
 * Created by CM on 10/15/2014.
 */
public final class InternetSearch {
    public static SuggestionsAdapter createSuggestionsAdapterForClass(AudioDataManager audioDataManager,
                                                                      Context context,
                                                                      Class aClass) {
        if(Audio.class.isAssignableFrom(aClass)){
            return new ArtistsSuggestionsAdapter(context, audioDataManager);
        } else if(Artist.class.isAssignableFrom(aClass)) {
            return new ArtistsSuggestionsAdapter(context, audioDataManager);
        } else if(Album.class.isAssignableFrom(aClass)) {
            return new AlbumsSuggestionsAdapter(context, audioDataManager);
        } else {
            throw new IllegalArgumentException("Class " + aClass.getCanonicalName() + " is not supported");
        }
    }

    public static List search(String query, AudioDataManager audioDataManager, Class aClass) {
        if(Audio.class.isAssignableFrom(aClass)){
            return audioDataManager.getSongs(query);
        } else if(Artist.class.isAssignableFrom(aClass)) {
            return audioDataManager.getArtists(query);
        } else if(Album.class.isAssignableFrom(aClass)) {
            return audioDataManager.getAlbums(query);
        } else {
            throw new IllegalArgumentException("Class " + aClass.getCanonicalName() + " is not supported");
        }
    }
}

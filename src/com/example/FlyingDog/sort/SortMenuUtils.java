package com.example.FlyingDog.sort;

import com.example.FlyingDog.R;
import com.tiksem.media.data.Audio;
import com.tiksem.media.data.AudioComparators;

import java.util.Collections;
import java.util.List;

/**
 * Created by stykhonenko on 21.10.15.
 */
public class SortMenuUtils {
    public static void sortAudios(List<Audio> audios, int menuItemId) {
        if (menuItemId == R.id.sort_by_name) {
            Collections.sort(audios, AudioComparators.audioByName());
        } else if(menuItemId == R.id.sort_artist_name) {
            Collections.sort(audios, AudioComparators.audioByArtistName());
        } else if(menuItemId == R.id.shuffle) {
            Collections.shuffle(audios);
        }
    }

    public static void sortGenres(List<String> genres, int menuItemId) {
        if (menuItemId == R.id.sort_by_name) {
            Collections.sort(genres);
        }
    }
}

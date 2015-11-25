package com.tiksem.FlyingDog.songs_you_may_like;

import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.FlyingDog.ui.fragments.AllSongsFragment;
import com.tiksem.media.data.Audio;
import com.utils.framework.CollectionUtils;
import com.utils.framework.Predicate;
import com.utils.framework.collections.NavigationList;
import com.utils.framework.strings.Strings;

/**
 * Created by stykhonenko on 25.11.15.
 */
public class YourSongsFragment extends AllSongsFragment {
    @Override
    protected NavigationList<Audio> getAudiosFromInternet(final String filter, RequestManager requestManager) {
        return NavigationList.decorate(CollectionUtils.findAll(getLocalSongs(), new Predicate<Audio>() {
            @Override
            public boolean check(Audio audio) {
                if (Strings.containsIgnoreCase(audio.getName(), filter) ||
                        Strings.containsIgnoreCase(audio.getArtistName(), filter)) {
                    return true;
                }

                String albumName = audio.getAlbumName();
                if (albumName != null) {
                    return Strings.containsIgnoreCase(albumName, filter);
                }

                return false;
            }
        }));
    }
}

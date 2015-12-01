package com.tiksem.FlyingDog.songs_you_may_like;

import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.FlyingDog.ui.fragments.SongsYouMayLikeFragment;
import com.tiksem.media.data.Audio;
import com.tiksem.media.search.syouml.SongsYouMayLikeService;
import com.utils.framework.collections.NavigationList;

/**
 * Created by stykhonenko on 01.12.15.
 */
public class SuggestedSongsFragment extends SongsYouMayLikeFragment {
    private boolean shouldUpdateToGenresBaseSearch = false;

    @Override
    protected NavigationList<Audio> getAudiosFromInternet(String filter, RequestManager requestManager) {
        if (shouldUpdateToGenresBaseSearch) {
            SongsYouMayLikeService.Binder binder = getBinder();
            if (binder != null && !binder.searchSongsByGenres()) {
                binder.setSearchSongsByGenres(true);
                shouldUpdateToGenresBaseSearch = false;
                return binder.reload();
            }
        }

        return super.getAudiosFromInternet(filter, requestManager);
    }

    @Override
    protected void onEmptyResults() {
        SongsYouMayLikeService.Binder binder = getBinder();
        if (binder != null && !binder.searchSongsByGenres()) {
            shouldUpdateToGenresBaseSearch = true;
            updateNavigationListWithLastFilter();
            return;
        }

        super.onEmptyResults();
    }
}

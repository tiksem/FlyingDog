package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.tiksem.media.data.Artist;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 05.11.15.
 */
public class CountryArtistsFragment extends ArtistsOfTagFragment {
    public static CountryArtistsFragment create(String tag) {
        return create(tag, new CountryArtistsFragment());
    }

    @Override
    protected NavigationList<Artist> createInternetList(String filter) {
        return getRequestManager().getArtistsByCountry(getTagName(), filter);
    }
}

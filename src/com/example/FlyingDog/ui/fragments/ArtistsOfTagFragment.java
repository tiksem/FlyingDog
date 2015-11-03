package com.example.FlyingDog.ui.fragments;

import android.app.Activity;
import com.tiksem.media.data.Artist;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.fragments.Fragments;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class ArtistsOfTagFragment extends ArtistsFragment implements TagProvider {
    private static final String TAG = "TAG";

    private String tag;

    public static ArtistsOfTagFragment create(String tag) {
        return Fragments.createFragmentWith1Arg(new ArtistsOfTagFragment(), TAG, tag);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tag = getArguments().getString(TAG);
    }

    @Override
    protected List<Artist> createLocalList() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected NavigationList<Artist> createInternetList(String filter) {
        return getRequestManager().getArtistsByTag(tag);
    }

    @Override
    protected boolean alwaysUseNavigationList() {
        return true;
    }

    @Override
    protected boolean hasSearchMenu() {
        return false;
    }

    @Override
    public String getTagName() {
        return tag;
    }
}

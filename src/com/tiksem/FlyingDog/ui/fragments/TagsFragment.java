package com.tiksem.FlyingDog.ui.fragments;

import com.tiksem.FlyingDog.network.RequestManager;
import com.tiksem.FlyingDog.ui.Level;
import com.tiksem.FlyingDog.ui.adapters.TagsAdapter;
import com.utils.framework.algorithms.Search;
import com.utils.framework.collections.NavigationList;
import com.utilsframework.android.adapters.ViewArrayAdapter;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public abstract class TagsFragment extends AbstractAudioDataFragment<String> {
    @Override
    protected List<String> createLocalList() {
        throw new RuntimeException("Should not be called");
    }

    protected abstract List<String> getTags();

    @Override
    protected ViewArrayAdapter<String, ?> createAdapter(RequestManager requestManager) {
        return new TagsAdapter(getActivity());
    }

    @Override
    protected void onItemSelected(String tag, int position) {
        getPlayListsActivity().replaceFragment(SongsOfTagFragment.create(tag), Level.TAG_SONGS_AND_ARTISTS);
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected NavigationList<String> getNavigationList(String filter) {
        List<String> tags = getTags();
        if (filter != null) {
            tags = Search.filterIgnoreCase(tags, filter);
        }

        return NavigationList.decorate(tags);
    }
}

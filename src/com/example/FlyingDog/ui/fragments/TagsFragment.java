package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.network.RequestManager;
import com.example.FlyingDog.ui.Level;
import com.example.FlyingDog.ui.adapters.TagsAdapter;
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

    protected abstract List<String> getTags(String filter);

    @Override
    protected ViewArrayAdapter<String, ?> createAdapter(RequestManager requestManager) {
        return new TagsAdapter(getActivity());
    }

    @Override
    protected void onListItemClicked(String tag, int position) {
        getPlayListsActivity().replaceFragment(SongsOfTagFragment.create(tag), Level.TAG_SONGS_AND_ARTISTS);
    }

    @Override
    protected boolean hasSearchMenu() {
        return true;
    }

    @Override
    protected NavigationList<String> getNavigationList(String filter) {
        return NavigationList.decorate(getTags(filter));
    }
}

package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.ui.Level;
import com.tiksem.media.data.Mood;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class MoodFragment extends TagsFragment {
    @Override
    protected List<String> getTags() {
        return Mood.getMoods();
    }

    @Override
    protected void onListItemClicked(String tag, int position) {
        super.onListItemClicked(tag, position);
        getPlayListsActivity().replaceFragment(MoodSongsFragment.create(tag), Level.MOOD_SONGS);
    }
}

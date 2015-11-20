package com.tiksem.FlyingDog.ui.fragments;

import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.ui.Level;
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
    protected void onItemSelected(String tag, int position) {
        getPlayListsActivity().replaceFragment(MoodSongsFragment.create(tag), Level.MOOD_SONGS);
    }

    @Override
    protected CharSequence getEmptyListText() {
        return getString(R.string.no_mood_found);
    }
}

package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.example.FlyingDog.ui.Level;
import com.tiksem.media.data.Countries;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class CountriesFragment extends TagsFragment {
    @Override
    protected List<String> getTags() {
        return Countries.getCountries();
    }

    @Override
    protected void onItemSelected(String tag, int position) {
        getPlayListsActivity().replaceFragment(CountrySongsFragment.create(tag), Level.COUNTRY_SONGS_AND_ARTISTS);
    }

    @Override
    protected CharSequence getEmptyListText() {
        return getString(R.string.no_countries_found);
    }
}

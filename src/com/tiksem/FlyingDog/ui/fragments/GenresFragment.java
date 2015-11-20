package com.tiksem.FlyingDog.ui.fragments;

import com.tiksem.FlyingDog.R;
import com.tiksem.FlyingDog.sort.SortMenuUtils;
import com.tiksem.media.data.Genres;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class GenresFragment extends TagsFragment {
    @Override
    protected List<String> getTags() {
        return Genres.getGenresList();
    }

    @Override
    protected int getSortMenuId() {
        return R.menu.sort_genres;
    }

    @Override
    protected void sort(List<String> items, int sortingOrder) {
        SortMenuUtils.sortGenres(items, sortingOrder);
    }

    @Override
    protected CharSequence getEmptyListText() {
        return getString(R.string.no_genres_found);
    }
}

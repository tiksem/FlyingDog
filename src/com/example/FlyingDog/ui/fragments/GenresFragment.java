package com.example.FlyingDog.ui.fragments;

import com.example.FlyingDog.R;
import com.example.FlyingDog.sort.SortMenuUtils;
import com.tiksem.media.data.Genres;
import com.utils.framework.algorithms.Search;

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
}

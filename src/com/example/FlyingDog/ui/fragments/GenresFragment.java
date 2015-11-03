package com.example.FlyingDog.ui.fragments;

import com.tiksem.media.data.Genres;
import com.utils.framework.algorithms.Search;

import java.util.List;

/**
 * Created by stykhonenko on 03.11.15.
 */
public class GenresFragment extends TagsFragment {
    @Override
    protected List<String> getTags(String filter) {
        if (filter == null) {
            return Genres.getGenresList();
        } else {
            return Search.filter(Genres.getGenresList(), filter.toLowerCase());
        }
    }
}

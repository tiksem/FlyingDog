package com.example.FlyingDog.ui.fragments;

import com.tiksem.media.data.Audio;
import com.utils.framework.collections.NavigationList;

import java.util.List;

/**
 * Created by stykhonenko on 07.11.15.
 */
public class CurrentPlayListInfo {
    private NavigationList<Audio> currentPlayList;
    private List<Audio> originalOrderedList;
    private int sortOrder;

    public NavigationList<Audio> getCurrentPlayList() {
        return currentPlayList;
    }

    public List<Audio> getOriginalOrderedList() {
        return originalOrderedList;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setCurrentPlayList(NavigationList<Audio> currentPlayList) {
        this.currentPlayList = currentPlayList;
    }

    public void setOriginalOrderedList(List<Audio> originalOrderedList) {
        this.originalOrderedList = originalOrderedList;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}

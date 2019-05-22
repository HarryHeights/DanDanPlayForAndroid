package com.xyoye.dandanplay2.bean.event;

/**
 * Created by xyy on 2019/1/8.
 */

public class SearchHistoryEvent {
    private String searchText;

    public SearchHistoryEvent(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}

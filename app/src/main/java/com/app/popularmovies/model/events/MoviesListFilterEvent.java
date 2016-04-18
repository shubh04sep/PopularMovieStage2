package com.app.popularmovies.model.events;

/**
 * Created by rahul on 15/3/16.
 */
public class MoviesListFilterEvent {

    private String filter;

    public MoviesListFilterEvent(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }
}

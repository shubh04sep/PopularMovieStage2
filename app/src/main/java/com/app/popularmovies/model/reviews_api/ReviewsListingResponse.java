package com.app.popularmovies.model.reviews_api;

import java.util.ArrayList;

/**
 * Created by Rahul on 3/25/2016.
 */
public class ReviewsListingResponse {
    long id, page, total_pages, total_results;

    ArrayList<ReviewsEntity> results;

    public long getId() {
        return id;
    }

    public long getPage() {
        return page;
    }

    public long getTotal_pages() {
        return total_pages;
    }

    public long getTotal_results() {
        return total_results;
    }

    public ArrayList<ReviewsEntity> getResults() {
        return results;
    }

    public static class ReviewsEntity {
        String id, author, content, url;

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }
}

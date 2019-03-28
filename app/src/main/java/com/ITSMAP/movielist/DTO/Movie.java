package com.ITSMAP.movielist.DTO;

import android.os.Parcelable;

public class Movie extends com.ITSMAP.movielist.JSONResponse.Movie implements Parcelable {
    private String userRating;
    private String userComment;
    private boolean watched;

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}



package com.ITSMAP.movielist.DTO;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String Name;
    private String Plot;
    private String Genres;
    private String iMDBRating;
    private String UserRating;
    private String UserComment;
    private boolean watched;
    private boolean userRating;
    private boolean userComment;

    public boolean hasUserComment() {
        return userComment;
    }

    public void setUserComment(boolean userCommentBool) {
        this.userComment = userCommentBool;
    }

    public boolean hasUserRating() {
        return userRating;
    }

    public void setUserRating(boolean userRating) {
        this.userRating = userRating;
    }

    public String getUserComment() {
        return UserComment;
    }

    public void setUserComment(String userComment) {
        UserComment = userComment;
    }


    public boolean hasBeenWatched() {
        return watched;
    }

    public void setWatchStatus(boolean watchStatus) {
        this.watched = watchStatus;
    }


    public Movie(String name, String plot, String genres, String iMDBRating) {
        Name = name;
        Plot = plot;
        Genres = genres;
        this.iMDBRating = iMDBRating;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getGenres() {
        return Genres;
    }

    public void setGenres(String genres) {
        Genres = genres;
    }

    public String getiMDBRating() {
        return iMDBRating;
    }

    public void setiMDBRating(String iMDBRating) {
        this.iMDBRating = iMDBRating;
    }

    public String getUserRating() {
        return UserRating;
    }

    public void setUserRating(String userRating) {
        UserRating = userRating;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Name);
        dest.writeString(this.Genres);
        dest.writeString(this.iMDBRating);
        dest.writeString(this.Plot);
        dest.writeString(this.UserRating);
        dest.writeString(this.UserComment);
        dest.writeInt(watched ? 1 : 0);
        dest.writeInt(userRating ? 1 : 0);
        dest.writeInt(userComment ? 1 : 0);
    }

    private static Movie readFromParcel(Parcel in) {
        String name = in.readString();
        String genres = in.readString();
        String iMDBRating = in.readString();
        String plot = in.readString();
        String userRating = in.readString();
        String userComment = in.readString();
        boolean watchStatus = false;
        boolean UserRating = false;
        boolean UserComment = false;
        if (in.readInt() == 1) {
            watchStatus = true;
        }
        if (in.readInt() == 1) {
            UserRating = true;
        }
        if (userComment != null) {
            UserComment = true;
        }
        Movie movie = new Movie(name, plot, genres, iMDBRating);
        movie.setUserComment(UserComment);
        movie.setUserComment(userComment);
        movie.setUserRating(UserRating);
        movie.setUserRating(userRating);
        movie.setWatchStatus(watchStatus);
        return movie;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return readFromParcel(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

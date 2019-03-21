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
        dest.writeString(this.Plot);
        dest.writeString(this.Genres);
        dest.writeString(this.iMDBRating);
        dest.writeString(this.UserRating);
        dest.writeString(this.UserComment);
        dest.writeByte(this.watched ? (byte) 1 : (byte) 0);
        dest.writeByte(this.userRating ? (byte) 1 : (byte) 0);
        dest.writeByte(this.userComment ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.Name = in.readString();
        this.Plot = in.readString();
        this.Genres = in.readString();
        this.iMDBRating = in.readString();
        this.UserRating = in.readString();
        this.UserComment = in.readString();
        this.watched = in.readByte() != 0;
        this.userRating = in.readByte() != 0;
        this.userComment = in.readByte() != 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

package com.ITSMAP.movielist.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Movie implements Parcelable {

    private String Name;
    private String Plot;
    private String Genres;
    private String iMDBRating;
    private String UserRating;

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
    }

    private static Movie readFromParcel(Parcel in) {
        String name = in.readString();
        String genres = in.readString();
        String iMDBRating = in.readString();
        String plot = in.readString();
        return new Movie(name,plot,genres,iMDBRating);
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

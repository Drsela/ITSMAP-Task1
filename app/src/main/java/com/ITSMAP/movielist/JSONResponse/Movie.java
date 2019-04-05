package com.ITSMAP.movielist.JSONResponse;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "db_movies")
public class Movie implements Parcelable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)

    private int id;

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Year")
    @Expose
    private String year;
    @SerializedName("Rated")
    @Expose
    private String rated;
    @SerializedName("Released")
    @Expose
    private String released;
    @SerializedName("Runtime")
    @Expose
    private String runtime;
    @SerializedName("Genre")
    @Expose
    private String genre;
    @SerializedName("Director")
    @Expose
    private String director;
    @SerializedName("Writer")
    @Expose
    private String writer;
    @SerializedName("Actors")
    @Expose
    private String actors;
    @SerializedName("Plot")
    @Expose
    private String plot;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("Awards")
    @Expose
    private String awards;
    @SerializedName("Poster")
    @Expose
    private String poster;

    @Ignore
    @SerializedName("Ratings")
    @Expose
    private List<Rating> ratings = null;
    @SerializedName("Metascore")
    @Expose
    private String metascore;
    @SerializedName("imdbRating")
    @Expose
    private String imdbRating;
    @SerializedName("imdbVotes")
    @Expose
    private String imdbVotes;
    @SerializedName("imdbID")
    @Expose
    private String imdbID;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("DVD")
    @Expose
    private String dVD;
    @SerializedName("BoxOffice")
    @Expose
    private String boxOffice;
    @SerializedName("Production")
    @Expose
    private String production;
    @SerializedName("Website")
    @Expose
    private String website;
    @SerializedName("Response")
    @Expose
    private String response;

    @Expose(serialize = false)
    private String userComment = null;

    @Expose(serialize = false)
    private String personalRating = null;

    @Expose(serialize = false)
    private boolean watched = false;

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(String personalRating) {
        this.personalRating = personalRating;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDVD() {
        return dVD;
    }

    public void setDVD(String dVD) {
        this.dVD = dVD;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    public Movie() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeString(this.rated);
        dest.writeString(this.released);
        dest.writeString(this.runtime);
        dest.writeString(this.genre);
        dest.writeString(this.director);
        dest.writeString(this.writer);
        dest.writeString(this.actors);
        dest.writeString(this.plot);
        dest.writeString(this.language);
        dest.writeString(this.country);
        dest.writeString(this.awards);
        dest.writeString(this.poster);
        dest.writeList(this.ratings);
        dest.writeString(this.metascore);
        dest.writeString(this.imdbRating);
        dest.writeString(this.imdbVotes);
        dest.writeString(this.imdbID);
        dest.writeString(this.type);
        dest.writeString(this.dVD);
        dest.writeString(this.boxOffice);
        dest.writeString(this.production);
        dest.writeString(this.website);
        dest.writeString(this.response);
        dest.writeString(this.userComment);
        dest.writeString(this.personalRating);
        dest.writeByte(this.watched ? (byte) 1 : (byte) 0);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.year = in.readString();
        this.rated = in.readString();
        this.released = in.readString();
        this.runtime = in.readString();
        this.genre = in.readString();
        this.director = in.readString();
        this.writer = in.readString();
        this.actors = in.readString();
        this.plot = in.readString();
        this.language = in.readString();
        this.country = in.readString();
        this.awards = in.readString();
        this.poster = in.readString();
        this.ratings = new ArrayList<>();
        in.readList(this.ratings, Rating.class.getClassLoader());
        this.metascore = in.readString();
        this.imdbRating = in.readString();
        this.imdbVotes = in.readString();
        this.imdbID = in.readString();
        this.type = in.readString();
        this.dVD = in.readString();
        this.boxOffice = in.readString();
        this.production = in.readString();
        this.website = in.readString();
        this.response = in.readString();
        this.userComment = in.readString();
        this.personalRating = in.readString();
        this.watched = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public static List<Movie> populateFromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Movie>>() {
        }.getType());
    }
}



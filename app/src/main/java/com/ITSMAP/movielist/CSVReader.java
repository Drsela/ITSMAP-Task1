package com.ITSMAP.movielist;

import com.ITSMAP.movielist.DTO.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    private InputStream inputStream;

    public CSVReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List getMovies() {
        List resultList = new ArrayList<Movie>();
        BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(";");
                resultList.add(new Movie(row[0], row[1], row[2], row[3]));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading CSV File");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream");
            }
        }

        return resultList;
    }
}

package com.example.myandroidapplication.Model.genre;

import java.util.ArrayList;
import java.util.Arrays;

public class GenreResponse {
    private Genre[] genres;

    public ArrayList<Genre> getGenre() {
        return new ArrayList<Genre>(Arrays.asList(genres));
    }

    class Genres {
        private int id;
        private String name;
    }
}

package com.example.myandroidapplication.Model.genre;

public class Genre {
    private int id;
    private String name;
    int imageId;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }

    public Genre getGenre() {
        return new Genre(id, name);
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



package com.bomberomedia.popularmoviesstage1;

class MyMovie {
    String title;

    String synopsis; //overview in the api
    double rating;  //vote average in api
    String releaseDate;

    String filePath;

    boolean isAdult;

    String getImageUrl(){
        String baseUrl = "https://image.tmdb.org/t/p";
        String fileSize = "/w500";
        return baseUrl + fileSize + filePath;
    }

    @Override
    public String toString() {
        return "{Title: " + title + "} \n" +
                "{poster url: " + getImageUrl() + "} \n" +
                "{release date: " + releaseDate + "} \n" +
                "{rating: " + String.valueOf(rating) + "} \n";
    }
}

package com.bomberomedia.popularmoviesstage1;

public class MyMovie {
    String title;
    String imageUrl;

    String synopsis; //overview in the api
    double rating;  //vote average in api
    String releaseDate;

    String baseUrl = "https://image.tmdb.org/t/p";
    String fileSize = "/w500";
    String filePath;

    public String getImageUrl(){
        imageUrl = baseUrl + fileSize + filePath;
        return imageUrl;
    }
}

package com.example.hw9;

public class ArtistModel {

    private String Artist_name;
    private String ID;
    private String Followers;
    private String Popularity;
    private String Spotify;
    private String ArtistImage;
    private String AlbumImage1;
    private String AlbumImage2;
    private String AlbumImage3;


    // Constructor
    public ArtistModel(String Artist_name,String Followers, String ArtistImage, String AlbumImage1, String AlbumImage2, String AlbumImage3, String Spotify, String Popularity) {
        this.Artist_name = Artist_name;
        this.Followers = Followers;
        this.ArtistImage = ArtistImage;
        this.AlbumImage1 = AlbumImage1;
        this.AlbumImage2 = AlbumImage2;
        this.AlbumImage3 = AlbumImage3;
        this.Spotify = Spotify;
        this.Popularity = Popularity;
    }

    // Getter and Setter
    public String getArtist_name() {
        return Artist_name;
    }
    public void setArtist_name(String artist_name) {
        this.Artist_name = artist_name;
    }

    // Getter and Setter
    public String getArtist_Followers() {
        return Followers;
    }
    public void setArtist_Followers(String artist_followers) {
        this.Followers = artist_followers;
    }

    // Getter and Setter
    public String getArtist_Image() {
        return ArtistImage;
    }
    public void setArtist_Image(String artist_image) {
        this.ArtistImage = artist_image;
    }

    // Getter and Setter
    public String getArtist_Spotify() {
        return Spotify;
    }
    public void setArtist_Spotify(String artist_spotify) {
        this.Spotify = artist_spotify;
    }

    // Getter and Setter
    public String getArtist_popularity() {
        return Popularity;
    }
    public void setArtist_popularity(String artist_popularity) {
        this.Popularity = artist_popularity;
    }

    // Getter and Setter
    public String getArtist_AlbumImage1() {
        return AlbumImage1;
    }
    public void setArtist_AlbumImage1(String album_image) {
        this.AlbumImage1 = album_image;
    }

    // Getter and Setter
    public String getArtist_AlbumImage2() {
        return AlbumImage2;
    }

    public void setArtist_AlbumImage2(String album_image) {
        this.AlbumImage2 = album_image;
    }

    // Getter and Setter
    public String getArtist_AlbumImage3() {
        return AlbumImage1;
    }
    public void setArtist_AlbumImage3(String album_image) {
        this.AlbumImage1 = album_image;
    }



}
package model.data.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Book {
    private final int id;
    private String title;
    private String author;
    private String synopsis;
    private String language;
    private List<String> genres;
    private boolean availability;
    private double costPerDownload;
    private Map<String, String> downloadFiles;
    private String imagePath;
    private List<Review> reviews;

    public Book(int id, String title, String author, String synopsis, String language, List<String> genres, boolean availability, double costPerDownload, Map<String, String> downloadFiles, String imagePath)
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.language = language;
        this.genres = genres;
        this.availability = availability;
        this.costPerDownload = costPerDownload;
        this.downloadFiles = downloadFiles;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public double getCostPerDownload() {
        return costPerDownload;
    }

    public void setCostPerDownload(double costPerDownload) {
        this.costPerDownload = costPerDownload;
    }

    public Map<String, String> getDownloadFiles() {
        return downloadFiles;
    }

    public void setDownloadFiles(Map<String, String> downloadFiles) {
        this.downloadFiles = downloadFiles;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", synopsis='" + synopsis + '\'' +
                ", language='" + language + '\'' +
                ", genres=" + genres +
                ", availability=" + availability +
                ", costPerDownload=" + costPerDownload +
                ", downloadFiles=" + downloadFiles +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}

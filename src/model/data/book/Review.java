package model.data.book;

public class Review {
    String userEmail;
    int rating;
    String review;

    public Review(String userEmail, int rating, String review) {
        this.userEmail = userEmail;
        this.rating = rating;
        this.review = review;
    }
}

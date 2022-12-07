package model.data.book;

public class Review {
    int id;
    String userEmail;
    int rating;
    String review;

    public Review(int id, String userEmail, int rating, String review) {
        this.id = id;
        this.userEmail = userEmail;
        this.rating = rating;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

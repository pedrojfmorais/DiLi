package pt.isec.gps.dili.model.data.book;

public class Review {
    private final int id;
    private final String userEmail;
    private final int rating;
    private String review;

    public Review(int id, String userEmail, int rating, String review) {
        this.id = id;
        this.userEmail = userEmail;
        this.rating = rating;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

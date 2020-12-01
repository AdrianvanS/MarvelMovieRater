public class Movie {

    private String title;
    private String released;
    private int rating;

    public Movie(String title, String released, int rating) {
        this.title = title;
        this.released = released;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getReleased() {
        return released;
    }

    public int getRating() {
        return rating;
    }

}

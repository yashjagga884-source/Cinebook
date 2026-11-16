package cinebook;

/** Immutable movie catalogue entry. */
public final class Movie {
    private final String movieId, title, genre, language;
    private final int durationMinutes;
    private final double rating;

    public Movie(String movieId, String title, String genre, String language, int durationMinutes, double rating) {
        if (movieId == null || movieId.isBlank() || title == null || title.isBlank()) throw new IllegalArgumentException("Movie ID and title are required.");
        this.movieId = movieId; this.title = title; this.genre = genre; this.language = language;
        this.durationMinutes = durationMinutes; this.rating = rating;
    }
    public String getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getLanguage() { return language; }
    public int getDurationMinutes() { return durationMinutes; }
    public double getRating() { return rating; }
    @Override public String toString() { return String.format("[%s] %s (%s) | %s | %d min | %.1f/10", movieId, title, language, genre, durationMinutes, rating); }
}

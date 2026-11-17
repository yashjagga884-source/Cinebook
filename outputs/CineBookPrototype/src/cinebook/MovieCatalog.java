package cinebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** In-memory movie collection with ID lookup and case-insensitive search. */
public final class MovieCatalog {
    private final Map<String, Movie> movies = new LinkedHashMap<>();
    public void add(Movie movie) { if (movies.putIfAbsent(movie.getMovieId(), movie) != null) throw new IllegalArgumentException("Duplicate movie ID."); }
    public Movie find(String id) { return movies.get(id == null ? "" : id.toUpperCase(Locale.ROOT)); }
    public List<Movie> getAll() { return Collections.unmodifiableList(new ArrayList<>(movies.values())); }
    public List<Movie> search(String query) {
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (q.isEmpty()) return List.of();
        return movies.values().stream().filter(m -> m.getTitle().toLowerCase(Locale.ROOT).contains(q) || m.getGenre().toLowerCase(Locale.ROOT).contains(q)).toList();
    }
}

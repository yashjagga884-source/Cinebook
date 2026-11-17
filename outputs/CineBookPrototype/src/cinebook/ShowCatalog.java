package cinebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** In-memory scheduled show collection. */
public final class ShowCatalog {
    private final Map<String, Show> shows = new LinkedHashMap<>();
    public void add(Show show) { if (shows.putIfAbsent(show.getShowId(), show) != null) throw new IllegalArgumentException("Duplicate show ID."); }
    public Show find(String id) { return shows.get(id == null ? "" : id.toUpperCase(Locale.ROOT)); }
    public List<Show> getAll() { return Collections.unmodifiableList(new ArrayList<>(shows.values())); }
    public List<Show> forMovie(String movieId) { return shows.values().stream().filter(s -> s.getMovie().getMovieId().equalsIgnoreCase(movieId)).toList(); }
}

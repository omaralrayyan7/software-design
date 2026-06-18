package lms.decorator;

import lms.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * DECORATOR PATTERN — Concrete Decorator
 *
 * Adds advanced filtering capabilities on top of the base search:
 *   - searchByRating: filter results to books with rating >= minRating
 *   - searchByLanguage: filter results to books in a specific language
 *
 * These methods first call the wrapped search() to get a candidate set,
 * then apply the additional filter — stacking behaviour without modifying
 * the original class.
 */
public class AdvancedSearchDecorator extends SearchDecorator {

    public AdvancedSearchDecorator(Search wrappedSearch) {
        super(wrappedSearch);
    }

    /**
     * Search and filter by minimum star rating.
     *
     * @param query     base search query
     * @param minRating minimum rating (inclusive), e.g. 4.0
     * @return books matching the query AND rated at or above minRating
     */
    public List<Book> searchByRating(String query, double minRating) {
        System.out.println("[AdvancedSearch] Filtering by minRating=" + minRating);
        List<Book> base = wrappedSearch.search(query);
        List<Book> filtered = new ArrayList<>();
        for (Book book : base) {
            if (book.getRating() >= minRating) {
                filtered.add(book);
            }
        }
        System.out.println("[AdvancedSearch] " + filtered.size() + " result(s) after rating filter.");
        return filtered;
    }

    /**
     * Search and filter by language.
     *
     * @param query    base search query
     * @param language target language, e.g. "English"
     * @return books matching the query AND written in the specified language
     */
    public List<Book> searchByLanguage(String query, String language) {
        System.out.println("[AdvancedSearch] Filtering by language=" + language);
        List<Book> base = wrappedSearch.search(query);
        List<Book> filtered = new ArrayList<>();
        for (Book book : base) {
            if (book.getLanguage().equalsIgnoreCase(language)) {
                filtered.add(book);
            }
        }
        System.out.println("[AdvancedSearch] " + filtered.size() + " result(s) after language filter.");
        return filtered;
    }

    /**
     * Standard decorated search — delegates to wrapped component.
     */
    @Override
    public List<Book> search(String query) {
        System.out.println("[AdvancedSearch] Delegating to wrapped search.");
        return super.search(query);
    }
}

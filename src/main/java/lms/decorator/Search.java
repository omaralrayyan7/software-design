package lms.decorator;

import lms.model.Book;
import java.util.List;

/**
 * DECORATOR PATTERN — Component Interface
 *
 * Defines the base search operation that all concrete components
 * and decorators must implement.
 */
public interface Search {

    /**
     * Search for books matching the given query.
     *
     * @param query search term
     * @return list of matching books
     */
    List<Book> search(String query);
}

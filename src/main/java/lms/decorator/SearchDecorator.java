package lms.decorator;

import lms.model.Book;
import java.util.List;

/**
 * DECORATOR PATTERN — Abstract Decorator
 *
 * Wraps a Search component and delegates the base search to it.
 * Subclasses override search() to add extra filtering behaviour
 * while still calling the wrapped component for the initial results.
 */
public abstract class SearchDecorator implements Search {

    /** The wrapped Search component (can be another decorator or concrete component). */
    protected final Search wrappedSearch;

    protected SearchDecorator(Search wrappedSearch) {
        this.wrappedSearch = wrappedSearch;
    }

    @Override
    public List<Book> search(String query) {
        // Default: simply delegate to the wrapped component
        return wrappedSearch.search(query);
    }
}

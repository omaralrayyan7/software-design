package lms.adapter;

import java.util.List;

/**
 * ADAPTER PATTERN — Target Interface
 *
 * Defines the interface the Library Management System expects.
 * External or legacy libraries are adapted to this interface.
 */
public interface LibraryInterface {

    /**
     * Search for books by title keyword.
     *
     * @param keyword the search term
     * @return list of matching book titles
     */
    List<String> searchByTitle(String keyword);

    /**
     * Search for books by author name.
     *
     * @param author the author name
     * @return list of matching book titles
     */
    List<String> searchByAuthor(String author);

    /**
     * Get the availability status of a specific book.
     *
     * @param bookTitle the book title
     * @return true if available, false otherwise
     */
    boolean checkAvailability(String bookTitle);
}

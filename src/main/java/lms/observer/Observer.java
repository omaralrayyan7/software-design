package lms.observer;

import lms.model.Book;

/**
 * OBSERVER PATTERN — Observer interface
 *
 * Any class that wants to receive book-availability notifications
 * must implement this interface.
 */
public interface Observer {
    /**
     * Called by the Subject (Book) whenever its state changes.
     *
     * @param book the book whose state changed
     */
    void update(Book book);
}

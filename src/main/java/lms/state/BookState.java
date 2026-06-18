package lms.state;

import lms.model.Book;

/**
 * STATE PATTERN — State interface
 *
 * Each concrete state encapsulates the behaviour of a Book
 * when it is in that particular state (Available, CheckedOut, etc.).
 */
public interface BookState {
    /**
     * Handle the state-specific logic and optionally transition
     * the book to the next state.
     *
     * @param book the context whose state may be changed
     */
    void handleRequest(Book book);

    /** Human-readable name of this state (used for display). */
    String getStateName();
}

package lms.state;

import lms.model.Book;

/**
 * STATE PATTERN — RemovedState
 *
 * The book has been permanently removed from the catalogue.
 * No further transitions are allowed.
 */
public class RemovedState implements BookState {

    @Override
    public void handleRequest(Book book) {
        System.out.println("[State] Book \"" + book.getTitle()
                + "\" has been REMOVED from the catalogue. No action possible.");
    }

    @Override
    public String getStateName() {
        return "REMOVED";
    }
}

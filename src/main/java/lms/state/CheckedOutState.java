package lms.state;

import lms.model.Book;

/**
 * STATE PATTERN — CheckedOutState
 *
 * The book has been borrowed by a user and is not on the shelf.
 * Transition: when the book is returned → AvailableState,
 *             and all waitlisted observers are notified.
 */
public class CheckedOutState implements BookState {

    @Override
    public void handleRequest(Book book) {
        System.out.println("[State] Book \"" + book.getTitle()
                + "\" has been RETURNED. Transitioning to AvailableState.");
        // Transition first, then notify observers so they see the new state
        book.setState(new AvailableState());
        book.notifyObservers(); // Observer Pattern triggers here
    }

    @Override
    public String getStateName() {
        return "CHECKED_OUT";
    }
}

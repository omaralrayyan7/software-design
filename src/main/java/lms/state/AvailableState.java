package lms.state;

import lms.model.Book;

/**
 * STATE PATTERN — AvailableState
 *
 * The book is on the shelf and can be borrowed.
 * Transition: when a user borrows it → CheckedOutState.
 */
public class AvailableState implements BookState {

    @Override
    public void handleRequest(Book book) {
        System.out.println("[State] Book \"" + book.getTitle()
                + "\" is AVAILABLE. Transitioning to CheckedOutState.");
        book.setState(new CheckedOutState());
    }

    @Override
    public String getStateName() {
        return "AVAILABLE";
    }
}

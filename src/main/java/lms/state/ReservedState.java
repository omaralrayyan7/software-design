package lms.state;

import lms.model.Book;

/**
 * STATE PATTERN — ReservedState
 *
 * The book has been reserved (held) for a specific user.
 * Transition: reservation honoured → CheckedOutState.
 *             reservation cancelled → AvailableState.
 */
public class ReservedState implements BookState {

    @Override
    public void handleRequest(Book book) {
        System.out.println("[State] Book \"" + book.getTitle()
                + "\" reservation fulfilled. Transitioning to CheckedOutState.");
        book.setState(new CheckedOutState());
    }

    @Override
    public String getStateName() {
        return "RESERVED";
    }
}

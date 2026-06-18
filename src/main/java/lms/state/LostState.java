package lms.state;

import lms.model.Book;

/**
 * STATE PATTERN — LostState
 *
 * The book has been reported as lost.
 * Can transition back to Available if the book is found and returned.
 */
public class LostState implements BookState {

    @Override
    public void handleRequest(Book book) {
        System.out.println("[State] Book \"" + book.getTitle()
                + "\" was LOST but has been found. Transitioning to AvailableState.");
        book.setState(new AvailableState());
    }

    @Override
    public String getStateName() {
        return "LOST";
    }
}

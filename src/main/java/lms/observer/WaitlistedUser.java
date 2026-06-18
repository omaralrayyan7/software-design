package lms.observer;

import lms.model.Book;

/**
 * OBSERVER PATTERN — Concrete Observer
 *
 * A user who has joined the waitlist for a specific book.
 * Receives a notification when that book becomes available.
 */
public class WaitlistedUser implements Observer {

    private final String name;
    private final String email;

    public WaitlistedUser(String name, String email) {
        this.name  = name;
        this.email = email;
    }

    /**
     * Called automatically by Book.notifyObservers() when the book's
     * state changes to AvailableState.
     */
    @Override
    public void update(Book book) {
        System.out.println("[Notification] Hi " + name
                + " (" + email + ")! The book \""
                + book.getTitle() + "\" is now available. Go borrow it!");
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public String getName()  { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "WaitlistedUser{name='" + name + "', email='" + email + "'}";
    }
}

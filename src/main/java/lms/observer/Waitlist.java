package lms.observer;

import lms.model.Book;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * OBSERVER PATTERN — Waitlist
 *
 * Holds a queue of observers (users) waiting for a specific book.
 * When the book becomes available, notifyUsers() broadcasts the event.
 */
public class Waitlist {

    private final int bookId;
    private final Queue<Observer> queueOfUsers = new ArrayDeque<>();

    public Waitlist(int bookId) {
        this.bookId = bookId;
    }

    /** Add a user to the back of the waitlist queue. */
    public void addUser(Observer user) {
        queueOfUsers.add(user);
        System.out.println("[Waitlist] User added to waitlist for bookId=" + bookId);
    }

    /** Notify ALL waiting users that the book is now available. */
    public void notifyUsers(Book book) {
        if (queueOfUsers.isEmpty()) {
            System.out.println("[Waitlist] No users waiting for bookId=" + bookId);
            return;
        }
        System.out.println("[Waitlist] Notifying " + queueOfUsers.size()
                + " user(s) for bookId=" + bookId);
        for (Observer user : queueOfUsers) {
            user.update(book);
        }
    }

    /** Remove and return the first user in the queue (FIFO priority). */
    public Observer pollNextUser() {
        return queueOfUsers.poll();
    }

    public int getBookId() { return bookId; }

    public int size() { return queueOfUsers.size(); }

    public boolean isEmpty() { return queueOfUsers.isEmpty(); }
}

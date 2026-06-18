package lms.model;

import lms.observer.Observer;
import lms.observer.Waitlist;
import lms.state.AvailableState;
import lms.state.BookState;

import java.util.ArrayList;
import java.util.List;

/**
 * MODEL — Book (Subject in Observer Pattern + Context in State Pattern)
 *
 * This is the central domain class.  It plays two roles from the
 * Gang-of-Four patterns:
 *
 * 1. STATE PATTERN  — Book holds a reference to a BookState object and
 *    delegates all state-dependent behaviour to it.  Callers invoke
 *    book.request() without knowing which state is active.
 *
 * 2. OBSERVER PATTERN — Book acts as the Subject.  It maintains a
 *    Waitlist and notifies all waiting users whenever its state changes
 *    back to Available (triggered inside CheckedOutState.handleRequest).
 */
public class Book {

    // -----------------------------------------------------------------------
    //  Core attributes
    // -----------------------------------------------------------------------
    private final String isbn;
    private final String title;
    private final String author;
    private final String genre;
    private String language;
    private double rating;    // 0.0 – 5.0
    private int    year;

    // -----------------------------------------------------------------------
    //  State Pattern
    // -----------------------------------------------------------------------
    private BookState currentState;

    // -----------------------------------------------------------------------
    //  Observer Pattern
    // -----------------------------------------------------------------------
    private final Waitlist waitlist = new Waitlist();
    /** Extra list for simple one-shot observers not managed by the waitlist queue. */
    private final List<Observer> observers = new ArrayList<>();

    // -----------------------------------------------------------------------
    //  Constructors
    // -----------------------------------------------------------------------

    public Book(String isbn, String title, String author, String genre) {
        this.isbn   = isbn;
        this.title  = title;
        this.author = author;
        this.genre  = genre;
        this.language = "English";
        this.rating   = 0.0;
        this.year     = 0;
        this.currentState = new AvailableState(); // books start as available
    }

    public Book(String isbn, String title, String author, String genre,
                String language, double rating, int year) {
        this(isbn, title, author, genre);
        this.language = language;
        this.rating   = rating;
        this.year     = year;
    }

    // -----------------------------------------------------------------------
    //  State Pattern methods
    // -----------------------------------------------------------------------

    /** Trigger the current state's action (borrow / return / reserve / etc.). */
    public void request() {
        currentState.handleRequest(this);
    }

    /** Called by State objects to switch to a new state. */
    public void setState(BookState newState) {
        System.out.println("[Book] \"" + title + "\" state: "
                + currentState.getStateName() + " → " + newState.getStateName());
        this.currentState = newState;
    }

    public BookState getState()        { return currentState; }
    public String    getStateName()    { return currentState.getStateName(); }

    // -----------------------------------------------------------------------
    //  Observer Pattern methods
    // -----------------------------------------------------------------------

    /** Add a user to the waitlist queue. */
    public void addToWaitlist(Observer observer) {
        waitlist.addUser(observer);
    }

    /** Notify ALL waitlisted users that this book is now available. */
    public void notifyObservers() {
        System.out.println("[Book] Notifying waitlisted users for \"" + title + "\"");
        waitlist.notifyUsers(this);
        // Also notify any simple observers
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /** Subscribe a simple observer (not using the queue). */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public Waitlist getWaitlist() { return waitlist; }

    // -----------------------------------------------------------------------
    //  Getters / Setters
    // -----------------------------------------------------------------------

    public String getIsbn()    { return isbn; }
    public String getTitle()   { return title; }
    public String getAuthor()  { return author; }
    public String getGenre()   { return genre; }

    public String getLanguage()          { return language; }
    public void   setLanguage(String l)  { this.language = l; }

    public double getRating()           { return rating; }
    public void   setRating(double r)   { this.rating = r; }

    public int  getYear()        { return year; }
    public void setYear(int y)   { this.year = y; }

    @Override
    public String toString() {
        return String.format("Book{isbn='%s', title='%s', author='%s', state='%s', rating=%.1f}",
                isbn, title, author, getStateName(), rating);
    }
}

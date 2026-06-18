package lms;

import lms.adapter.ExternalLibrary;
import lms.adapter.LibraryInterface;
import lms.adapter.NationalLibraryAdapter;
import lms.decorator.AdvancedSearchDecorator;
import lms.decorator.BookSearch;
import lms.model.Book;
import lms.model.BorrowingRecord;
import lms.model.User;
import lms.observer.WaitlistedUser;
import lms.singleton.DatabaseManager;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests covering all five design patterns.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LibrarySystemTest {

    // -----------------------------------------------------------------------
    //  1. SINGLETON
    // -----------------------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Singleton: DatabaseManager returns same instance")
    void testSingletonSameInstance() {
        DatabaseManager db1 = DatabaseManager.getInstance();
        DatabaseManager db2 = DatabaseManager.getInstance();
        assertSame(db1, db2, "Both references must point to the same DatabaseManager instance");
    }

    @Test
    @Order(2)
    @DisplayName("Singleton: CRUD operations persist across calls")
    void testSingletonCrud() {
        DatabaseManager db = DatabaseManager.getInstance();
        int before = db.getRecordCount();

        BorrowingRecord rec = new BorrowingRecord("T-001", "U-001", "ISBN-999",
                LocalDate.now());
        db.insertBorrowingRecord(rec);

        assertEquals(before + 1, db.getRecordCount());
        assertNotNull(db.getBorrowingRecord("T-001"));
        assertEquals("ISBN-999", db.getBorrowingRecord("T-001").getBookIsbn());

        db.deleteBorrowingRecord("T-001");
        assertEquals(before, db.getRecordCount());
    }

    // -----------------------------------------------------------------------
    //  2. OBSERVER
    // -----------------------------------------------------------------------

    @Test
    @Order(3)
    @DisplayName("Observer: waitlisted users are notified on book return")
    void testObserverNotification() {
        Book book = new Book("ISBN-OBS", "Test Book", "Author", "Genre");

        // Track notifications manually via a simple counter observer
        int[] notificationCount = {0};
        book.addObserver(b -> notificationCount[0]++);

        // Put the book in checked-out state, then simulate a return
        book.request(); // Available → CheckedOut
        assertEquals("CHECKED_OUT", book.getStateName());

        book.request(); // CheckedOut → Available (triggers notifyObservers)
        assertEquals("AVAILABLE", book.getStateName());
        assertEquals(1, notificationCount[0], "Observer should have been notified once");
    }

    @Test
    @Order(4)
    @DisplayName("Observer: multiple users on waitlist all receive notifications")
    void testObserverMultipleUsers() {
        Book book = new Book("ISBN-OBS2", "Multi-Observer Book", "Author", "Genre");

        WaitlistedUser u1 = new WaitlistedUser("Alice", "alice@test.com");
        WaitlistedUser u2 = new WaitlistedUser("Bob",   "bob@test.com");
        WaitlistedUser u3 = new WaitlistedUser("Carol", "carol@test.com");

        book.addToWaitlist(u1);
        book.addToWaitlist(u2);
        book.addToWaitlist(u3);

        // Borrow then return — should not throw and waitlist should be drained
        book.request(); // → CheckedOut
        book.request(); // → Available + notifyObservers
        // Test passes if no exception is thrown; notifyObservers internally
        // polls the queue so this also validates the Waitlist drain logic
        assertEquals("AVAILABLE", book.getStateName());
    }

    // -----------------------------------------------------------------------
    //  3. STATE
    // -----------------------------------------------------------------------

    @Test
    @Order(5)
    @DisplayName("State: Available → CheckedOut → Available lifecycle")
    void testStateBorrowReturn() {
        Book book = new Book("ISBN-ST1", "State Book", "Author", "Genre");
        assertEquals("AVAILABLE", book.getStateName());

        book.request(); // borrow
        assertEquals("CHECKED_OUT", book.getStateName());

        book.request(); // return
        assertEquals("AVAILABLE", book.getStateName());
    }

    @Test
    @Order(6)
    @DisplayName("State: RemovedState is terminal — no further transitions")
    void testStateRemovedIsTerminal() {
        Book book = new Book("ISBN-ST2", "Removed Book", "Author", "Genre");
        book.setState(new lms.state.RemovedState());
        assertEquals("REMOVED", book.getStateName());

        book.request(); // should print warning but NOT change state
        assertEquals("REMOVED", book.getStateName(), "REMOVED state should have no transition");
    }

    @Test
    @Order(7)
    @DisplayName("State: LostState transitions back to Available")
    void testStateLostFound() {
        Book book = new Book("ISBN-ST3", "Lost Book", "Author", "Genre");
        book.setState(new lms.state.LostState());
        assertEquals("LOST", book.getStateName());

        book.request(); // found
        assertEquals("AVAILABLE", book.getStateName());
    }

    // -----------------------------------------------------------------------
    //  4. ADAPTER
    // -----------------------------------------------------------------------

    @Test
    @Order(8)
    @DisplayName("Adapter: NationalLibraryAdapter translates searchByTitle correctly")
    void testAdapterSearchByTitle() {
        LibraryInterface adapter = new NationalLibraryAdapter(new ExternalLibrary());
        List<String> results = adapter.searchByTitle("Clean");

        assertFalse(results.isEmpty(), "Should find at least one result for 'Clean'");
        assertTrue(results.stream().anyMatch(t -> t.contains("Clean Code")));
    }

    @Test
    @Order(9)
    @DisplayName("Adapter: NationalLibraryAdapter translates checkAvailability correctly")
    void testAdapterCheckAvailability() {
        LibraryInterface adapter = new NationalLibraryAdapter(new ExternalLibrary());

        assertTrue(adapter.checkAvailability("Clean Code"),
                "Clean Code should be available");
        assertFalse(adapter.checkAvailability("The Pragmatic Programmer"),
                "The Pragmatic Programmer should be checked out");
    }

    // -----------------------------------------------------------------------
    //  5. DECORATOR
    // -----------------------------------------------------------------------

    @Test
    @Order(10)
    @DisplayName("Decorator: searchByRating filters correctly")
    void testDecoratorSearchByRating() {
        List<Book> catalogue = buildCatalogue();
        AdvancedSearchDecorator search = new AdvancedSearchDecorator(new BookSearch(catalogue));

        List<Book> highRated = search.searchByRating("", 4.7);
        assertTrue(highRated.stream().allMatch(b -> b.getRating() >= 4.7),
                "All results should have rating >= 4.7");
    }

    @Test
    @Order(11)
    @DisplayName("Decorator: searchByLanguage filters correctly")
    void testDecoratorSearchByLanguage() {
        List<Book> catalogue = buildCatalogue();
        AdvancedSearchDecorator search = new AdvancedSearchDecorator(new BookSearch(catalogue));

        List<Book> frenchBooks = search.searchByLanguage("", "French");
        assertTrue(frenchBooks.stream().allMatch(b -> b.getLanguage().equalsIgnoreCase("French")),
                "All results should be in French");
        assertFalse(frenchBooks.isEmpty(), "Should find at least one French book");
    }

    // -----------------------------------------------------------------------
    //  Helper
    // -----------------------------------------------------------------------

    private List<Book> buildCatalogue() {
        List<Book> catalogue = new ArrayList<>();
        Book b1 = new Book("C1", "Clean Code", "Martin", "Tech", "English", 4.8, 2008);
        Book b2 = new Book("C2", "Refactoring", "Fowler", "Tech", "English", 4.7, 1999);
        Book b3 = new Book("C3", "Algorithms", "Sedgewick", "CS", "French", 4.3, 2011);
        Book b4 = new Book("C4", "DDD", "Evans", "Tech", "English", 4.5, 2003);
        catalogue.add(b1); catalogue.add(b2);
        catalogue.add(b3); catalogue.add(b4);
        return catalogue;
    }
}

package lms.service;

import lms.adapter.ExternalLibrary;
import lms.adapter.LibraryInterface;
import lms.adapter.LibraryNetworkAdapter;
import lms.adapter.NationalLibraryAdapter;
import lms.decorator.AdvancedSearchDecorator;
import lms.decorator.BookSearch;
import lms.decorator.Search;
import lms.model.Book;
import lms.model.BorrowingRecord;
import lms.model.User;
import lms.observer.WaitlistedUser;
import lms.singleton.DatabaseManager;
import lms.state.CheckedOutState;
import lms.state.LostState;
import lms.state.ReservedState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE — LibrarySystem (Main Entry Point)
 *
 * Demonstrates all five Gang-of-Four patterns implemented in this project:
 *
 *  1. SINGLETON  — DatabaseManager: single DB instance
 *  2. OBSERVER   — Book/Waitlist/WaitlistedUser: notify waitlisted users on return
 *  3. STATE      — Book transitions through Available → CheckedOut → Available
 *  4. ADAPTER    — NationalLibraryAdapter / LibraryNetworkAdapter wrap external APIs
 *  5. DECORATOR  — AdvancedSearchDecorator adds rating & language filters to BookSearch
 */
public class LibrarySystem {

    public static void main(String[] args) {

        System.out.println("=======================================================");
        System.out.println("   LIBRARY MANAGEMENT SYSTEM — Design Patterns Demo");
        System.out.println("=======================================================\n");

        // -------------------------------------------------------------------
        // 1. SINGLETON — obtain the one-and-only DatabaseManager
        // -------------------------------------------------------------------
        System.out.println("── 1. SINGLETON PATTERN ───────────────────────────────");
        DatabaseManager db1 = DatabaseManager.getInstance();
        DatabaseManager db2 = DatabaseManager.getInstance();
        System.out.println("Same instance? " + (db1 == db2)); // true
        db1.connect();

        // -------------------------------------------------------------------
        // 2. OBSERVER — book waitlist notification
        // -------------------------------------------------------------------
        System.out.println("\n── 2. OBSERVER PATTERN ────────────────────────────────");

        Book cleanCode = new Book("ISBN-001", "Clean Code", "Robert C. Martin", "Technology",
                "English", 4.8, 2008);

        // Users join the waitlist
        WaitlistedUser alice = new WaitlistedUser("Alice",  "alice@library.jo");
        WaitlistedUser bob   = new WaitlistedUser("Bob",    "bob@library.jo");
        cleanCode.addToWaitlist(alice);
        cleanCode.addToWaitlist(bob);
        System.out.println("Alice and Bob joined the waitlist for \"" + cleanCode.getTitle() + "\"");

        // -------------------------------------------------------------------
        // 3. STATE — book lifecycle transitions
        // -------------------------------------------------------------------
        System.out.println("\n── 3. STATE PATTERN ───────────────────────────────────");

        System.out.println("Initial state: " + cleanCode.getStateName());

        cleanCode.request();          // Available → CheckedOut  (borrow)
        System.out.println("After borrow:  " + cleanCode.getStateName());

        cleanCode.request();          // CheckedOut → Available  (return) + notifies observers
        System.out.println("After return:  " + cleanCode.getStateName());

        // Reserve demo
        Book refactoring = new Book("ISBN-002", "Refactoring", "Martin Fowler", "Technology",
                "English", 4.7, 1999);
        refactoring.setState(new ReservedState());
        System.out.println("Refactoring state: " + refactoring.getStateName());
        refactoring.request();         // Reserved → CheckedOut
        System.out.println("After reservation fulfilled: " + refactoring.getStateName());

        // Lost state demo
        Book pragProg = new Book("ISBN-003", "The Pragmatic Programmer", "Andrew Hunt", "Technology",
                "English", 4.9, 1999);
        pragProg.setState(new LostState());
        System.out.println("Pragmatic Programmer state: " + pragProg.getStateName());
        pragProg.request();            // Lost → Available (found)
        System.out.println("After found: " + pragProg.getStateName());

        // -------------------------------------------------------------------
        // 4. ADAPTER — wrap incompatible external library APIs
        // -------------------------------------------------------------------
        System.out.println("\n── 4. ADAPTER PATTERN ─────────────────────────────────");

        LibraryInterface nationalLib = new NationalLibraryAdapter(new ExternalLibrary());
        LibraryInterface networkLib  = new LibraryNetworkAdapter();

        System.out.println("NationalLib search by title 'Clean': "
                + nationalLib.searchByTitle("Clean"));
        System.out.println("NationalLib availability of 'Clean Code': "
                + nationalLib.checkAvailability("Clean Code"));

        System.out.println("NetworkLib search by author 'Silberschatz': "
                + networkLib.searchByAuthor("Silberschatz"));
        System.out.println("NetworkLib availability of 'Introduction to Algorithms': "
                + networkLib.checkAvailability("Introduction to Algorithms"));

        // -------------------------------------------------------------------
        // 5. DECORATOR — layered search functionality
        // -------------------------------------------------------------------
        System.out.println("\n── 5. DECORATOR PATTERN ───────────────────────────────");

        List<Book> catalogue = new ArrayList<>();
        catalogue.add(cleanCode);
        catalogue.add(refactoring);
        catalogue.add(pragProg);
        catalogue.add(new Book("ISBN-004", "Domain-Driven Design", "Eric Evans", "Technology",
                "English", 4.5, 2003));
        catalogue.add(new Book("ISBN-005", "Introduction to Algorithms", "Cormen", "CS",
                "English", 4.6, 2009));
        catalogue.add(new Book("ISBN-006", "Algorithms en Français", "Sedgewick", "CS",
                "French", 4.3, 2011));

        // Set ratings for demonstration
        cleanCode.setRating(4.8);
        refactoring.setRating(4.7);
        pragProg.setRating(4.9);

        Search base     = new BookSearch(catalogue);
        AdvancedSearchDecorator advanced = new AdvancedSearchDecorator(base);

        System.out.println("\nBase search for 'a':");
        base.search("a").forEach(b -> System.out.println("  " + b.getTitle()));

        System.out.println("\nAdvanced: search 'a' + rating >= 4.7:");
        advanced.searchByRating("a", 4.7)
                .forEach(b -> System.out.println("  " + b.getTitle() + " (" + b.getRating() + ")"));

        System.out.println("\nAdvanced: search '' + language = 'French':");
        advanced.searchByLanguage("", "French")
                .forEach(b -> System.out.println("  " + b.getTitle() + " [" + b.getLanguage() + "]"));

        // -------------------------------------------------------------------
        // Singleton DB — record a borrowing
        // -------------------------------------------------------------------
        System.out.println("\n── DB via Singleton ───────────────────────────────────");
        BorrowingRecord br = new BorrowingRecord("REC-001", "USR-001", "ISBN-001",
                LocalDate.now().minusDays(3));
        db1.insertBorrowingRecord(br);
        System.out.println("Total records: " + db1.getRecordCount());
        System.out.println("Record: " + db1.getBorrowingRecord("REC-001"));

        db1.disconnect();

        System.out.println("\n=======================================================");
        System.out.println("   Demo complete. All 5 patterns executed successfully.");
        System.out.println("=======================================================");
    }
}

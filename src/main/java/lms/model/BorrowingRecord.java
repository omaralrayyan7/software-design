package lms.model;

import java.time.LocalDate;

/**
 * MODEL — BorrowingRecord
 *
 * Tracks a single borrowing transaction.
 * Used by DatabaseManager (Singleton) to persist / retrieve records.
 */
public class BorrowingRecord {

    private final String recordId;
    private final String userId;
    private final String bookIsbn;
    private final LocalDate borrowDate;
    private LocalDate returnDate;    // null until returned
    private boolean isReturned;

    public BorrowingRecord(String recordId, String userId, String bookIsbn, LocalDate borrowDate) {
        this.recordId   = recordId;
        this.userId     = userId;
        this.bookIsbn   = bookIsbn;
        this.borrowDate = borrowDate;
        this.returnDate = null;
        this.isReturned = false;
    }

    // -----------------------------------------------------------------------
    //  Business logic
    // -----------------------------------------------------------------------

    /** Mark the book as returned on the given date. */
    public void markReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.isReturned = true;
    }

    /** Calculate how many days the book was / has been borrowed. */
    public long daysElapsed() {
        LocalDate end = isReturned ? returnDate : LocalDate.now();
        return java.time.temporal.ChronoUnit.DAYS.between(borrowDate, end);
    }

    /** Returns true if the book is overdue (> 14 days and not returned). */
    public boolean isOverdue() {
        return !isReturned && daysElapsed() > 14;
    }

    // -----------------------------------------------------------------------
    //  Getters
    // -----------------------------------------------------------------------

    public String    getRecordId()   { return recordId; }
    public String    getUserId()     { return userId; }
    public String    getBookIsbn()   { return bookIsbn; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public boolean   isReturned()    { return isReturned; }

    @Override
    public String toString() {
        return String.format(
                "BorrowingRecord{id='%s', user='%s', book='%s', borrowed=%s, returned=%s, overdue=%b}",
                recordId, userId, bookIsbn, borrowDate,
                isReturned ? returnDate.toString() : "N/A", isOverdue());
    }
}

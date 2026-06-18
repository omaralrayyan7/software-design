package lms.singleton;

import lms.model.BorrowingRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SINGLETON PATTERN — DatabaseManager
 *
 * Guarantees a single instance manages all database interactions.
 * Thread-safe via synchronized getInstance().
 *
 * Responsibilities:
 *  - CRUD operations for BorrowingRecord
 *  - Managing the database connection lifecycle
 */
public class DatabaseManager {

    // ── Singleton instance ────────────────────────────────────────────────
    private static DatabaseManager instance;
    private final String connectionString;

    // In-memory store keyed by recordId (replace with real DB in production)
    private final Map<String, BorrowingRecord> records = new HashMap<>();

    /** Private constructor — prevents direct instantiation. */
    private DatabaseManager() {
        this.connectionString = "jdbc:h2:mem:lmsdb";
        System.out.println("[DatabaseManager] Instance created. Connection: " + connectionString);
    }

    /**
     * Returns the single instance; creates it on first call.
     * Synchronized to be thread-safe.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // ── Connection lifecycle ──────────────────────────────────────────────

    public void connect() {
        System.out.println("[DatabaseManager] Connected to database.");
    }

    public void disconnect() {
        System.out.println("[DatabaseManager] Disconnected from database.");
    }

    // ── CRUD — BorrowingRecord ────────────────────────────────────────────

    /** Persist a new borrowing record. */
    public void insertBorrowingRecord(BorrowingRecord record) {
        if (records.containsKey(record.getRecordId())) {
            throw new IllegalArgumentException("Record already exists: " + record.getRecordId());
        }
        records.put(record.getRecordId(), record);
        System.out.println("[DB] Inserted record: " + record);
    }

    /** Update an existing record (matched by recordId). */
    public void updateBorrowingRecord(BorrowingRecord updated) {
        if (!records.containsKey(updated.getRecordId())) {
            throw new IllegalArgumentException("Record not found: " + updated.getRecordId());
        }
        records.put(updated.getRecordId(), updated);
        System.out.println("[DB] Updated record: " + updated);
    }

    /** Remove a borrowing record by recordId. */
    public void deleteBorrowingRecord(String recordId) {
        BorrowingRecord removed = records.remove(recordId);
        System.out.println("[DB] " + (removed != null ? "Deleted" : "Not found") + " record: " + recordId);
    }

    /** Retrieve a specific record by recordId. */
    public BorrowingRecord getBorrowingRecord(String recordId) {
        return records.get(recordId);
    }

    /** Retrieve all borrowing records for a given bookIsbn. */
    public List<BorrowingRecord> getRecordsByBook(String bookIsbn) {
        List<BorrowingRecord> result = new ArrayList<>();
        for (BorrowingRecord r : records.values()) {
            if (r.getBookIsbn().equals(bookIsbn)) result.add(r);
        }
    
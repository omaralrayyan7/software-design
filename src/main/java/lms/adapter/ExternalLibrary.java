package lms.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ADAPTER PATTERN — Adaptee
 *
 * Simulates a legacy / third-party external library system
 * with an incompatible API that we cannot change.
 */
public class ExternalLibrary {

    /** Internal catalogue: title → author */
    private final Map<String, String> catalogue = new HashMap<>();
    /** Availability map: title → available */
    private final Map<String, Boolean> availability = new HashMap<>();

    public ExternalLibrary() {
        catalogue.put("Clean Code", "Robert C. Martin");
        catalogue.put("The Pragmatic Programmer", "Andrew Hunt");
        catalogue.put("Design Patterns", "Gang of Four");
        catalogue.put("Refactoring", "Martin Fowler");
        catalogue.put("Domain-Driven Design", "Eric Evans");

        availability.put("Clean Code", true);
        availability.put("The Pragmatic Programmer", false);
        availability.put("Design Patterns", true);
        availability.put("Refactoring", false);
        availability.put("Domain-Driven Design", true);
    }

    // -----------------------------------------------------------------------
    //  Incompatible API — method names and signatures differ from LibraryInterface
    // -----------------------------------------------------------------------

    /** Find books whose title contains the given fragment (case-insensitive). */
    public List<String> findBooksByTitle(String titleFragment) {
        List<String> results = new ArrayList<>();
        for (String title : catalogue.keySet()) {
            if (title.toLowerCase().contains(titleFragment.toLowerCase())) {
                results.add(title);
            }
        }
        return results;
    }

    /** Find books written by the given author (case-insensitive match). */
    public List<String> findBooksByAuthor(String authorName) {
        List<String> results = new ArrayList<>();
        for (Map.Entry<String, String> entry : catalogue.entrySet()) {
            if (entry.getValue().toLowerCase().contains(authorName.toLowerCase())) {
                results.add(entry.getKey());
            }
        }
        return results;
    }

    /** Returns "AVAILABLE" or "CHECKED_OUT" string instead of boolean. */
    public String getBookStatus(String title) {
        Boolean avail = availability.get(title);
        if (avail == null) return "NOT_FOUND";
        return avail ? "AVAILABLE" : "CHECKED_OUT";
    }
}

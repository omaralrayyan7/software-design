package lms.adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ADAPTER PATTERN — Second Concrete Adapter
 *
 * Adapts a hypothetical remote / network-based library catalogue
 * (simulated here) to the LibraryInterface.
 *
 * Demonstrates that multiple adapters can wrap different adaptees
 * while exposing the same target interface to the system.
 */
public class LibraryNetworkAdapter implements LibraryInterface {

    // Simulated remote catalogue (normally this would be an HTTP client, etc.)
    private final java.util.Map<String, String> remoteCatalogue = new java.util.HashMap<>();
    private final java.util.Map<String, Boolean> remoteAvailability = new java.util.HashMap<>();

    public LibraryNetworkAdapter() {
        remoteCatalogue.put("Introduction to Algorithms", "Cormen");
        remoteCatalogue.put("Computer Networks", "Tanenbaum");
        remoteCatalogue.put("Operating System Concepts", "Silberschatz");
        remoteCatalogue.put("Database System Concepts", "Silberschatz");

        remoteAvailability.put("Introduction to Algorithms", true);
        remoteAvailability.put("Computer Networks", true);
        remoteAvailability.put("Operating System Concepts", false);
        remoteAvailability.put("Database System Concepts", true);
    }

    @Override
    public List<String> searchByTitle(String keyword) {
        System.out.println("[Adapter] NetworkLibrary: remote title search for \"" + keyword + "\"");
        List<String> results = new ArrayList<>();
        for (String title : remoteCatalogue.keySet()) {
            if (title.toLowerCase().contains(keyword.toLowerCase())) {
                results.add("[Network] " + title);
            }
        }
        return results;
    }

    @Override
    public List<String> searchByAuthor(String author) {
        System.out.println("[Adapter] NetworkLibrary: remote author search for \"" + author + "\"");
        List<String> results = new ArrayList<>();
        for (java.util.Map.Entry<String, String> entry : remoteCatalogue.entrySet()) {
            if (entry.getValue().toLowerCase().contains(author.toLowerCase())) {
                results.add("[Network] " + entry.getKey());
            }
        }
        return results;
    }

    @Override
    public boolean checkAvailability(String bookTitle) {
        System.out.println("[Adapter] NetworkLibrary: remote availability check for \"" + bookTitle + "\"");
        // Strip the "[Network] " prefix if present
        String normalizedTitle = bookTitle.replace("[Network] ", "");
        Boolean avail = remoteAvailability.get(normalizedTitle);
        return avail != null && avail;
    }
}

package lms.adapter;

import java.util.List;

/**
 * ADAPTER PATTERN — Concrete Adapter (Object Adapter)
 *
 * Adapts ExternalLibrary (incompatible API) to the LibraryInterface
 * expected by the Library Management System.
 *
 * This is an Object Adapter: it holds a reference to the adaptee
 * rather than inheriting from it.
 */
public class NationalLibraryAdapter implements LibraryInterface {

    private final ExternalLibrary externalLibrary;

    public NationalLibraryAdapter(ExternalLibrary externalLibrary) {
        this.externalLibrary = externalLibrary;
    }

    @Override
    public List<String> searchByTitle(String keyword) {
        // Translate: searchByTitle → findBooksByTitle
        System.out.println("[Adapter] NationalLibrary: translating searchByTitle → findBooksByTitle");
        return externalLibrary.findBooksByTitle(keyword);
    }

    @Override
    public List<String> searchByAuthor(String author) {
        // Translate: searchByAuthor → findBooksByAuthor
        System.out.println("[Adapter] NationalLibrary: translating searchByAuthor → findBooksByAuthor");
        return externalLibrary.findBooksByAuthor(author);
    }

    @Override
    public boolean checkAvailability(String bookTitle) {
        // Translate: boolean checkAvailability → String getBookStatus
        System.out.println("[Adapter] NationalLibrary: translating checkAvailability → getBookStatus");
        String status = externalLibrary.getBookStatus(bookTitle);
        return "AVAILABLE".equals(status);
    }
}

package lms.decorator;

import lms.model.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * DECORATOR PATTERN — Concrete Component
 *
 * The base, undecorated search implementation.
 * Searches books by title or author (case-insensitive substring match).
 */
public class BookSearch implements Search {

    private final List<Book> bookCatalogue;

    public BookSearch(List<Book> bookCatalogue) {
        this.bookCatalogue = bookCatalogue;
    }

    @Override
    public List<Book> search(String query) {
        System.out.println("[Search] Basic search for: \"" + query + "\"");
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Book book : bookCatalogue) {
            if (book.getTitle().toLowerCase().contains(lowerQuery)
                    || book.getAuthor().toLowerCase().contains(lowerQuery)) {
                results.add(book);
            }
        }

        System.out.println("[Search] Found " + results.size() + " result(s).");
        return results;
    }
}

# Library Management System — Design Patterns

[![Java](https://img.shields.io/badge/Java-17-007396?logo=java)](https://www.java.com/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/Tests-JUnit_5-25A162?logo=junit5)](https://junit.org/junit5/)

A Java application demonstrating **five Gang of Four (GoF) design patterns** applied to a realistic Library Management System. Designed as a university software design project.

**Team:** Omar Alrayyan · Mousa Estefan · Ayham Alahmad

## The Scenario

> A user searches for a book. It's checked out — they join the waitlist. When it's returned:
> - **Observer** notifies all waitlisted users automatically
> - **State** transitions the book from `CHECKED_OUT` → `AVAILABLE`
> - **Adapter** queries the national library network seamlessly
> - **Decorator** adds rating/language filters to search dynamically
> - **Singleton** ensures one `DatabaseManager` handles all DB transactions

## Design Patterns

| Pattern | Classes | Purpose |
|---|---|---|
| **Singleton** | `DatabaseManager` | One DB connection instance across the app |
| **Observer** | `Book`, `Observer`, `WaitlistedUser` | Auto-notify waitlist when book is returned |
| **State** | `BookState`, `AvailableState`, `CheckedOutState`, `ReservedState` … | Encapsulate book lifecycle transitions |
| **Adapter** | `NationalLibraryAdapter`, `LibraryNetworkAdapter` | Integrate external library APIs without changing core logic |
| **Decorator** | `BookSearch`, `AdvancedSearchDecorator` | Add rating/language filters without modifying base search |

## Project Structure

```
src/
├── main/java/lms/
│   ├── singleton/DatabaseManager.java
│   ├── observer/{Observer, WaitlistedUser, Waitlist}.java
│   ├── state/{BookState, AvailableState, CheckedOutState, ReservedState, …}.java
│   ├── adapter/{LibraryInterface, ExternalLibrary, NationalLibraryAdapter, LibraryNetworkAdapter}.java
│   ├── decorator/{Search, BookSearch, SearchDecorator, AdvancedSearchDecorator}.java
│   ├── model/{Book, User, BorrowingRecord}.java
│   └── service/LibrarySystem.java     ← main entry point (Facade)
└── test/java/lms/LibrarySystemTest.java
```

## Key Code Segments

```java
// Singleton
public static synchronized DatabaseManager getInstance() {
    if (instance == null) instance = new DatabaseManager();
    return instance;
}

// Observer — book notifies waitlist on return
public void handleRequest(Book book) {
    book.setState(new AvailableState());
    book.notifyObservers(); // triggers all WaitlistedUser.update()
}

// Decorator — add filters without touching BookSearch
AdvancedSearchDecorator adv = new AdvancedSearchDecorator(new BookSearch());
adv.searchByRating("Java", 4.5);
adv.searchByLanguage("Java", "Arabic");

// Adapter — unified interface over incompatible external API
public List<Book> searchBook(String title) {
    return externalAPI.findBook(title); // adapts ExternalLibrary → LibraryInterface
}
```

## How to Run

```bash
git clone https://github.com/omaralrayyan7/software-design.git
cd software-design
mvn compile
mvn exec:java -Dexec.mainClass="lms.service.LibrarySystem"
mvn test
```

**Prerequisites:** Java 17+, Maven 3.8+

## Class Diagram

See [`docs/class-diagram.png`](docs/class-diagram.png) for the full UML diagram.

## License

[MIT](LICENSE)

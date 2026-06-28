# What is the Library Management System (LMS)?

## Overview

The **Library Management System (LMS)** is a Java-based web application designed as a university software design project. It manages books, users, borrowing records, and external library integrations through five classical **Gang of Four (GoF) design patterns**. The project demonstrates how design patterns solve real-world problems: state management, notifications, external API integration, dynamic search enhancement, and centralized database access.

**Team:** Omar Alrayyan · Mousa Estefan · Ayham Alahmad

---

## Scenario

> A user searches for a book. If it's checked out, they join a waitlist. When the book is returned:
> - **Observer Pattern** → all waitlisted users get automatic notifications
> - **State Pattern** → book status transitions from `CHECKED_OUT` → `AVAILABLE`
> - **Adapter Pattern** → seamless integration with the national library network
> - **Decorator Pattern** → advanced search (filter by rating, language) added without touching base code
> - **Singleton Pattern** → single `DatabaseManager` instance handles all DB transactions

---

## Design Patterns Used

| Pattern | Classes | Purpose |
|---|---|---|
| **Singleton** | `DatabaseManager` | One DB connection instance across the entire app |
| **Observer** | `Book`, `Observer`, `WaitlistedUser`, `User` | Auto-notify waitlisted users when a book is returned |
| **State** | `BookState`, `AvailableState`, `CheckedOutState`, `ReservedState`, `RemovedState`, `LostState` | Encapsulate book lifecycle transitions |
| **Adapter** | `LibraryInterface`, `NationalLibraryAdapter`, `LibraryNetworkAdapter` | Integrate external library APIs without changing core logic |
| **Decorator** | `Search`, `BookSearch`, `SearchDecorator`, `AdvancedSearchDecorator` | Add rating/language filters to search dynamically |

---

## Project Structure

```
src/
├── main/java/lms/
│   ├── singleton/
│   │   └── DatabaseManager.java        # Singleton — thread-safe DB connection
│   ├── observer/
│   │   ├── Observer.java               # Observer interface
│   │   ├── WaitlistedUser.java         # Concrete observer
│   │   └── Waitlist.java               # Holds queue of observers per book
│   ├── state/
│   │   ├── BookState.java              # State interface
│   │   ├── AvailableState.java
│   │   ├── CheckedOutState.java
│   │   ├── ReservedState.java
│   │   ├── RemovedState.java
│   │   └── LostState.java
│   ├── adapter/
│   │   ├── LibraryInterface.java       # Target interface
│   │   ├── ExternalLibrary.java        # Adaptee (simulated external API)
│   │   ├── NationalLibraryAdapter.java # Adapter 1
│   │   └── LibraryNetworkAdapter.java  # Adapter 2
│   ├── decorator/
│   │   ├── Search.java                 # Component interface
│   │   ├── BookSearch.java             # Concrete component
│   │   ├── SearchDecorator.java        # Abstract decorator
│   │   └── AdvancedSearchDecorator.java# Concrete decorator
│   ├── model/
│   │   ├── Book.java                   # Subject (Observer) + State holder
│   │   ├── User.java                   # Observer + borrower
│   │   └── BorrowingRecord.java        # Borrowing transaction
│   └── service/
│       └── LibrarySystem.java          # Facade — main entry point
└── test/java/lms/
    └── LibrarySystemTest.java          # JUnit 5 tests for all patterns
```

---

## Key Code Segments

### Singleton — DatabaseManager
```java
public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseManager() {}
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }
}
```

### Observer — Book notifies waitlist
```java
public void notifyObservers() {
    for (Observer observer : observers) observer.update(this);
}
```

### State — Book transitions
```java
// CheckedOutState → AvailableState on return
public void handleRequest(Book book) {
    System.out.println("Book returned. Transitioning to AvailableState.");
    book.setState(new AvailableState());
    book.notifyObservers(); // triggers Observer pattern
}
```

### Adapter — external library
```java
public class NationalLibraryAdapter implements LibraryInterface {
    private ExternalLibrary externalAPI;
    public List<Book> searchBook(String title) {
        return externalAPI.findBook(title); // adapts incompatible API
    }
}
```

### Decorator — advanced search
```java
AdvancedSearchDecorator advSearch = new AdvancedSearchDecorator(new BookSearch());
advSearch.searchByRating("Java", 4.5);
advSearch.searchByLanguage("Java", "Arabic");
```

---

## How to Run

**Prerequisites:** Java 17+, Maven

```bash
# Clone and build
git clone https://github.com/omaralrayyan7/software-design.git
cd software-design
mvn compile

# Run the demo
mvn exec:java -Dexec.mainClass="lms.service.LibrarySystem"

# Run tests
mvn test
```

---

## System Benefits

- **Modularity** — each pattern encapsulates one responsibility
- **Scalability** — add observers or adapters without touching core code
- **Testability** — decoupled dependencies enable isolated unit tests
- **Flexibility** — new book states (e.g., "Damaged") added with one class
- **Reusability** — interfaces and decorators shared across modules

---

## Class Diagram

See [`docs/class-diagram.png`](docs/class-diagram.png) for the full UML class diagram.

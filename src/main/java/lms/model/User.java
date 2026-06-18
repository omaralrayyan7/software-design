package lms.model;

import lms.observer.Observer;

/**
 * MODEL — User
 *
 * Represents a library member.  Also implements Observer so that
 * a User can be placed on a book's waitlist and receive notifications
 * when the book becomes available.
 */
public class User implements Observer {

    private final String userId;
    private final String name;
    private final String email;
    private String membershipType; // "STANDARD" | "PREMIUM"

    public User(String userId, String name, String email) {
        this.userId         = userId;
        this.name           = name;
        this.email          = email;
        this.membershipType = "STANDARD";
    }

    public User(String userId, String name, String email, String membershipType) {
        this(userId, name, email);
        this.membershipType = membershipType;
    }

    // -----------------------------------------------------------------------
    //  Observer implementation
    // -----------------------------------------------------------------------

    @Override
    public void update(Book book) {
        System.out.println("[Notification] Dear " + name + " (" + email + "), "
                + "the book \"" + book.getTitle() + "\" is now " + book.getStateName() + "!");
    }

    // -----------------------------------------------------------------------
    //  Getters / Setters
    // -----------------------------------------------------------------------

    public String getUserId()       { return userId; }
    public String getName()         { return name; }
    public String getEmail()        { return email; }

    public String getMembershipType()              { return membershipType; }
    public void   setMembershipType(String type)   { this.membershipType = type; }

    @Override
    public String toString() {
        return String.format("User{id='%s', name='%s', email='%s', membership='%s'}",
                userId, name, email, membershipType);
    }
}

package info.batey.killrauction.domain;

import java.util.Set;

public class AuctionUser {
    private String userName;
    private String md5Password;
    private Long salt;
    private String firstName;
    private String lastName;
    private Set<String> emails;

    public AuctionUser(String userName, String md5Password, Long salt, String firstName, String lastName, Set<String> emails) {
        this.userName = userName;
        this.md5Password = md5Password;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emails = emails;
    }

    public String getUserName() {
        return userName;
    }

    public String getMd5Password() {
        return md5Password;
    }

    public Long getSalt() {
        return salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<String> getEmails() {
        return emails;
    }
}

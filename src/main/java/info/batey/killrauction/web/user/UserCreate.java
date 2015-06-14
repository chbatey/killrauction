package info.batey.killrauction.web.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class UserCreate {
    @JsonProperty
    private String userName;
    @JsonProperty
    private String password;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private Set<String> emails;
    @JsonProperty
    private Long salt;

    @JsonCreator
    public UserCreate(@JsonProperty("username") String userName,
                      @JsonProperty("salt") Long salt,
                      @JsonProperty("password") String password,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("emails") Set<String> emails) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emails = emails;
        this.salt = salt;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
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

    public Long getSalt() { return salt; }

    @Override
    public String toString() {
        return "UserCreate{" +
                "userName='" + userName + '\'' +
                "salt='" + salt + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emails=" + emails +
                '}';
    }
}

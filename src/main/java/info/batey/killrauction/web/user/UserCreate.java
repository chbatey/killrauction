package info.batey.killrauction.web.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
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

    @JsonCreator
    public UserCreate(@JsonProperty("username") String userName,
                      @JsonProperty("password") String password,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("email") Set<String> emails) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emails = emails;
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

    @Override
    public String toString() {
        return "UserCreate{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emails=" + emails +
                '}';
    }
}

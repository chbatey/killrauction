package info.batey.killrauction.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserWithSalt extends User {
    private Long salt;

    public UserWithSalt(String username, Long salt, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.salt = salt;
    }

    public Long getSalt() {
        return salt;
    }
}

package info.batey.killrauction.infrastruture;

import com.google.common.collect.Sets;
import info.batey.killrauction.domain.AuctionUser;
import info.batey.killrauction.web.security.UserWithSalt;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class CassandraUserDetailsService implements UserDetailsService {

    private AuctionUserDao auctionUserDao;

    @Inject
    public CassandraUserDetailsService(AuctionUserDao auctionUserDao) {
        this.auctionUserDao = auctionUserDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuctionUser user = auctionUserDao.retrieveUser(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserWithSalt(user.getUserName(), user.getSalt(), user.getMd5Password(), Sets.newHashSet(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

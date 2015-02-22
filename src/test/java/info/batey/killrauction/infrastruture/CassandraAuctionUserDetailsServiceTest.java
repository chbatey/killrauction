package info.batey.killrauction.infrastruture;

import com.google.common.collect.Sets;
import info.batey.killrauction.domain.AuctionUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class CassandraAuctionUserDetailsServiceTest {

    private CassandraUserDetailsService underTest;

    @Mock
    private AuctionUserDao auctionUserDao;

    @Before
    public void setUp() throws Exception {
        underTest = new CassandraUserDetailsService(auctionUserDao);
    }

    @Test
    public void getsUserFromDataDao() throws Exception {
        //given
        AuctionUser auctionUser = new AuctionUser("chris", "password", "christopher", "batey", Collections.emptySet());
        given(auctionUserDao.retrieveUser("chris")).willReturn(Optional.of(auctionUser));
        //when
        UserDetails chris = underTest.loadUserByUsername("chris");
        //then
        assertEquals(Sets.newHashSet(new SimpleGrantedAuthority("ROLE_USER")), chris.getAuthorities());
        assertEquals("chris", chris.getUsername());
        assertEquals("password", chris.getPassword());
        assertTrue(chris.isAccountNonExpired());
        assertTrue(chris.isAccountNonLocked());
        assertTrue(chris.isCredentialsNonExpired());
        assertTrue(chris.isEnabled());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void userDoesNotExist() throws Exception {
        given(auctionUserDao.retrieveUser(anyString())).willReturn(Optional.empty());
        underTest.loadUserByUsername("no_exist");
    }
}
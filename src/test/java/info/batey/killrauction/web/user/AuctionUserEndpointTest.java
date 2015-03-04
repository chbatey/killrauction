package info.batey.killrauction.web.user;

import info.batey.killrauction.infrastruture.AuctionUserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuctionUserEndpointTest {

    private AuctionUserEndpoint underTest;

    @Mock
    private AuctionUserDao auctionUserDao;

    @Before
    public void setUp() throws Exception {
        underTest = new AuctionUserEndpoint(auctionUserDao);
    }

    @Test
    public void shouldSendUserToDao() throws Exception {
        given(auctionUserDao.createUser(any(UserCreate.class))).willReturn(true);
        UserCreate userCreate = new UserCreate("username", "password", "firstName", "lastName", Collections.emptySet());
        underTest.createUser(userCreate);
        verify(auctionUserDao).createUser(userCreate);
    }

    @Test(expected = AuctionUserEndpoint.UserExistsException.class)
    public void userAlreadyExists() throws Exception {
        given(auctionUserDao.createUser(any(UserCreate.class))).willReturn(false);
        UserCreate userCreate = new UserCreate("username", "password", "firstName", "lastName", Collections.emptySet());
        underTest.createUser(userCreate);
    }
}
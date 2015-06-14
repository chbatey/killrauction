package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.collect.Sets;
import info.batey.killrauction.domain.AuctionUser;
import info.batey.killrauction.web.user.UserCreate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class AuctionUserDaoTest {

    private static Cluster cluster;
    private static Session session;

    private AuctionUserDao underTest;

    @Mock
    private Md5PasswordEncoder md5PasswordEncoder;

    @Mock
    private SecureRandom secure;

    @BeforeClass
    public static void schemaSetup() throws Exception {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS killrauction_tests WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
        session.execute("use killrauction_tests");
        session.execute("CREATE TABLE IF NOT EXISTS users ( user_name text PRIMARY KEY, password text, salt bigint, first_name text , last_name text , emails set<text> )");
    }

    @AfterClass
    public static void shutdown() throws Exception {
        session.execute("drop keyspace killrauction_tests");
        session.close();
        cluster.close();
    }

    @Before
    public void setUp() throws Exception {
        session.execute("truncate users");
        underTest = new AuctionUserDao(session, md5PasswordEncoder, secure);
        underTest.prepareStatements();
    }

    @Test
    public void createsUser() throws Exception {
        //given
        Random rand = new Random();
        Long salt = rand.nextLong();

        UserCreate userCreate = new UserCreate("chbatey", salt, "bananas", "chris", "batey", Sets.newHashSet("christopher.batey@gmail.com"));
        given(md5PasswordEncoder.encodePassword(anyString(), anyString())).willReturn("saltedPassword");
        given(secure.nextLong()).willReturn( salt );

        //when
        boolean userCreated = underTest.createUser(userCreate);

        //then
        assertTrue(userCreated);
        List<AuctionUser> usersFromDb = session.execute("select * from users").all().stream().map(row -> new AuctionUser(
                row.getString("user_name"), row.getString("password"), row.getLong("salt"), row.getString("first_name"), row.getString("last_name"),
                row.getSet("emails", String.class))).collect(Collectors.<AuctionUser>toList());
        assertEquals(1, usersFromDb.size());
        AuctionUser auctionUser = usersFromDb.get(0);
        assertEquals(userCreate.getUserName(), auctionUser.getUserName());
        assertEquals("saltedPassword", auctionUser.getMd5Password());
        assertEquals(userCreate.getFirstName(), auctionUser.getFirstName());
        assertEquals(userCreate.getLastName(), auctionUser.getLastName());
        assertEquals(userCreate.getEmails(), auctionUser.getEmails());
        assertEquals(Long.valueOf(salt), auctionUser.getSalt());
    }

    @Test
    public void createUserThatAlreadyExists() throws Exception {
        Random rand = new Random();
        Long salt = rand.nextLong();
        //given
        UserCreate userCreate = new UserCreate("chbatey", salt, "bananas", "chris", "batey", Sets.newHashSet("christopher.batey@gmail.com"));
        underTest.createUser(userCreate);

        //when
        boolean userCreated = underTest.createUser(userCreate);

        //then
        assertFalse("Expected user creation to fail", userCreated);
    }

    @Test
    public void returnEmptyIfNoUser() throws Exception {
        assertEquals(Optional.<AuctionUser>empty(), underTest.retrieveUser("no_exist"));
    }

    @Test
    public void userDoesExist() throws Exception {
        Random rand = new Random();
        Long salt = rand.nextLong();

        //given
        session.execute("insert into users (user_name, password, salt, first_name, last_name, emails ) VALUES " +
                                          "( 'chbatey', 'password', "+ salt + ", 'christopher', 'batey', {'blah@blah.com'} );");
        //when
        AuctionUser chbatey = underTest.retrieveUser("chbatey").orElseThrow(() -> new AssertionError("Expected AuctionUser"));
        //then
        assertEquals("chbatey", chbatey.getUserName());
        assertEquals("christopher", chbatey.getFirstName());
        assertEquals("batey", chbatey.getLastName());
        assertEquals("password", chbatey.getMd5Password());
        assertEquals(Long.valueOf(salt), chbatey.getSalt());
        assertEquals(Sets.newHashSet("blah@blah.com"), chbatey.getEmails());
    }
}

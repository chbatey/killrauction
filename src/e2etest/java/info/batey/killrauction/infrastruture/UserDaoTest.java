package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import info.batey.killrauction.domain.User;
import info.batey.killrauction.web.user.UserCreate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserDaoTest {

    private static Cluster cluster;
    private static Session session;

    private UserDao underTest;
    private MessageDigest digest;
    private Base64.Encoder base64 = Base64.getEncoder();

    public UserDaoTest() throws Exception {
        digest = MessageDigest.getInstance("MD5");
    }

    @BeforeClass
    public static void schemaSetup() throws Exception {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS killrauction_tests WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 }");
        session.execute("use killrauction_tests");
        session.execute("CREATE TABLE IF NOT EXISTS users ( user_name text PRIMARY KEY, password text, first_name text , last_name text , emails set<text> )");
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
        underTest = new UserDao(session);
        underTest.prepareStatements();
    }

    @Test
    public void createsUser() throws Exception {
        //given
        UserCreate userCreate = new UserCreate("chbatey", "bananas", "chris", "batey", Sets.newHashSet("christopher.batey@gmail.com"));

        //when
        boolean userCreated = underTest.createUser(userCreate);

        //then
        assertTrue(userCreated);
        List<User> usersFromDb = session.execute("select * from users").all().stream().map(row -> new User(
                row.getString("user_name"), row.getString("password"), row.getString("first_name"), row.getString("last_name"), row.<String>getSet("emails", String.class)
        )).collect(Collectors.<User>toList());
        assertEquals(1, usersFromDb.size());
        User user = usersFromDb.get(0);
        assertEquals(userCreate.getUserName(), user.getUserName());
        String encodedPassword = base64.encodeToString(digest.digest(userCreate.getPassword().getBytes(Charsets.UTF_8)));
        assertEquals(encodedPassword, user.getMd5Password());
        assertEquals(userCreate.getFirstName(), user.getFirstName());
        assertEquals(userCreate.getLastName(), user.getLastName());
        assertEquals(userCreate.getEmails(), user.getEmails());
    }
}
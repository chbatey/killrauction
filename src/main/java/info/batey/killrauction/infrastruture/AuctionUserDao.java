package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import info.batey.killrauction.domain.AuctionUser;
import info.batey.killrauction.web.user.UserCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Optional;

@Component
public class AuctionUserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionUserDao.class);

    private static final String CREATE_USER_STATEMENT = "INSERT INTO users (user_name, password, salt, first_name , last_name , emails ) values (?, ?, ?, ?, ?, ?) if not exists";
    private static final String GET_USER_STATEMENT = "select * from users where user_name = ?";

    private Session session;
    private Md5PasswordEncoder md5PasswordEncoder;
    private SecureRandom secureRandom;

    private PreparedStatement createUser;
    private PreparedStatement getUser;


    @Autowired
    public AuctionUserDao(Session session, Md5PasswordEncoder md5PasswordEncoder, SecureRandom secureRandom) {
        this.session = session;
        this.md5PasswordEncoder = md5PasswordEncoder;
        this.secureRandom = secureRandom;
    }

    @PostConstruct
    public void prepareStatements() {
        createUser = session.prepare(CREATE_USER_STATEMENT);
        getUser = session.prepare(GET_USER_STATEMENT);
    }

    public boolean createUser(UserCreate userCreate) {
        LOGGER.debug("Creating user {}", userCreate);
        Object salt = secureRandom.nextLong();
        String endcodedPassword = md5PasswordEncoder.encodePassword(userCreate.getPassword(), salt);
        BoundStatement boundStatement = createUser.bind(userCreate.getUserName(), endcodedPassword, salt, userCreate.getFirstName(), userCreate.getLastName(), userCreate.getEmails());
        ResultSet response = session.execute(boundStatement);
        boolean applied = response.wasApplied();
        LOGGER.debug("User created {}", applied);
        return applied;
    }

    public Optional<AuctionUser> retrieveUser(String userName) {
        BoundStatement boundStatement = getUser.bind(userName);
        return Optional.ofNullable(session.execute(boundStatement).one()).map(row -> new AuctionUser(
                        row.getString("user_name"),
                        row.getString("password"),
                        row.getLong("salt"),
                        row.getString("first_name"),
                        row.getString("last_name"),
                        row.getSet("emails", String.class)));
    }
}

package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.google.common.base.Charsets;
import info.batey.killrauction.domain.AuctionUser;
import info.batey.killrauction.web.user.UserCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Component
public class AuctionUserDao {

    private final static String CREATE_USER_STATEMENT = "INSERT INTO users (user_name, password, first_name , last_name , emails ) values (?, ?, ?, ?, ?) if not exists";
    private final static String GET_USER_STATEMENT = "select * from users where user_name = ?";

    private Session session;
    private PreparedStatement createUser;
    private PreparedStatement getUser;
    private MessageDigest digest;
    private Base64.Encoder base64 = Base64.getEncoder();

    @Autowired
    public AuctionUserDao(Session session) {
        try {
            this.digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5", e);
        }
        this.session = session;
    }

    @PostConstruct
    public void prepareStatements() {
        createUser = session.prepare(CREATE_USER_STATEMENT);
        getUser = session.prepare(GET_USER_STATEMENT);
    }

    public boolean createUser(UserCreate userCreate) {
        String encodedPassword = base64.encodeToString(digest.digest(userCreate.getPassword().getBytes(Charsets.UTF_8)));
        BoundStatement boundStatement = createUser.bind(userCreate.getUserName(), encodedPassword, userCreate.getFirstName(), userCreate.getLastName(), userCreate.getEmails());
        session.execute(boundStatement);
        return true;
    }

    public Optional<AuctionUser> retrieveUser(String userName) {
        BoundStatement boundStatement = getUser.bind(userName);
        return Optional.ofNullable(session.execute(boundStatement).one()).map(row -> new AuctionUser(
                        row.getString("user_name"),
                        row.getString("password"),
                        row.getString("first_name"),
                        row.getString("last_name"),
                        row.getSet("emails", String.class)
                ));
    }
}

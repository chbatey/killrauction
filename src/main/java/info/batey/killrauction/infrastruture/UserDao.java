package info.batey.killrauction.infrastruture;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.google.common.base.Charsets;
import info.batey.killrauction.web.user.UserCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class UserDao {

    private final static String CREATE_USER_STATEMENT = "INSERT INTO users (user_name, password, first_name , last_name , emails ) values (?, ?, ?, ?, ?) if not exists";

    private Session session;
    private PreparedStatement createUser;
    private MessageDigest digest;
    private Base64.Encoder base64 = Base64.getEncoder();

    @Autowired
    public UserDao(Session session) {
        try {
            this.digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5", e);
        }
        this.session = session;
    }

    @PostConstruct
    public void prepareStatements() {
        createUser = this.session.prepare(CREATE_USER_STATEMENT);
    }

    public boolean createUser(UserCreate userCreate) {
        String encodedPassword = base64.encodeToString(digest.digest(userCreate.getPassword().getBytes(Charsets.UTF_8)));
        BoundStatement boundStatement = createUser.bind(userCreate.getUserName(), encodedPassword, userCreate.getFirstName(), userCreate.getLastName(), userCreate.getEmails());
        session.execute(boundStatement);
        return true;
    }
}

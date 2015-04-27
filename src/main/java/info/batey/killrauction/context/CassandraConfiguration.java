package info.batey.killrauction.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "cassandra")
public class CassandraConfiguration {

    @NotNull
    private List<String> contactPoints;

    @NotNull
    private String keyspace;

    public List<String> getContactPoints() {
        return this.contactPoints;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setContactPoints(List<String> contactPoints) {
        this.contactPoints = contactPoints;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

}

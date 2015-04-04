package info.batey.killrauction.context;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Component
@ConfigurationProperties(prefix = "cassandra")
public class CassandraConfiguration {

    @NotNull
    private String[] contactPoints;

    @NotNull
    private String keyspace;

    public String[] getContactPoints() {
        return this.contactPoints;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public void setContactPoints(String[] contactPoints) {
        this.contactPoints = contactPoints;
    }

    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    @Override
    public String toString() {
        return "CassandraConfiguration{" +
                "contactPoints=" + Arrays.toString(contactPoints) +
                ", keyspace='" + keyspace + '\'' +
                '}';
    }
}

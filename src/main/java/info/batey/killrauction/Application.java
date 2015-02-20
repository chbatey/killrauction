package info.batey.killrauction;

import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class })
@ComponentScan
public class Application {
    public static void main (String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Application.class, args);

    }

    @Bean
    public EmbeddedServletContainerFactory sessionlessServletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setTomcatContextCustomizers(Arrays.asList(new CustomCustomizer()));
        return factory;
    }

    @Bean
    public Blah hello() {
        return new Blah();

    }

    public static class Blah {

    }

    public static class CustomCustomizer implements TomcatContextCustomizer {
        @Override
        public void customize(Context context) {
            context.setUseHttpOnly(false);
        }
    }
}

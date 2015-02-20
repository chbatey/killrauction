package info.batey.killrauction;

import cucumber.api.CucumberOptions;
import cucumber.api.java.Before;
import cucumber.api.junit.Cucumber;
import info.batey.killrauction.Application;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/e2etest/resources/" )
public class RunEndToEndTests {

}

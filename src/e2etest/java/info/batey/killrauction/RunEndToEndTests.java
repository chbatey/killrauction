package info.batey.killrauction;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/e2etest/resources/" )
public class RunEndToEndTests {

}

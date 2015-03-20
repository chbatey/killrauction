package info.batey.killrauction.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private Md5PasswordEncoder md5PasswordEncoder;

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.info("Setting up users");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(md5PasswordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("salt");
        authProvider.setSaltSource(saltSource);
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        http.httpBasic();
        http.authorizeRequests()
                .antMatchers("/api/auction").hasRole("USER")
                .antMatchers("/ws").hasRole("USER")
                .antMatchers("/api/oldbids").hasRole("USER")
                .antMatchers("/websockets.html").hasRole("USER")
                .antMatchers("/api/user").hasAnyRole("ANONYMOUS", "USER");

    }
}

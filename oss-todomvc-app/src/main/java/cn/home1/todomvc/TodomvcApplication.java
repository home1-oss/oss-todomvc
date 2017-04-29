package cn.home1.todomvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication(exclude = {OAuth2AutoConfiguration.class})
//@EnableResourceServer // oauth2
@EnableTransactionManagement
@RestController
public class TodomvcApplication {

  @RequestMapping("/api/user") // oauth2
  public Principal user(final Principal user) {
    return user;
  }

  public static void main(final String... args) {
    SpringApplication.run(TodomvcApplication.class, args);
  }
}

package cn.home1.todomvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(exclude = {OAuth2AutoConfiguration.class})
@EnableFeignClients
public class TodomvcThymeleafApplication {

  public static void main(final String... args) {
    SpringApplication.run(TodomvcThymeleafApplication.class, args);
  }
}

package com.yirendai.todomvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

/**
 * Created by zechengzhao on 16-11-16.
 */
@SpringBootApplication
@EnableZuulProxy
public class TodomvcGatewayApplication {
  public static void main(final String... args) {
    SpringApplication.run(TodomvcGatewayApplication.class, args);
  }
}

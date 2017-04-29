package cn.home1.todomvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.google.common.collect.ImmutableMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RefreshScope
@RequestMapping("/api")
@RestController
public class GreetingController {

  @Value("${greeting}")
  private String greeting = "";

  @RequestMapping(method = GET, path = "/greeting", produces = {APPLICATION_JSON_VALUE})
  public Map<String, Object> greeting() {
    return ImmutableMap.of("greeting", this.greeting);
  }
}

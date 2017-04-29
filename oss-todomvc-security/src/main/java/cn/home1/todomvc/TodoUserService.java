package cn.home1.todomvc;

import cn.home1.oss.lib.security.api.BaseUserDetailsAuthenticationProvider;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by melody on 21/10/2016.
 */
@Slf4j
@Service
public class TodoUserService extends BaseUserDetailsAuthenticationProvider<TodoUser> {

  @Autowired(required = false)
  @Getter
  @Setter
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TodoUserRepository userRepo;

  @Override
  protected List<TodoUser> testUsers() {
    final List<TodoUser> testUsers = new ArrayList<>();
    for (int i = 0; i < 10; ++i) {
      final TodoUser user = TodoUser.builder().name("user" + i).enabled(true).password("password" + i).build();
      testUsers.add(user);
    }

    return testUsers;
  }

  @Override
  public TodoUser findByName(String username) {
    return userRepo.findByName(username);
  }

  @Override
  public TodoUser save(TodoUser staff) {
    if (staff != null) {
      staff.passwordEncode(this.passwordEncoder);
    }
    return userRepo.save(staff);
  }

  @Override
  protected void delete(TodoUser staff) {
    userRepo.delete(staff.getUid());
  }

  @Override
  protected GrantedAuthority saveRole(GrantedAuthority authority) {
    return null;
  }
}

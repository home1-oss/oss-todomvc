package cn.home1.todomvc;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import cn.home1.oss.lib.security.api.AbstractUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by zeche on 2016/11/1.
 */
@Data
@Entity
@Builder
@ToString(of = {"name", "enabled"})
@EqualsAndHashCode(of = {"name"})
@NoArgsConstructor
@AllArgsConstructor
public class TodoUser extends AbstractUser {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long uid;

  private String name;
  private String password;

  @Type(type = "yes_no")
  private Boolean enabled;

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return new HashSet<GrantedAuthority>();
  }

  @Override
  public String getId() {
    return uid == null ? null : uid.toString();
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  protected void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void passwordEncode(final PasswordEncoder passwordEncoder) {
    if (passwordEncoder != null && isNotBlank(this.password)) {
      this.password = passwordEncoder.encode(this.password);
    }
  }
}

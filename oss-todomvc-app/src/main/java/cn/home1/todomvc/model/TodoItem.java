package cn.home1.todomvc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cn.home1.oss.lib.errorhandle.api.ApplicationExceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class TodoItem implements Serializable {

  private static final long serialVersionUID = -1471143236650834794L;

  @Id
  @GeneratedValue
  private Long id;

  @JsonIgnore
  private Long userId;

  //ATTENTION: 故意穿透到持久层，测试errorhandle对hibernate抛出的异常的处理
  @Length(max = 20, message = "长度不能超过20")
  @Column(nullable = false, length = 20)
  private String title;

  @Column(nullable = false)
  private Boolean completed = Boolean.FALSE;

  public void check() {
    ApplicationExceptions.checkNotNull(this.id, HttpStatus.BAD_REQUEST, //
      "todo item's id is invalid null!");

    ApplicationExceptions.check(StringUtils.isNotBlank(this.title), HttpStatus.BAD_REQUEST,  //
      "todo item's title is null or blank");
  }
}

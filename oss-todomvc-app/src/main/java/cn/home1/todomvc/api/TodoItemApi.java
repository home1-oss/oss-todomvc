package cn.home1.todomvc.api;

import static java.lang.Long.parseLong;

import cn.home1.oss.lib.security.api.GenericUser;
import cn.home1.todomvc.model.TodoItem;
import cn.home1.todomvc.service.TodoItemService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 * 异常都不作处理 由errorHandle来处理.
 *
 * @author sunday
 */
@Slf4j
@RestController
@RequestMapping("/api/todos")
public class TodoItemApi {

  @Autowired
  private TodoItemService todosService;

  /**
   * 根据 userId 和 todoItemId，获取对应的todoItem.
   *
   * @param id todoItem的ID
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<TodoItem> findOne(final @PathVariable("id") Long id) {
    Long userId = this.getUserIdFromContext();
    return new ResponseEntity<>(this.todosService.findOne(id, userId), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<TodoItem>> fetchAllByUser() {
    Long userId = this.getUserIdFromContext();
    List<TodoItem> items = this.todosService.fetchAllByUser(userId);

    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void update( //
      @PathVariable("id") final Long id,  //
      @Valid @RequestBody final TodoItem item) {

    item.setId(id);
    final Long userId = this.getUserIdFromContext();

    this.todosService.update(item, userId);
  }

  // 为测试errorHandle的内容 这里不做todoModel的title长度检查
  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<TodoItem> add(final @RequestBody TodoItem model) {
    final Long userId = this.getUserIdFromContext();
    final TodoItem item = this.todosService.create(model, userId);
    return new ResponseEntity<>(item, HttpStatus.OK);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void delete(final @PathVariable("id") Long id) {
    final Long userId = this.getUserIdFromContext();
    this.todosService.delete(id, userId);
  }

  @RequestMapping(method = RequestMethod.DELETE)
  public void batchDelete(@RequestBody List<Long> items) {
    this.todosService.batchDelete(items, getUserIdFromContext());
  }

  @RequestMapping(method = RequestMethod.PUT)
  public void batchUpdate(@Valid @RequestBody final List<TodoItem> items) {
    this.todosService.batchUpdate(items, getUserIdFromContext());
  }

  // 以下两个接口，为基于模板的实现提供服务
  @RequestMapping(value = "/toggle_all", method = RequestMethod.PUT)
  public void toggleAll(@RequestParam("checked") boolean completed) {
    this.todosService.toggleAll(completed, getUserIdFromContext());
  }

  @RequestMapping(value = "/clear_completed", method = RequestMethod.DELETE)
  public void clearCompleted() {
    this.todosService.clearCompleted(getUserIdFromContext());
  }

  private Long getUserIdFromContext() {
    final Optional<GenericUser> userOptional = GenericUser.fromSecurityContext();
    return userOptional.isPresent() ? parseLong(userOptional.get().getId()) : null;
  }
}

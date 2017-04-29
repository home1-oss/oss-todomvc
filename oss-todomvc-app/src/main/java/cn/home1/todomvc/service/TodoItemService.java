package cn.home1.todomvc.service;

import static cn.home1.oss.lib.errorhandle.api.ApplicationExceptions.checkNotNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import cn.home1.oss.lib.errorhandle.api.ApplicationExceptions;
import cn.home1.todomvc.model.TodoItem;
import cn.home1.todomvc.repository.TotoItemRepository;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class TodoItemService {

  @Autowired
  private TotoItemRepository todosRepository;

  public TodoItem findOne(final Long id, final Long userId) {
    return this.todosRepository.findByIdAndUserId(id, userId);
  }

  public List<TodoItem> fetchAllByUser(final Long userId) {
    return this.todosRepository.findByUserId(userId);
  }

  public TodoItem create(final TodoItem item, final Long userId) {
    ApplicationExceptions.check(StringUtils.isNotBlank(item.getTitle()), HttpStatus.BAD_REQUEST,  //
        "todo item's title is null or blank");

    item.setUserId(userId);
    return this.todosRepository.save(item);
  }

  public void update(final TodoItem item, final Long userId) {
    item.check();

    TodoItem previousItem = this.findOne(item.getId(), userId);
    checkNotNull(previousItem, NOT_FOUND, //
        "document not found, todo itemId: {}, userId: {}", item.getId(), userId);

    BeanUtils.copyProperties(item, previousItem, "id", "userId");
    this.todosRepository.save(previousItem);
  }

  public void delete(final Long id, final Long userId) {
    checkNotNull(this.findOne(id, userId), NOT_FOUND, //
        "document not found, todo itemId: {}, userId: {}", id, userId);

    this.todosRepository.delete(id);
  }

  @Transactional
  public void batchUpdate(final List<TodoItem> items, final Long userId) {
    for (TodoItem item : items) {
      this.update(item, userId);
    }
  }

  @Transactional
  public void batchDelete(final List<Long> items, final Long userId) {
    for (final Long id : items) {
      this.delete(id, userId);
    }
  }

  // 以下两个接口，为基于模板的实现提供服务
  public void toggleAll(final boolean completed, final Long userId) {
    this.todosRepository.toggleAll(completed, userId);
  }

  public void clearCompleted(final Long userId) {
    this.todosRepository.clearCompleted(userId);
  }
}

package cn.home1.todomvc.repository;

import cn.home1.todomvc.model.TodoItem;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by melody on 2016/11/11.
 */
@FeignClient(name = TodoItemRpcRepository.SERVICE_NAME, path = "/api/todos")
public interface TodoItemRpcRepository {
  String SERVICE_NAME = "oss-todomvc-app";

  @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  TodoItem create(@RequestBody final TodoItem model);

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  TodoItem findById(@PathVariable("id") final Long id);

  @RequestMapping(method = RequestMethod.GET)
  List<TodoItem> findAll();

  @RequestMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
  void update( @PathVariable("id") final Long id, //
    @RequestBody final TodoItem item);

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  void deleteById(@PathVariable("id") final Long id);

  @RequestMapping(method = RequestMethod.DELETE)
  void batchDelete(@RequestBody final List<Long> items);

  @RequestMapping(method = RequestMethod.PUT)
  void batchUpdate(@RequestBody final List<TodoItem> items);

  // 以下两个接口，为基于模板的应用提供服务
  @RequestMapping(value = "/toggle_all", method = RequestMethod.PUT)
  void toggleAll(@RequestParam("checked") final boolean checked);

  @RequestMapping(value = "/clear_completed", method = RequestMethod.DELETE)
  void clearCompleted();
}

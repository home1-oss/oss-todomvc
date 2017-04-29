package cn.home1.todomvc;

import cn.home1.todomvc.model.TodoItem;
import cn.home1.todomvc.repository.TodoItemRpcRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by melody on 16/11/29.
 */
@Controller
@RequestMapping("/action")
public class TodoItemApi {

  @Autowired
  private TodoItemRpcRepository itemRepo;

  @RequestMapping(value = "/add_todo", method = RequestMethod.POST)
  public String createTodoItem(@RequestParam(name = "filter", defaultValue = "all") final String filter, //
    @RequestParam("title") final String title) {

    final TodoItem item = TodoItem.builder().title(title).completed(false).build();
    itemRepo.create(item);
    return "redirect:/index.html?filter=" + filter;
  }

  @RequestMapping(value = "/delete_todo", method = RequestMethod.GET)
  public String deleteTodoItem(@RequestParam("id") final long id,
    @RequestParam(name = "filter", defaultValue = "all") final String filter) {

    itemRepo.deleteById(id);
    return "redirect:/index.html?filter=" + filter;
  }

  @RequestMapping(value = "/update_todo", method = RequestMethod.GET)
  public String updateTodoItem(@RequestParam("id") final long id, //
    @RequestParam("title") final String title, //
    @RequestParam("completed") final boolean completed,
    @RequestParam(name = "filter", defaultValue = "all") final String filter) {

    final TodoItem item = TodoItem.builder().id(id).title(title).completed(completed).build();
    itemRepo.update(id, item);

    return "redirect:/index.html?filter=" + filter;
  }

  @RequestMapping(value = "/toggle_all", method = RequestMethod.GET)
  public String toggleAll(@RequestParam("checked") final boolean completed,
    @RequestParam(name = "filter", defaultValue = "all") final String filter
  ) {

    itemRepo.toggleAll(completed);
    return "redirect:/index.html?filter=" + filter;
  }

  @RequestMapping(value = "/clear_completed", method = RequestMethod.GET)
  public String clearCompleted(@RequestParam(name = "filter", defaultValue = "all") final String filter) {
    itemRepo.clearCompleted();
    return "redirect:/index.html?filter=" + filter;
  }
}


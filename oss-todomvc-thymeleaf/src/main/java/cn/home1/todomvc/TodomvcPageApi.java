package cn.home1.todomvc;

import static cn.home1.oss.lib.errorhandle.api.ApplicationExceptions.applicationException;
import static java.util.stream.Collectors.toList;

import cn.home1.oss.lib.security.api.GenericUser;
import cn.home1.todomvc.model.TodoItem;
import cn.home1.todomvc.repository.TodoItemRpcRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by melody on 16/11/28.
 */

@Slf4j
@Controller
public class TodomvcPageApi {
  public static final String LOGIN_TEXT_PREFIX = "当前登录用户: ";
  @Autowired
  private TodoItemRpcRepository itemRepo;

  @RequestMapping(value = "/login.html", method = RequestMethod.GET)
  public ModelAndView loginPage() {
    return new ModelAndView(TodomvcAttrs.LOGIN_PAGE);
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String homePage() {
    return "redirect:/index.html";
  }

  @RequestMapping(value = "/index.html", method = RequestMethod.GET)
  public ModelAndView indexPage(@RequestParam(name = "filter", defaultValue = "all") final String filter) {

    final Map<String, Object> attrs = new HashMap<>();
    attrs.put(TodomvcAttrs.FILTER, filter);
    attrs.put(TodomvcAttrs.HAS_TODOS, false);
    attrs.put(TodomvcAttrs.HAS_COMPLETED_TODOS, false);
    attrs.put(TodomvcAttrs.TOGGLE_ALL_CHECK_STATE, false);

    setLoginText(attrs);

    setTodos(filter, attrs);

    return new ModelAndView(TodomvcAttrs.INDEX_PAGE, attrs);
  }

  private void setToggleAllState(final List<TodoItem> todos, final Map<String, Object> attrs) {
    boolean checked = true;
    for (TodoItem todo : todos) {
      if (!todo.getCompleted()) {
        checked = false;
        break;
      }
    }

    attrs.put(TodomvcAttrs.TOGGLE_ALL_CHECK_STATE, checked);
  }

  private void setTodos(final String filter, final Map<String, Object> attrs) {

    List<TodoItem> todos = itemRepo.findAll();
    if (todos.isEmpty()) {
      attrs.put(TodomvcAttrs.TODOS, todos);
      return;
    }

    attrs.put(TodomvcAttrs.HAS_TODOS, true);
    setToggleAllState(todos, attrs);

    final List<TodoItem> activeTodos = todos.stream().filter(todo -> !todo.getCompleted()).collect(toList());
    final long count = activeTodos.size();
    attrs.put(TodomvcAttrs.ACTIVE_TODOS_LEFT_TEXT, this.getActiveTodosLeftText(count));

    final List<TodoItem> completedTodos = todos.stream() //
        .filter(TodoItem::getCompleted).collect(toList());
    attrs.put(TodomvcAttrs.HAS_COMPLETED_TODOS, !completedTodos.isEmpty());

    switch (filter) {
      case "all":
        attrs.put(TodomvcAttrs.TODOS, todos);
        break;
      case "active":
        attrs.put(TodomvcAttrs.TODOS, activeTodos);
        break;
      case "completed":
        attrs.put(TodomvcAttrs.TODOS, completedTodos);
        break;
      default:
        throw applicationException(HttpStatus.BAD_REQUEST, "filter must be in {all, active, completed}");
    }
  }

  private String getActiveTodosLeftText(final long leftActiveTodos) {
    return String.valueOf(leftActiveTodos) + (leftActiveTodos == 1 ? " item left" : " items left");
  }

  private void setLoginText(final Map<String, Object> attrs) {
    final Optional<GenericUser> userOptional = GenericUser.fromSecurityContext();
    final String loginText = LOGIN_TEXT_PREFIX + (userOptional.isPresent() ? userOptional.get().getName() : "");
    attrs.put(TodomvcAttrs.LOGIN_TEXT, loginText);
  }
}

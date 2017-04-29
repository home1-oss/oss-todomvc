package cn.home1.todomvc;

/**
 * Created by melody on 16/11/30.
 */
public final class TodomvcAttrs {
  // view name
  public static final String LOGIN_PAGE = "login";
  public static final String INDEX_PAGE = "index";

  // ModelAndView attribute keys for thymeleaf
  public static final String LOGIN_TEXT = "loginText";
  public static final String FILTER = "filter";
  public static final String TODOS = "todos";

  public static final String HAS_TODOS = "hasTodos";
  public static final String HAS_COMPLETED_TODOS = "hasCompletedTodos";
  public static final String TOGGLE_ALL_CHECK_STATE = "toggleAllChecked";
  public static final String ACTIVE_TODOS_LEFT_TEXT = "activeTodosLeftText";
}

package cn.home1.todomvc.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import cn.home1.todomvc.TodoUserService;
import cn.home1.todomvc.TodoUser;
import cn.home1.todomvc.TodomvcApplication;
import cn.home1.todomvc.model.TodoItem;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by zecheng on 16-10-24.
 */
@ActiveProfiles("it.env")
@SpringBootTest(classes={TodomvcApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@Slf4j
public class TodoItemServiceIT {
  @Autowired
  private TodoItemService itemService;

  @Autowired
  private TodoUserService userService;

  private TodoUser user;

  @Before
  public void init() {
    // create user
    final String username = "user1";
    this.user = userService.findByName(username);

    // create test items
    for (int i = 0; i < 10; ++i) {
      TodoItem item = new TodoItem();
      item.setTitle("testitem" + i);
      item = itemService.create(item, user.getUid());
    }
  }

  @Test
  public void testClearCompleted() {
    testToggleAll();

    List<TodoItem> items = itemService.fetchAllByUser(this.user.getUid());
    Assert.assertNotEquals(items.size(), 0);

    itemService.clearCompleted(this.user.getUid());

    items = itemService.fetchAllByUser(this.user.getUid());
    Assert.assertEquals(items.size(), 0);
  }

  private void testToggleAll() {
    itemService.toggleAll(true, this.user.getUid());
    List<TodoItem> items = itemService.fetchAllByUser(this.user.getUid());
    for (TodoItem item : items) {
      Assert.assertEquals(item.getCompleted(), true);
    }
  }
}

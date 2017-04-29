package cn.home1.todomvc.repository;

import cn.home1.todomvc.model.TodoItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TotoItemRepository extends JpaRepository<TodoItem, Long> {

  TodoItem findByIdAndUserId(long id, long userId);

  List<TodoItem> findByUserId(long userId);

  @Modifying
  @Query("update TodoItem t set t.completed=:completed where t.userId=:userId")
  void toggleAll(@Param("completed") final Boolean completed, @Param("userId") final Long userId);

  @Modifying
  @Query("delete TodoItem t where t.userId=:userId and t.completed=true")
  void clearCompleted(@Param("userId") final Long userId);
}

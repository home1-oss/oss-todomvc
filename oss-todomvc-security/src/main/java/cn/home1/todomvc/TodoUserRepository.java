package cn.home1.todomvc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by melody on 21/10/2016.
 */
@Transactional
public interface TodoUserRepository extends JpaRepository<TodoUser, Long> {
  TodoUser findByName(String name);
}

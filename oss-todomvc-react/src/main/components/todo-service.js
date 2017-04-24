
import Utils from './utils';
import { LocalCache } from './config';
import { TodoRepository, TodoException } from './todo-repository';

class TodoService {
  loadAll() {
    let _this = this;
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          _this.checkToken();
          let response = await TodoRepository.loadAll();
          let data = await response.json();
          if (response.ok) {
            LocalCache.todos = data;
            resolve(data);
          } else {
            reject(data);
          }
        } catch (ex) {
          reject(ex);
        }
      })();
    });
  }

  checkToken() {
    let token = localStorage.getItem("token");
    if (token == null || token.trim() === "") {
      throw new TodoException(401, "Unauthorized, redirect to login page?");
    }
  }

  addTodo(title) {
    let _this = this;
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          _this.checkToken();
          let response = await TodoRepository.create({title: title, completed: false});
          let data = await response.json();
          if (response.ok) {
            LocalCache.todos.push(data);
            resolve(data);
          } else {
            reject(data);
          }
        } catch (ex) {
          reject(ex);
        }
      })();
    });
  }

  destroy(todo) {
    let _this = this;
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          _this.checkToken();
          let response = await TodoRepository.delete(todo.id);
          if (response.ok) {
            LocalCache.todos = LocalCache.todos.filter(function (candidate) {
              return candidate !== todo;
            });
            resolve();
          } else {
            reject(response.json());
          }
        } catch (ex) {
          reject(ex);
        }
      })();
    });
  }

  // update
  update(todoToSave, updateContent) {
    let _this = this;
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          _this.checkToken();
          let item = Utils.extend({}, todoToSave, updateContent);
          let response = await TodoRepository.update(item);
          if (response.ok) {
            LocalCache.todos = LocalCache.todos.map((todo) => {
              return (todo === todoToSave) ? item : todo;
            });
            resolve();
          } else {
            reject(response.json());
          }
        } catch (ex) {
          reject(ex);
        }
      })();
    });
  }
  
  toggleAll(checked) {
    let _this = this;
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          _this.checkToken();
          let newTodos = LocalCache.todos.map( todo => Utils.extend({}, todo, {completed: checked}) );
          let response = await TodoRepository.batchUpdate(newTodos);
          if (response.ok) {
            LocalCache.todos = newTodos;
            resolve();
          } else {
            reject(response.json());
          }
        } catch (ex) {
          reject(ex);
        }
      })();
    });
  }
  
  clearCompleted() {
    let _this = this;
    return new Promise((resolve, reject) => {
      (async () => {
        try {
          _this.checkToken();
          let idsForDelete = [];
          let newTodos = [];
          LocalCache.todos.map((item)=> {
            if (item.completed) {
              idsForDelete.push(item.id);
            } else {
              newTodos.push(item);
            }
          });

          let response = await TodoRepository.batchDelete(idsForDelete);
          if (response.ok) {
            LocalCache.todos = newTodos;
            resolve();
          } else {
            reject(response.json())
          }
        } catch (ex) {
          reject(ex);
        }
      })();
    });
  }
}

export default TodoService;


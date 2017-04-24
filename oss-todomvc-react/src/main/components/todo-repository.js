import $ from 'jquery';

// not extends from error
class TodoException {
  constructor(status, localizedMessage) { 
    this.status = status;
    this.localizedMessage = localizedMessage;
  }
}

function errorHandler(xhr, status) {
  let msg = xhr.responseText; 
  if (msg){ 
    msg = JSON.parse(msg);
    if (msg.status == 401) {
      throw new TodoException(msg.status, "身份验证错误，是否跳转到登录页面？");
    } else if(msg.status == 422){
      throw new TodoException(msg.status, status);
    } else {                    
      throw new TodoException(msg.status, status + " " + msg.message);
    } 
  }
};

var TodoRepository = {
  loadAll: function() {
    let url = '/todomvc/api/todos';
    let headers = {
      "X-Auth-Token": localStorage.getItem("token")
    };

    let params = {
      method: "GET",
      headers: headers
    };

    return fetch(url, params);
  },

  create: function(item) {
    let url = '/todomvc/api/todos';
    let headers = {
      "X-Auth-Token": localStorage.getItem("token"),
      "Content-Type": "application/json"
    };

    let params = {
      method: "POST",
      headers: headers,
      body: JSON.stringify(item)
    };

    return fetch(url, params);
  },

  delete: function(id) {
    let url = '/todomvc/api/todos/' + id;
    let headers = {
      "X-Auth-Token": localStorage.getItem("token"),
    };

    let params = {
      method: "DELETE",
      headers: headers,
    };

    return fetch(url, params);
  },

  update: function(item) {
    let url = '/todomvc/api/todos/' + item.id;
    let headers = {
      "X-Auth-Token": localStorage.getItem("token"),
      "Content-Type": "application/json"
    };

    let params = {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(item)
    };

    return fetch(url, params);
  }, 

  batchUpdate: function(items) {
    let url = '/todomvc/api/todos';
    let headers = {
      "X-Auth-Token": localStorage.getItem("token"),
      "Content-Type": "application/json"
    };

    let params = {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(items)
    };

    return fetch(url, params);
  },

  batchDelete: function(ids) {
    let url = '/todomvc/api/todos';
    let headers = {
      "X-Auth-Token": localStorage.getItem("token"),
      "Content-Type": "application/json"
    };

    let params = {
      method: "DELETE",
      headers: headers,
      body: JSON.stringify(ids)
    };

    return fetch(url, params);
  }
};

export { TodoRepository, TodoException };


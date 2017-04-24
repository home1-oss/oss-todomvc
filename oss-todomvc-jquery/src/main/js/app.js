
var BASE_URL = "/todomvc/api/todos";

Handlebars.registerHelper('eq', function (a, b, options) {
    return a === b ? options.fn(this) : options.inverse(this);
});

function checkToken() {
  console.log("checkToken");
  let token = localStorage.getItem("token");
  if (token === null || token.trim() === "") {
    if (confirm("用户未登录，将跳转到登录页面！")) {
      localStorage.clear();
      window.location.href = "login.html";
    } else {
      alert("请重新登录！");
    }

    return false;
  }

  return true;
}

function handleError(xhr, textStatus) {
    var res = xhr.responseText;
    if (res) {
        var info = JSON.parse(res);
        if (info.status == 401) {
            if (confirm("验证失败, 是否重新登录?")) {
                localStorage.clear();
                window.location.href = "login.html";
            }
        } else if (info.status == 422) {
            alert("参数错误 field:" + info.errors[0].field + ",msg:" + info.errors[0].message);
        } else {
            alert(res);
        }
    }
};

var ENTER_KEY = 13;
var ESCAPE_KEY = 27;
///---------------------新加开始
var ajaxUtil = {
    exchange: function (method, url, d) {
        if (!checkToken()) {
            return;
        }
        var ret_data = null;
        $.ajax({
            type: method,
            async: false,
            dataType: "json",
            headers: {
              'X-Auth-Token': localStorage.getItem("token")
            },
            contentType: "application/json;charset=UTF-8",
            url: url,
            data: (d && JSON.stringify(d)) || '',
            success: function (ret) {
                ret_data = ret;
            },  //多
            error: handleError,
        });
        return ret_data;
    },
    // 只有请求到服务器，服务器正常返回 就 return true
    simplexCommunication: function (method, url, d) {
        if (!checkToken()) {
            return;
        }
        var ret = false;
        $.ajax({
            type: method,
            async: false,
            contentType: "application/json;charset=UTF-8",
            headers: {
              'X-Auth-Token': localStorage.getItem("token")
            },
            url: url,
            data: (d && JSON.stringify(d)) || '',
            success: function () {
                ret = true;
            },  //有不返回的接口 会报错 这里为简单 不再新写方法了
            error: handleError
        });
        return ret;
    }
}
///---------------------新加结束


var util = {
    uuid: function () {
        /*jshint bitwise:false */
        var i, random;
        var uuid = '';

        for (i = 0; i < 32; i++) {
            random = Math.random() * 16 | 0;
            if (i === 8 || i === 12 || i === 16 || i === 20) {
                uuid += '-';
            }
            uuid += (i === 12 ? 4 : (i === 16 ? (random & 3 | 8) : random)).toString(16);
        }

        return uuid;
    },
    pluralize: function (count, word) {
        return count === 1 ? word : word + 's';
    },
    store: function (namespace, data) {
        if (arguments.length > 1) {
            return localStorage.setItem(namespace, JSON.stringify(data));
        } else {
            var store = localStorage.getItem(namespace);
            return (store && JSON.parse(store)) || [];
        }
    },
    ///----------------新加开始
    findAll: function () {
        return ajaxUtil.exchange("GET", BASE_URL) || [];
    },
    update: function (data) {
        return ajaxUtil.simplexCommunication("PUT", BASE_URL + "/" + data.id, data);
    },
    delete: function (id) {
        return ajaxUtil.simplexCommunication("DELETE", BASE_URL + "/" + id);
    },
    add: function (data) {
        return ajaxUtil.exchange("POST", BASE_URL, data);
    }
    ///----------------新加结束
};

var App = {
    init: function () {
        ///----------------新加开始    原来的初始化代码  util.store('todos-jquery');
        this.todos = util.findAll();
        ///----------------新加结束
        this.todoTemplate = Handlebars.compile($('#todo-template').html());
        this.footerTemplate = Handlebars.compile($('#footer-template').html());
        this.bindEvents();


        new Router({
            '/:filter': function (filter) {
                this.filter = filter;
                this.render();
            }.bind(this)
        }).init('/all');
    },
    bindEvents: function () {
        $('#new-todo').on('keyup', this.create.bind(this));
        $('#toggle-all').on('change', this.toggleAll.bind(this));
        $('#footer').on('click', '#clear-completed', this.destroyCompleted.bind(this));
        $('#todo-list')
            .on('change', '.toggle', this.toggle.bind(this))
            .on('dblclick', 'label', this.edit.bind(this))
            .on('keyup', '.edit', this.editKeyup.bind(this))
            .on('focusout', '.edit', this.update.bind(this))
            .on('click', '.destroy', this.destroy.bind(this));
    },
    render: function () {
        var todos = this.getFilteredTodos();
        $('#todo-list').html(this.todoTemplate(todos));
        $('#main').toggle(todos.length > 0);
        $('#toggle-all').prop('checked', this.getActiveTodos().length === 0);
        this.renderFooter();
        $('#new-todo').focus();
        util.store('todos-jquery', this.todos);
    },
    renderFooter: function () {
        var todoCount = this.todos.length;
        var activeTodoCount = this.getActiveTodos().length;
        var template = this.footerTemplate({
            activeTodoCount: activeTodoCount,
            activeTodoWord: util.pluralize(activeTodoCount, 'item'),
            completedTodos: todoCount - activeTodoCount,
            filter: this.filter
        });

        $('#footer').toggle(todoCount > 0).html(template);
    },
    toggleAll: function (e) {
        var isChecked = $(e.target).prop('checked');

        this.todos.forEach(function (todo) {
            todo.completed = isChecked;
        });

        this.render();
    },
    getActiveTodos: function () {
        return this.todos.filter(function (todo) {
            return !todo.completed;
        });
    },
    getCompletedTodos: function () {
        return this.todos.filter(function (todo) {
            return todo.completed;
        });
    },
    getFilteredTodos: function () {
        if (this.filter === 'active') {
            return this.getActiveTodos();
        }

        if (this.filter === 'completed') {
            return this.getCompletedTodos();
        }

        return this.todos;
    },
    destroyCompleted: function () {
        ///----------------新加开始
        this.getCompletedTodos().forEach(function (todo) {
            util.delete(todo.id);  //仅仅演示用 正式环境请采用批量删除的方式
        });
        ///----------------新加结束
        this.todos = this.getActiveTodos();
        this.filter = 'all';
        this.render();
    },
    // accepts an element from inside the `.item` div and
    // returns the corresponding index in the `todos` array
    indexFromEl: function (el) {
        var id = $(el).closest('li').data('id');
        var todos = this.todos;
        var i = todos.length;

        while (i--) {
            if (todos[i].id === id) {
                return i;
            }
        }
    },

    create: function (e) {
        var $input = $(e.target);
        var val = $input.val().trim();

        if (e.which !== ENTER_KEY || !val) {
            return;
        }
        ///----------------新加开始 有改动
        var todo = {title: val, completed: false};
        todo = util.add(todo);
        if (todo) {
            this.todos.push(todo);
        }
        ///----------------新加开始

        $input.val('');
        this.render();
    },

    toggle: function (e) {
        var i = this.indexFromEl(e.target);
        this.todos[i].completed = !this.todos[i].completed;
        util.update(this.todos[i]);
        this.render();
    },

    edit: function (e) {
        var $input = $(e.target).closest('li').addClass('editing').find('.edit');
        $input.val($input.val()).focus();
    },

    editKeyup: function (e) {
        if (e.which === ENTER_KEY) {
            e.target.blur();
        }

        if (e.which === ESCAPE_KEY) {
            $(e.target).data('abort', true).blur();
        }
    },

    update: function (e) {
        var el = e.target;
        var $el = $(el);
        var val = $el.val().trim();

        if (!val) {
            this.destroy(e);
            return;
        }

        if ($el.data('abort')) {
            $el.data('abort', false);
        } else {
            var oldVal = this.todos[this.indexFromEl(el)].title;
            this.todos[this.indexFromEl(el)].title = val;
            ///----------------新加开始
            if (!util.update(this.todos[this.indexFromEl(el)])) {
                this.todos[this.indexFromEl(el)].title = oldVal;
            }
            ///----------------新加结束
        }

        this.render();
    },

    destroy: function (e) {
        ///----------------新加开始
        if (util.delete(this.todos[this.indexFromEl(e.target)].id)) {
            this.todos.splice(this.indexFromEl(e.target), 1);
        }
        ///----------------新加结束
        //      this.todos.splice(this.indexFromEl(e.target), 1);
        this.render();
    }
};

App.init();

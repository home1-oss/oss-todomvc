/*jshint quotmark:false */
/*jshint white:false */
/*jshint trailing:false */
/*jshint newcap:false */
/*global React, Router*/

import React from 'react';
import director from 'director';
import { withRouter } from 'react-router';

import Info from './info';
import Utils from './utils';
import { AppConfig, LocalCache } from './config';

import TodoItem from './todo-item';
import TodoNavbar from './todo-navbar';
import TodoFooter from './todo-footer';
import TodoService from './todo-service';

var ENTER_KEY = 13;

var Service = new TodoService();

var TodoApp = React.createClass({
  getInitialState: function () {
    this.newTodo = "";
    return {
      nowShowing: AppConfig.ALL_TODOS,
      editing: null,
      action: null
    };
  },

  componentDidMount: function() {
    Service.loadAll().then((data) => {
      this.setState({action: "loadAll"});
    }).catch((ex) => {
      this._handleException(ex);
    });
  },

  _handleException: function(ex) {
    if (ex.status == 401) {
      if (confirm(ex.localizedMessage)) {
        localStorage.clear();
        this.props.router.push('/');
      }
    } else {
      alert(ex.localizedMessage);
    }
  },

  _handleTypeClick: function(type) {
    this.setState({nowShowing: type});
  },

  handleChange: function (event) {
    this.newTodo = event.target.value;
  },

  handleNewTodoKeyDown: function (event) {
    if (event.keyCode !== ENTER_KEY) {
      return;
    }
    event.preventDefault();

    var val = event.target.value;
    if (val) {
      Service.addTodo(val).then((data) => {
        this.refs.edit.value = '';
        this.setState({action: 'item created'});
      }).catch((ex) => {
        this._handleException(ex);
      });
    }
  },

  destroy: function (todo) {
    Service.destroy(todo).then(() => {
      this.setState({action: "destroy"});
    }).catch((ex) => {
      this._handleException(ex);
    });
  },

  save: function (todoToSave, text) {
    Service.update(todoToSave, {title: text}).then((data) => {
      this.setState({editing: null});
    }).catch((ex) => {
        this._handleException(ex);
    });
  },

  toggle: function (todoToToggle) {
    Service.update(todoToToggle, {completed: !todoToToggle.completed}).then((data) => {
      this.setState({action: "toggle"});
    }).catch((ex) => {
        this._handleException(ex);
    });
  },
  
  toggleAll: function (event) {
    Service.toggleAll(event.target.checked).then(() => {
      this.setState({action: "toggleAll"});
    }).catch(ex => {
      this._handleException(ex);
    });
  },

  clearCompleted: function () {
    Service.clearCompleted().then(() => {
      this.setState({action: "clearCompleted"});
    }).catch((ex) => {
      this._handleException(ex);
    });
  },

  edit: function (todo) {
    this.setState({editing: todo.id});
  },

  cancel: function () {
    this.setState({editing: null});
  },
  
  render: function () {
    let footer;
    let main;
    let todos = LocalCache.todos;

    let shownTodos = todos.filter(function (todo) {
      switch (this.state.nowShowing) {
        case AppConfig.ACTIVE_TODOS:
          return !todo.completed;
        case AppConfig.COMPLETED_TODOS:
          return todo.completed;
        default:
          return true;
      }
    }, this);

    let todoItems = shownTodos.map(function (todo) {
      return (
          <TodoItem
              key={todo.id}
              todo={todo}
              onToggle={this.toggle.bind(this, todo)}
              onDestroy={this.destroy.bind(this, todo)}
              onEdit={this.edit.bind(this, todo)}
              editing={this.state.editing === todo.id}
              onSave={this.save.bind(this, todo)}
              onCancel={this.cancel}
          />
      );
    }, this);

    let activeTodoCount = todos.reduce(function (accum, todo) {
      return todo.completed ? accum : accum + 1;
    }, 0);

    var completedCount = todos.length - activeTodoCount;

    if (activeTodoCount || completedCount) {
      footer =
          <TodoFooter
              count={activeTodoCount}
              handleClickCallback={this._handleTypeClick}
              completedCount={completedCount}
              nowShowing={this.state.nowShowing}
              onClearCompleted={this.clearCompleted}
          />;
    }

    if (todos.length) {
      main = (
          <section className="main">
            <input
                className="toggle-all"
                type="checkbox"
                onChange={this.toggleAll}
                checked={activeTodoCount === 0}
            />
            <ul className="todo-list">
              {todoItems}
            </ul>
          </section>
      );
    }

    return (
      <div>
        <TodoNavbar />
        <div className="mvc-todo-container">
          <div className="todoapp">
            <header className="header">
              <h1>todos</h1>
              <input
                  ref="edit"
                  className="new-todo"
                  placeholder="What needs to be done?"
                  // value={this.newTodo}
                  onKeyDown={this.handleNewTodoKeyDown}
                  onChange={this.handleChange}
                  autoFocus={true} />
            </header>

            {main}
            {footer}
          </div>

          <Info page="todos"></Info>
        </div>
      </div>
    );
  }
});

export default withRouter(TodoApp);


import React, {Component} from 'react';
import {Router, Route, IndexRoute, Link, hashHistory} from 'react-router';

import TodoApp from './todo-app';
import TodoLogin from './todo-login';
import TodoFramework from './todo-framework';

class UrlRouter extends Component {
  render() {
    return (
      <Router history={hashHistory}>
        <Route path="/" component={TodoFramework}>
          <IndexRoute component={TodoLogin} />

          <Route path="todos" component={TodoApp} />
        </Route>
      </Router>
    );
  }
}

export default UrlRouter;


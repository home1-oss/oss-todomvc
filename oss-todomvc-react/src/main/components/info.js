
import React, { Component } from 'react';

class Info extends Component {
  render() {
    let page = this.props.page || "todos";
    let label = page === "login" ? "This is a form login page" : "This is todo items page";

    return (
      <footer className="info">
        <p>{label}</p>
        <p>Created by <a href="http://github.com/petehunt/">petehunt</a></p>
        <p>Part of <a href="http://todomvc.com">TodoMVC</a></p>
      </footer>
    );
  }
};

export default Info;

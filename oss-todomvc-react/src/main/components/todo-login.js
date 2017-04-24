import $ from 'jquery';
import React from 'react';
import { withRouter } from 'react-router';

import Info from './info';

var TodoLogin = React.createClass({
  componentDidMount: function() {
    let username = localStorage.getItem("name");
    let token = localStorage.getItem("token");
    if (username != null && username.trim() != "" && token != null && token.trim() != "") {
      this.props.router.push("todos");
    }
  },

  _handleSubmit: function(event) {
    event.preventDefault();

    let user = {
      username: this.refs.username.value,
      password: this.refs.password.value
    };

    let loginUrl = "/api/login";
    $.ajax({
      url: loginUrl,
      type: "POST",
      contentType: "application/x-www-form-urlencoded;charset=UTF-8",
      dataType: "json",
      data: user,
      success: function(data, status, xhr) {
        let token = xhr.getResponseHeader("X-Auth-Token");
        localStorage.setItem("token", token);
        localStorage.setItem("name", data.name);

        this.props.router.push("todos");
      }.bind(this),
      error: function (xhr, status, error) {
        alert(xhr.responseText);
      }.bind(this),
    });
  },

  render: function() {
    return (
      <div>
        <div id="login" className="mvc-login">
          <header>
            <img src="assets/images/logo-icon.png"> </img>
          </header>

          <section>
            <form onSubmit={this._handleSubmit}>
              <input ref='username' className="mvc-login-input" placeholder="用户名" autofocus/>
              <input ref='password' className="mvc-login-input" type="password" placeholder="密码" autofocus/>
              <br/> <br/> <br/>
              <input type="submit" className="sign-button" value="登录"/>
            </form>
          </section>
        </div>

        <Info page="login"></Info>

      </div>
    );
  }
});

export default withRouter(TodoLogin);

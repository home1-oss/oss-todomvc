import React from 'react';
import { withRouter } from 'react-router';
import {Nav, Navbar} from 'react-bootstrap';

var TodoNavbar = React.createClass({
  getInitialState: function() {
    return  {
      username: localStorage.getItem("name")
    };
  },

  _handleEvent: function(event) {
    localStorage.clear();
    this.props.router.push("/");
  },

  render: function() {

    let label = this.state.username == null ? "未登录" : "当前登陆用户: " + this.state.username;

    return (
      <div>
        <Navbar>
          <Navbar.Header>
            <Navbar.Brand>
              <span>TodoMVC</span>
            </Navbar.Brand>
          </Navbar.Header>

          <Navbar.Collapse>
            <Nav pullRight>
              <li><a href="#"><span id="display-username">{label}</span></a></li>
              <button type="button" className="btn btn-default navbar-btn" onClick={this._handleEvent}>退出</button>
            </Nav>
          </Navbar.Collapse>
        </Navbar>
      </div>
    );

  }
});

export default withRouter(TodoNavbar);

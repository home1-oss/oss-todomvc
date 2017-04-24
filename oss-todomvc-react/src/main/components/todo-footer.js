/*jshint quotmark:false */
/*jshint white:false */
/*jshint trailing:false */
/*jshint newcap:false */
/*global React */

import classNames from 'classnames/index';
import React from 'react';

import { AppConfig } from './config';
import Utils from './utils';
import { Link } from 'react-router';

var TodoFooter = React.createClass({
  _handleClick(type) {
    this.props.handleClickCallback(type);
  },

  render: function () {
    let activeTodoWord = Utils.pluralize(this.props.count, 'item');
    let clearButton = null;

    if (this.props.completedCount > 0) {
      clearButton = (
          <button className="clear-completed" onClick={this.props.onClearCompleted}>
            Clear completed
          </button>
      );
    }

    let nowShowing = this.props.nowShowing;
    return (
        <footer className="footer">
					<span className="todo-count">
						<strong>{this.props.count}</strong> {activeTodoWord} left
					</span>
          <ul className="filters">
            <li> <a href="javascript:void(0)"
                className={classNames({selected: nowShowing === AppConfig.ALL_TODOS})} 
                onClick={this._handleClick.bind(this, "all")}
                > All </a> </li>
            {' '}
            <li> <a href="javascript:void(0)"
                className={classNames({selected: nowShowing === AppConfig.ACTIVE_TODOS})} 
                onClick={this._handleClick.bind(this, "active")}
                > Active </a> </li>
            {' '}
            <li> <a href="javascript:void(0)"
                className={classNames({selected: nowShowing === AppConfig.COMPLETED_TODOS})}
                onClick={this._handleClick.bind(this, "completed")}
                > Completed </a> </li>
          </ul>
          {clearButton}
        </footer>
    );
  }
});

export default TodoFooter;

import React, {Component} from 'react';

class TodoFramework extends Component {
  render() {
    return (
      <div>
        {this.props.children}
      </div>
    );
  }
}

export default TodoFramework;


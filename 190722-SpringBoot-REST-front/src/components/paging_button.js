import React from 'react'

import './paging_button.css';

class PagingButton extends React.Component {
  constructor(props, context) {
    super(props,context);
    this.action = this.action.bind(this);
  }
  render() {
    return (
      <div className="pagingButton">
        <button onClick={this.action}>{this.props.caption}</button>
      </div>
    );
  }

  action() {
    this.props.fnSetOffset(this.props.offset);
  }
  componentDidMount() {
  }

  
};

export default PagingButton
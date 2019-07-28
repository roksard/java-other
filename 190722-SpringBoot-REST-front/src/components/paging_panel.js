import React from 'react';

import PagingButton from './paging_button';
import './paging_panel.css';

class PagingPanel extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.setOffset = this.setOffset.bind(this);
  }
  render() {
    let offset = this.props.offset;
    let limit = this.props.limit;
    let count = this.props.count;
    let min = offset + 1;
    let max = offset + limit;
    if (count == 0) {
      min = 0;
      max = 0;
    }
    if(max > count) 
      max = count;
    return (
      <div className='pagingPanel'>
        Показаны элементы
        <div className="pagingInteractive">
          <PagingButton offset={offset-limit} caption="<"
            fnSetOffset={this.setOffset} /> 
            <div className="pagingInteractiveText">{min}-{max} из {count}</div>
          <PagingButton offset={offset + limit} caption=">"
            fnSetOffset={this.setOffset} />
        </div>
      </div >
    );
  }

  //method-filter, checks that offset and limit dont go out of bounds
  //and then passes call to the upper level
  setOffset(offsetParam) {
    let offset = offsetParam;
    let limit = this.props.limit;
    let count = this.props.count;
    let change = true;
    if (this.props.count == 0 || offset < 0)
      offset = 0;
    else if ((offset+1) > count)
      change = false; //we dont change offset, because it goes beyond bound;
    if(change)
      this.props.fnSetOffset(offset);
  }
};

export default PagingPanel
import React from 'react';

import '../config.js';
import './tab_select.css';

class TabSelect extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.selectTab = this.selectTab.bind(this);
  }
  /*
    global.orgListTab 
    global.empListTab
    global.orgTreeTab
    global.empTreeTab
  */
  render() {
    return (
      <div className='panel'>
        <button onClick={() => {this.selectTab(global.orgListTab)}}>Список организаций</button>
        <button onClick={() => {this.selectTab(global.empListTab)}}>Список сотрудников</button>
        <button onClick={() => {this.selectTab(global.orgTreeTab)}}>Дерево организаций</button>
        <button onClick={() => {this.selectTab(global.empTreeTab)}}>Дерево сотрудников</button>
      </div >
    );
  }

  selectTab(id) {
    this.props.fnSelectTab(id);
  }
};

export default TabSelect
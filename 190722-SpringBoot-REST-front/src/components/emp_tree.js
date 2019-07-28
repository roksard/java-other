import React from 'react'
import PagingPanel from './paging_panel';
import './emp_tree.css';

class EmpTree extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.handleClick = this.handleClick.bind(this);
  }
  render() {
    return this.props.data.employee === undefined ? 'Нажмите кнопку Дерево '
    +' в списке сотрудников чтобы посмотреть дерево соответствующего сотрудника' : ( 
      <div className='empTree'>
        <div>Дерево сотрудника</div>
        <div>
          <div>
            <h5 className="empName">[{this.props.data.employee.id}] {this.props.data.employee.name}</h5>
            <div className="empParent">
              <div className='empParentHeader'>Руководитель:{' '}</div>
              {this.props.data.boss ?
                <a href="#" onClick={(event) => { event.preventDefault(); 
                  this.handleClick(this.props.data.boss.id) }}>
                  [{this.props.data.boss.id}] {this.props.data.boss.name}
                </a>
                : 'нет'
              }
            </div>
            <div className="empChildList">
              <div className='empChildListHeader'>Подчиненные:</div>
              {this.props.data.childEmployeeList.length == 0 ? 'нет' : ''}
              {this.props.data.childEmployeeList.map(chEmp => (
                  <div>
                    <a href="#" onClick={(event) => { event.preventDefault(); this.handleClick(chEmp.id); }} >
                    [{chEmp.id}] {chEmp.name}
                    </a>
                  </div>
                ))}
              <PagingPanel offset={this.props.offset} limit={this.props.limit} count={this.props.count}
                fnSetOffset={this.props.fnSetOffset} />
            </div>
          </div>
        </div>
      </div>
    );
  }

  handleClick(id) {
    this.props.fnGotoId(id);
  }

};

export default EmpTree
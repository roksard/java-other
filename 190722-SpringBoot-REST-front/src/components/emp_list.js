import React from 'react'
import PagingPanel from './paging_panel';
import './emp_list.css';

class EmpList extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.state = {name: '', orgName: ''};
    this.selectTab = this.selectTab.bind(this);
  }
  render() {
    return (
      <div>
        <div>
          <div>Список сотрудников
            <button className="listEditButton" onClick={() => { this.edit(null) }}>
              Добавить
            </button></div>
          <div className="empSearch">
            Поиск по имени сотрудника:
            <input className="listEditButton" type="text" name="name" 
              value={this.state.name} 
              onChange={event => {this.setState({ name: event.target.value })}} /> 
            названию организации:
            <input className="listEditButton" type="text" 
              name="orgName" value={this.state.orgName}
              onChange={event => {this.setState({ orgName: event.target.value })}} />
            <button className="listEditButton" onClick={() => { 
                this.props.fnSearch(this.state.name, this.state.orgName) }}>Поиск</button>
          </div>
          <ul>
            {this.props.data.map((card) => (
              <li>
                <div className="empListCard">
                    <div className="empListName">[{card.employee.id}]{card.employee.name}</div>
                    <div className="empListOrg">Организация:[{card.organisation.id}] {card.organisation.name}
                    </div>
                    <div className="empListBoss">Руководитель: {card.boss ? '[' + card.boss.id + '] ' +
                      card.boss.name : '-'}
                    </div>
                    <button className="listEditButton" onClick={() => { this.tree(card.employee) }}>
                      Дерево
                    </button>
                    <button className="listEditButton" onClick={() => { this.edit(card.employee) }}>
                      Ред.
                    </button>
                    <button className="listEditButton" onClick={() => { this.delete(card.employee) }}>
                      Удалить
                    </button>
                </div>
              </li>
            ))}
          </ul>
        </div>
        <div>
          <PagingPanel offset={this.props.offset} limit={this.props.limit} count={this.props.count}
            fnSetOffset={this.props.fnSetOffset} />
        </div>
      </div>
    );
  }

  tree(emp) {
    this.props.fnTree(emp.id);
  }

  edit(empP) {
    let emp = empP;
    if (emp == null)
      emp = { id: 0, name: '', parentId: 0, organisationId: 0 };
    this.props.fnEdit(emp);
  }

  delete(emp) {
    let selectTab1 = this.selectTab.bind(this);
    fetch(global.host + '/employee/' + emp.id + '/delete', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }, body: {}
    })
      .then(res => {
        if ((res.status >= 200) && (res.status < 300)) {
          alert('Удаление успешно.');
          this.selectTab(1);
        } else {
          if (res.status == 409) {
            alert('Неудача: нельзя удалить элемент содержащий дочерние элементы');
          } else
            alert('Неудача: ' + res.status);
        }
        
      })
      .catch(console.log);
  }

  selectTab() {
    this.props.fnSelectTab(global.empListTab);
  }

};

export default EmpList
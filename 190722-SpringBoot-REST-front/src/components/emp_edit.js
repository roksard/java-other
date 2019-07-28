import React from 'react'
import '../config.js';
import './emp_edit.css';

class EmpEdit extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.changeNameHandler = this.changeNameHandler.bind(this);
    this.changeParentIdHandler = this.changeParentIdHandler.bind(this);
    this.changeOrgIdHandler = this.changeOrgIdHandler.bind(this);
    this.apply = this.apply.bind(this);
    this.cancel = this.cancel.bind(this);
    this.selectTab = this.selectTab.bind(this);

    this.edit = this.props.id != 0;
    this.caption = this.edit ? 'Редактирование сотрудника' : 'Добавление сотрудника';
    this.state = {
      id: this.props.id,
      name: this.props.name,
      parentId: this.props.parentId,
      organisationId: this.props.organisationId,
    };
  }
  render() {
   
    return (
      <div className="empEdit">
        <div className="empEditCaption">{this.caption}</div>
        Имя сотрудника<br /><input type="text" name="name" value={this.state.name}
          onChange={this.changeNameHandler} /> {this.edit ? 'идентификатор: ['+this.state.id+']' : ''} <br />
        Идентификатор организации (0 - нет)<br /><input type="text" name="organisationId"
          value={this.state.organisationId} onChange={this.changeOrgIdHandler} /><br />
        Идентификатор руководителя (0 - нет)<br /><input type="text" name="parentId"
          value={this.state.parentId} onChange={this.changeParentIdHandler} /><br />
        <button onClick={this.apply}>Подтвердить</button>
        <button onClick={this.cancel}>Отмена</button>
      </div>
    );
  }

  changeNameHandler(event) {
    this.setState({ name: event.target.value });
  }

  changeParentIdHandler(event) {
    this.setState({ parentId: event.target.value });
  }

  changeOrgIdHandler(event) {
    this.setState({ organisationId: event.target.value });
  }

  apply() {
    let action = this.edit ? '/update' : '/add';
    let selectTab1 = this.selectTab.bind(this);
    fetch(global.host + '/employee' + action, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: this.state.id,
        name: this.state.name,
        parentId: this.state.parentId,
        organisationId: this.state.organisationId,
      })
    }).then(function(res){ 
      
      if((res.status >= 200) && (res.status < 300))
        alert('Редактирование/добавление успешно.');
      else {
        if (res.status == 409) {
          alert('Ошибка: руководитель должен быть в той же организации.');
        } else
          alert('Ошибка: ' + res.status);
        console.log(res);
      }
      selectTab1();
    })
    .catch(function(res){ console.log(res) });
  }

  selectTab() {
    this.props.fnSelectTab(global.empListTab);
  }

  cancel() {
    this.selectTab();
  }
};

export default EmpEdit
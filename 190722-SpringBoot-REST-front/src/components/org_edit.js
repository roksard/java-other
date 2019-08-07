import React from 'react'
import '../config.js';
import './org_edit.css';

class OrgEdit extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.changeNameHandler = this.changeNameHandler.bind(this);
    this.changeParentIdHandler = this.changeParentIdHandler.bind(this);
    this.apply = this.apply.bind(this);
    this.cancel = this.cancel.bind(this);
    this.selectTab = this.selectTab.bind(this);

    this.edit = this.props.id != 0;
    this.caption = this.edit ? 'Редактирование организации' : 'Добавление организации';
    this.state = {
      id: this.props.id,
      name: this.props.name,
      parentId: this.props.parentId,
    };
  }
  render() {
   
    return (
      <div className="orgEdit">
        <div className="orgEditCaption">{this.caption}</div>
        Наименование организации<br /><input type="text" name="name" value={this.state.name}
          onChange={this.changeNameHandler} /> {this.edit ? 'идентификатор: ['+this.state.id+']' : ''} <br />
        Идентификатор головной организации (0 - нет)<br /><input type="text" name="parentId"
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

  apply() {
    let action = this.edit ? '/update' : '/add';
    let selectTab1 = this.selectTab.bind(this);
    fetch(global.host + '/organisation' + action, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        id: this.state.id,
        name: this.state.name,
        parentId: this.state.parentId,
      })
      //{"id":1,"name":"orgA","parentId":0}
    }).then(function(res){ 
      
      if((res.status >= 200) && (res.status < 300))
        alert('Редактирование/добавление успешно.');
      else {
        alert('Ошибка:' + res.status + ": " +  res.headers.get(global.msgHeader)); 
      }
      selectTab1();
    })
    .catch(function(res){ console.log(res) })
  }

  selectTab() {
    this.props.fnSelectTab(global.orgListTab);
  }

  cancel() {
    this.selectTab();
  }
};

export default OrgEdit
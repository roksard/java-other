import React from 'react'
import PagingPanel from './paging_panel';
import './org_list.css';

class OrgList extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.edit = this.edit.bind(this);
    this.delete = this.delete.bind(this);
    this.tree = this.tree.bind(this);
    this.changeNameHandler = this.changeNameHandler.bind(this);
    this.state = {name: ''};
    this.selectTab = this.selectTab.bind(this);
  }
  render() {
    return (
      <div>
        <div>
          <div>Список организаций
            <button className="listEditButton" onClick={() => { this.edit(null) }}>
              Добавить
            </button>
          </div>
          <div className="orgSearchName">
          Поиск по названию организации<br /><input className="listEditButton" type="text" name="name" value={this.state.name}
          onChange={this.changeNameHandler} /> <button className="listEditButton" onClick={() => 
            {this.props.fnSearch(this.state.name)}}>Поиск</button>
          </div>
          <ul>
            {this.props.data.map((card) => (
              <div className="organisation-card">
                <li>
                  <div className="organisation">
                    <div className="organisationName">[{card.organisation.id}]{card.organisation.name}: 
                      </div>
                    <div className="organisationEmployees">{card.employeeCount} чел.</div>
                    <button className="listEditButton" onClick={()=>{this.tree(card.organisation)}}>
                      Дерево</button>
                    <button className="listEditButton" onClick={()=>{this.edit(card.organisation)}}>
                      Ред.</button>
                    <button className="listEditButton" onClick={()=>{this.delete(card.organisation)}}>
                      Удалить</button>
                    
                  </div>
                </li>
              </div>
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
  changeNameHandler(event) {
    this.setState({ name: event.target.value });
  }

  tree(org){
    this.props.fnTree(org.id);
  }

  edit(orgP) {
    let org = orgP;
    if(org == null)
      org = { id: 0, name: '', parentId: 0};
    this.props.fnEdit(org);
  }

  delete(org) {
    let selectTab1 = this.props.fnSelectTab.bind(this);
    fetch(global.host + '/organisation/'+org.id+'/delete', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }, body:{}})
      .then(res => {
        if((res.status >= 200) && (res.status < 300)) {
          alert('Удаление успешно.');
          selectTab1(global.orgListTab);
        } else {
          if(res.status == 409) {
            alert('Неудача: нельзя удалить элемент содержащий дочерние элементы');
          } else 
            alert('Неудача: ' + res.status); 
        }
      })
      .catch(console.log);
  }

  selectTab() {
    this.props.fnSelectTab(global.orgListTab);
  }

};

export default OrgList
import React, { Component } from 'react';
import './App.css';
import RawData from './components/raw';
import './config';
import OrgList from './components/org_list';
import EmployeeList from './components/emp_list';
import PagingButton from './components/paging_button';
import OrgTree from './components/org_tree';
import EmpTree from './components/emp_tree';
import TabSelect from './components/tab_select.js';
import OrgEdit from './components/org_edit.js';
import EmpEdit from './components/emp_edit.js';

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

class App extends Component {
  constructor(props, context) {
    super(props, context)
    this.state =
      {
        active_tab: global.orgListTab,
        //Organisation list 
        org_list: [],
        org_list_count: 0,
        org_list_offset: 0,
        org_list_limit: 4,
        org_list_nameSearch: "",

        //Organisation tree
        org_tree: {},
        org_tree_count: 0,
        org_tree_offset: 0,
        org_tree_limit: 4,
        org_tree_id: 0,

        //Employee list
        emp_list: [],
        emp_list_count: 0,
        emp_list_offset: 0,
        emp_list_limit: 4,
        emp_list_nameSearch: "",
        emp_list_organisationNameSearch: "",

        //Employee tree
        emp_tree: {},
        emp_tree_count: 0,
        emp_tree_offset: 0,
        emp_tree_limit: 4,
        emp_tree_id: 0,

        //edit org
        edit_org_id: 0,
        edit_org_name: '',
        edit_org_parentId: 0,

        //edit emp
        edit_emp_id: 0,
        edit_emp_name: '',
        edit_emp_parentId: 0,
        edit_emp_organisationId: 0,
      }

    this.selectTab = this.selectTab.bind(this);
    this.fetchTab = this.fetchTab.bind(this);

    this.setOrgListOffset = this.setOrgListOffset.bind(this);
    this.fetchOrgList = this.fetchOrgList.bind(this);
    this.searchOrg = this.searchOrg.bind(this);

    this.setOrgTreeOffset = this.setOrgTreeOffset.bind(this);
    this.fetchOrgTree = this.fetchOrgTree.bind(this);
    this.gotoOrgTreeId = this.gotoOrgTreeId.bind(this);

    this.setEmpListOffset = this.setEmpListOffset.bind(this);
    this.fetchEmpList = this.fetchEmpList.bind(this);
    this.searchEmp= this.searchEmp.bind(this);

    this.setEmpTreeOffset = this.setEmpTreeOffset.bind(this);
    this.fetchEmpTree = this.fetchEmpTree.bind(this);
    this.gotoEmpTreeId = this.gotoEmpTreeId.bind(this);

    this.editOrg = this.editOrg.bind(this);
    this.editEmp = this.editEmp.bind(this);
  }

  render() {
    return (
      <div className='App-component'>
        <TabSelect fnSelectTab={this.selectTab} />
        {this.renderTab()}
        {/* <RawData data={this.state.emp_tree} /> */}
      </div>
    );
  }

  renderTab() {
    switch (this.state.active_tab) {
      case global.orgListTab: return (
        <OrgList data={this.state.org_list} offset={this.state.org_list_offset}
          limit={this.state.org_list_limit} count={this.state.org_list_count}
          fnSetOffset={this.setOrgListOffset} fnEdit={this.editOrg} fnTree={this.gotoOrgTreeId} 
          fnSearch={this.searchOrg} fnSelectTab={this.selectTab} />);
      case global.orgTreeTab: return (
        <OrgTree data={this.state.org_tree} offset={this.state.org_tree_offset}
          limit={this.state.org_tree_limit} count={this.state.org_tree_count}
          fnSetOffset={this.setOrgTreeOffset} fnGotoId={this.gotoOrgTreeId} />);
      case global.empListTab: return (
        <EmployeeList data={this.state.emp_list} offset={this.state.emp_list_offset}
          limit={this.state.emp_list_limit} count={this.state.emp_list_count}
          fnSetOffset={this.setEmpListOffset} fnEdit={this.editEmp} fnTree={this.gotoEmpTreeId}
          fnSearch={this.searchEmp} fnSelectTab={this.selectTab}/>);
      case global.empTreeTab: return (
        <EmpTree data={this.state.emp_tree} offset={this.state.emp_tree_offset}
          limit={this.state.emp_tree_limit} count={this.state.emp_tree_count}
          fnSetOffset={this.setEmpTreeOffset} fnGotoId={this.gotoEmpTreeId} />);
      case global.editOrgTab: return (
        <OrgEdit id={this.state.edit_org_id} name={this.state.edit_org_name} 
          parentId={this.state.edit_org_parentId} fnSelectTab={this.selectTab} />); 
      case global.editEmpTab: return (
        <EmpEdit id={this.state.edit_emp_id} name={this.state.edit_emp_name} 
          parentId={this.state.edit_emp_parentId} organisationId={this.state.edit_emp_organisationId}
          fnSelectTab={this.selectTab} />); 
      default:
        return (<RawData data={this.state.org_tree} />);
    }
  }

  selectTab(id) {
    this.setState(
      { 
        active_tab: id,  
        org_list_offset: 0,
        org_tree_offset: 0,
        emp_list_offset: 0,
        emp_tree_offset: 0,
        org_list_nameSearch: "",
        emp_list_nameSearch: "",
        emp_list_organisationNameSearch: "",
      }
      , this.fetchTab)
  }

  fetchTab() {
    switch (this.state.active_tab) {
      case global.orgListTab:
        this.fetchOrgList();
        break;
      case global.orgTreeTab:
        this.fetchOrgTree();
        break;
      case global.empListTab:
        this.fetchEmpList();
        break;
      case global.empTreeTab:
        this.fetchEmpTree();
        break;
    }
  }

  //Org list methods:
  searchOrg(name) {
    this.setState({ 
      org_list_nameSearch: name,
      org_list_offset: 0 
    }, this.fetchOrgList)
  }

  setOrgListOffset(offsetParam) {
    let offset = offsetParam;
    this.setState({ org_list_offset: offset }, this.fetchOrgList)
  }

  fetchOrgList() {
    let offset = this.state.org_list_offset;
    let limit = this.state.org_list_limit;
    let nameSearch = this.state.org_list_nameSearch;

    fetch(global.host + '/organisation/list?offset=' + offset + '&limit=' + limit + '&nameSearch='
      + encodeURIComponent(nameSearch))
      .then(response => response.json())
      .then(json => {
        this.setState({ org_list: json });
      })
      .catch(console.log);

    fetch(global.host + '/organisation/list/count' + '?nameSearch=' + encodeURIComponent(nameSearch))
      .then(response => response.json())
      .then(json => {
        this.setState({ org_list_count: json.value });
      })
      .catch(console.log);
  }

  //Org tree methods:
  setOrgTreeOffset(offset) {
    //set state and call fetch function
    this.setState({ org_tree_offset: offset }, this.fetchOrgTree)
  }

  fetchOrgTree() {
    let offset = this.state.org_tree_offset;
    let limit = this.state.org_tree_limit;
    let id = this.state.org_tree_id;
    fetch(global.host + '/organisation/' + id + '/tree?offset=' + offset + '&limit=' + limit)
      .then(response => {
        console.log('body:'+response.body);
        if(response.status >= 200 && response.status < 300)
          return response.json();
        else
          console.log('status '+response.status+': '+response.headers.get('empdb-message'))
      })
      .then(json => {
        this.setState({ org_tree: json });
      })
      .catch(console.log);

    fetch(global.host + '/organisation/' + id + '/tree/count')
      .then(response => response.json())
      .then(json => {
        this.setState({ org_tree_count: json.value });
      })
      .catch(console.log);
  }


  //select another level of org tree
  gotoOrgTreeId(id) { 
    this.setState({ org_tree_id: id }, () => {this.fetchOrgTree(); this.selectTab(global.orgTreeTab)});
  }

  //Employee list methods
  searchEmp(name, orgName) {
    this.setState({ 
      emp_list_nameSearch: name,
      emp_list_organisationNameSearch: orgName,
      emp_list_offset: 0 
    }, this.fetchEmpList)
  }

  setEmpListOffset(offset) {
    //set state and call fetch function
    this.setState({ emp_list_offset: offset }, this.fetchEmpList)
  }

  fetchEmpList() {
    let offset = this.state.emp_list_offset;
    let limit = this.state.emp_list_limit;
    let nameSearch = this.state.emp_list_nameSearch;
    let organisationNameSearch = this.state.emp_list_organisationNameSearch

    fetch(global.host + '/employee/list?offset=' + offset + '&limit=' + limit
      + '&nameSearch=' + encodeURIComponent(nameSearch)
      + '&organisationNameSearch=' + encodeURIComponent(organisationNameSearch))
      .then(response => response.json())
      .then(json => {
        this.setState({ emp_list: json });
      })
      .catch(console.log);

    fetch(global.host + '/employee/list/count' + '?nameSearch=' + encodeURIComponent(nameSearch)
      + '&organisationNameSearch=' + encodeURIComponent(organisationNameSearch))
      .then(response => response.json())
      .then(json => {
        this.setState({ emp_list_count: json.value });
      })
      .catch(console.log);
  }

  //Employee tree methods
  setEmpTreeOffset(offset) {
    //set state and call fetch function
    this.setState({ emp_tree_offset: offset }, this.fetchEmpTree)
  }

  fetchEmpTree() {
    let offset = this.state.emp_tree_offset;
    let limit = this.state.emp_tree_limit;
    let id = this.state.emp_tree_id;
    
    // fetch(global.host + '/employee/' + id + '/tree?offset=' + offset + '&limit=' + limit)
    //   .then(response => response.json())
    //   .then(json => {
    //     this.setState({ emp_tree: json });
    //   })
    //   .catch(console.log);
    fetch(global.host + '/employee/' + id + '/tree?offset=' + offset + '&limit=' + limit, {
        method: 'GET',
        headers: {
          'Accept': 'text/html',
          'Access-Control-Expose-Headers': 'Baeldung-Example-Header',
        }
        }
      )
      .then(response => {
        if(response.status >= 200 && response.status < 300)
          response.json();
        else
          console.log('status '+response.status+': '+response.headers.get('empdb-message'))
      })
      .then(json => {
        this.setState({ emp_tree: json });
      })
      .catch(console.log);

    fetch(global.host + '/employee/' + id + '/tree/count')
      .then(response => response.json())
      .then(json => {
        this.setState({ emp_tree_count: json.value });
      })
      .catch(console.log);
  }

  //select another level of org tree
  gotoEmpTreeId(id) { 
    this.setState({ emp_tree_id: id }, () => {this.fetchEmpTree(); this.selectTab(global.empTreeTab)});
  }

  editOrg(org){
    this.setState({
      edit_org_id: org.id,
      edit_org_name: org.name,
      edit_org_parentId: org.parentId,
    }, () => {this.selectTab(global.editOrgTab)});
  }

  editEmp(emp){
    this.setState({
      edit_emp_id: emp.id,
      edit_emp_name: emp.name,
      edit_emp_parentId: emp.parentId,
      edit_emp_organisationId: emp.organisationId,
    }, () => {this.selectTab(global.editEmpTab)});
  }

  componentDidMount() {
    this.fetchTab();
  }
}

export default App;

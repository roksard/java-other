import React from 'react'
import PagingPanel from './paging_panel';
import './org_tree.css';

class OrgTree extends React.Component {
  constructor(props, context) {
    super(props, context);
    this.handleClick = this.handleClick.bind(this);
  }
  render() {
    return this.props.data.organisation === undefined ? 'Нажмите кнопку Дерево '
    +' в списке организаций чтобы посмотреть дерево соответствующей организации'  : (
      <div className='orgTree'>
        <div>Дерево организации</div>
        <div>
          <div>
            <h5 className="orgName">[{this.props.data.organisation.id}] {this.props.data.organisation.name}</h5>
            <div className="orgParent">
              <div className='orgParentHeader'>Головная организация:{' '}</div>
              {this.props.data.parentOrganisation ?
                <a href="#" onClick={(event) => { event.preventDefault(); this.handleClick(this.props.data.parentOrganisation.id) }}>
                  [{this.props.data.parentOrganisation.id}] {this.props.data.parentOrganisation.name}
                </a>
                : 'нет'
              }
            </div>
            <div className="orgChildList">
              <div className='orgChildListHeader'>Дочерние организации:</div>
              {this.props.data.childOrganisationList.length == 0 ? 'нет' : ''}
              {this.props.data.childOrganisationList.map(chOrg => (
                  <div>
                    <a href="#" onClick={(event) => { event.preventDefault(); this.handleClick(chOrg.id); }} >
                    [{chOrg.id}] {chOrg.name}
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

export default OrgTree
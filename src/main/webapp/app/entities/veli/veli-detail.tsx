import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './veli.reducer';
import { IVeli } from 'app/shared/model/veli.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IVeliDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class VeliDetail extends React.Component<IVeliDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { veliEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="ogrenciTakipSistemiApp.veli.detail.title">Veli</Translate> [<b>{veliEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ad">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.ad">Ad</Translate>
              </span>
            </dt>
            <dd>{veliEntity.ad}</dd>
            <dt>
              <span id="soyad">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.soyad">Soyad</Translate>
              </span>
            </dt>
            <dd>{veliEntity.soyad}</dd>
            <dt>
              <span id="adres">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.adres">Adres</Translate>
              </span>
            </dt>
            <dd>{veliEntity.adres}</dd>
            <dt>
              <span id="meslegi">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.meslegi">Meslegi</Translate>
              </span>
            </dt>
            <dd>{veliEntity.meslegi}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.email">Email</Translate>
              </span>
            </dt>
            <dd>{veliEntity.email}</dd>
            <dt>
              <span id="telefonCep">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.telefonCep">Telefon Cep</Translate>
              </span>
            </dt>
            <dd>{veliEntity.telefonCep}</dd>
            <dt>
              <span id="telefonSabit">
                <Translate contentKey="ogrenciTakipSistemiApp.veli.telefonSabit">Telefon Sabit</Translate>
              </span>
            </dt>
            <dd>{veliEntity.telefonSabit}</dd>
          </dl>
          <Button tag={Link} to="/entity/veli" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/veli/${veliEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ veli }: IRootState) => ({
  veliEntity: veli.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(VeliDetail);

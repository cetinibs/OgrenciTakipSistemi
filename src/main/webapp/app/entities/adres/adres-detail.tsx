import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './adres.reducer';
import { IAdres } from 'app/shared/model/adres.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAdresDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AdresDetail extends React.Component<IAdresDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { adresEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="ogrenciTakipSistemiApp.adres.detail.title">Adres</Translate> [<b>{adresEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="evAdres">
                <Translate contentKey="ogrenciTakipSistemiApp.adres.evAdres">Ev Adres</Translate>
              </span>
            </dt>
            <dd>{adresEntity.evAdres}</dd>
            <dt>
              <span id="isAdres">
                <Translate contentKey="ogrenciTakipSistemiApp.adres.isAdres">Is Adres</Translate>
              </span>
            </dt>
            <dd>{adresEntity.isAdres}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="ogrenciTakipSistemiApp.adres.email">Email</Translate>
              </span>
            </dt>
            <dd>{adresEntity.email}</dd>
            <dt>
              <span id="telefonCep">
                <Translate contentKey="ogrenciTakipSistemiApp.adres.telefonCep">Telefon Cep</Translate>
              </span>
            </dt>
            <dd>{adresEntity.telefonCep}</dd>
            <dt>
              <span id="telefonSabit">
                <Translate contentKey="ogrenciTakipSistemiApp.adres.telefonSabit">Telefon Sabit</Translate>
              </span>
            </dt>
            <dd>{adresEntity.telefonSabit}</dd>
            <dt>
              <Translate contentKey="ogrenciTakipSistemiApp.adres.musteri">Musteri</Translate>
            </dt>
            <dd>{adresEntity.musteri ? adresEntity.musteri.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/adres" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/adres/${adresEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ adres }: IRootState) => ({
  adresEntity: adres.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AdresDetail);

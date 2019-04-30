import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './odeme.reducer';
import { IOdeme } from 'app/shared/model/odeme.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IOdemeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class OdemeDetail extends React.Component<IOdemeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { odemeEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="ogrenciTakipSistemiApp.odeme.detail.title">Odeme</Translate> [<b>{odemeEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="tarih">
                <Translate contentKey="ogrenciTakipSistemiApp.odeme.tarih">Tarih</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={odemeEntity.tarih} type="date" format={APP_LOCAL_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="odemeAdi">
                <Translate contentKey="ogrenciTakipSistemiApp.odeme.odemeAdi">Odeme Adi</Translate>
              </span>
            </dt>
            <dd>{odemeEntity.odemeAdi}</dd>
            <dt>
              <span id="odemeDetayi">
                <Translate contentKey="ogrenciTakipSistemiApp.odeme.odemeDetayi">Odeme Detayi</Translate>
              </span>
            </dt>
            <dd>{odemeEntity.odemeDetayi}</dd>
            <dt>
              <span id="odeme">
                <Translate contentKey="ogrenciTakipSistemiApp.odeme.odeme">Odeme</Translate>
              </span>
            </dt>
            <dd>{odemeEntity.odeme}</dd>
            <dt>
              <Translate contentKey="ogrenciTakipSistemiApp.odeme.musteri">Musteri</Translate>
            </dt>
            <dd>{odemeEntity.musteri ? odemeEntity.musteri.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/odeme" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/odeme/${odemeEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ odeme }: IRootState) => ({
  odemeEntity: odeme.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OdemeDetail);

import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './musteri.reducer';
import { IMusteri } from 'app/shared/model/musteri.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMusteriDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MusteriDetail extends React.Component<IMusteriDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { musteriEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="ogrenciTakipSistemiApp.musteri.detail.title">Musteri</Translate> [<b>{musteriEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="ad">
                <Translate contentKey="ogrenciTakipSistemiApp.musteri.ad">Ad</Translate>
              </span>
            </dt>
            <dd>{musteriEntity.ad}</dd>
            <dt>
              <span id="soyad">
                <Translate contentKey="ogrenciTakipSistemiApp.musteri.soyad">Soyad</Translate>
              </span>
            </dt>
            <dd>{musteriEntity.soyad}</dd>
            <dt>
              <Translate contentKey="ogrenciTakipSistemiApp.musteri.veli">Veli</Translate>
            </dt>
            <dd>
              {musteriEntity.velis
                ? musteriEntity.velis.map((val, i) => (
                    <span key={val.id}>
                      <a>{val.id}</a>
                      {i === musteriEntity.velis.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </dd>
          </dl>
          <Button tag={Link} to="/entity/musteri" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/musteri/${musteriEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ musteri }: IRootState) => ({
  musteriEntity: musteri.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MusteriDetail);

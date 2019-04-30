import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMusteri } from 'app/shared/model/musteri.model';
import { getEntities as getMusteris } from 'app/entities/musteri/musteri.reducer';
import { getEntity, updateEntity, createEntity, reset } from './odeme.reducer';
import { IOdeme } from 'app/shared/model/odeme.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOdemeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IOdemeUpdateState {
  isNew: boolean;
  musteriId: string;
}

export class OdemeUpdate extends React.Component<IOdemeUpdateProps, IOdemeUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      musteriId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getMusteris();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { odemeEntity } = this.props;
      const entity = {
        ...odemeEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/odeme');
  };

  render() {
    const { odemeEntity, musteris, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ogrenciTakipSistemiApp.odeme.home.createOrEditLabel">
              <Translate contentKey="ogrenciTakipSistemiApp.odeme.home.createOrEditLabel">Create or edit a Odeme</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : odemeEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="odeme-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="tarihLabel" for="tarih">
                    <Translate contentKey="ogrenciTakipSistemiApp.odeme.tarih">Tarih</Translate>
                  </Label>
                  <AvField id="odeme-tarih" type="date" className="form-control" name="tarih" />
                </AvGroup>
                <AvGroup>
                  <Label id="odemeAdiLabel" for="odemeAdi">
                    <Translate contentKey="ogrenciTakipSistemiApp.odeme.odemeAdi">Odeme Adi</Translate>
                  </Label>
                  <AvField id="odeme-odemeAdi" type="text" name="odemeAdi" />
                </AvGroup>
                <AvGroup>
                  <Label id="odemeDetayiLabel" for="odemeDetayi">
                    <Translate contentKey="ogrenciTakipSistemiApp.odeme.odemeDetayi">Odeme Detayi</Translate>
                  </Label>
                  <AvField id="odeme-odemeDetayi" type="text" name="odemeDetayi" />
                </AvGroup>
                <AvGroup>
                  <Label id="odemeLabel" for="odeme">
                    <Translate contentKey="ogrenciTakipSistemiApp.odeme.odeme">Odeme</Translate>
                  </Label>
                  <AvField id="odeme-odeme" type="string" className="form-control" name="odeme" />
                </AvGroup>
                <AvGroup>
                  <Label for="musteri.id">
                    <Translate contentKey="ogrenciTakipSistemiApp.odeme.musteri">Musteri</Translate>
                  </Label>
                  <AvInput id="odeme-musteri" type="select" className="form-control" name="musteri.id">
                    <option value="" key="0" />
                    {musteris
                      ? musteris.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/odeme" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  musteris: storeState.musteri.entities,
  odemeEntity: storeState.odeme.entity,
  loading: storeState.odeme.loading,
  updating: storeState.odeme.updating,
  updateSuccess: storeState.odeme.updateSuccess
});

const mapDispatchToProps = {
  getMusteris,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(OdemeUpdate);

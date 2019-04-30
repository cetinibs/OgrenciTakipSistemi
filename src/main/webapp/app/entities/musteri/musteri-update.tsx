import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IVeli } from 'app/shared/model/veli.model';
import { getEntities as getVelis } from 'app/entities/veli/veli.reducer';
import { getEntity, updateEntity, createEntity, reset } from './musteri.reducer';
import { IMusteri } from 'app/shared/model/musteri.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMusteriUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMusteriUpdateState {
  isNew: boolean;
  idsveli: any[];
}

export class MusteriUpdate extends React.Component<IMusteriUpdateProps, IMusteriUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      idsveli: [],
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

    this.props.getVelis();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { musteriEntity } = this.props;
      const entity = {
        ...musteriEntity,
        ...values,
        velis: mapIdList(values.velis)
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/musteri');
  };

  render() {
    const { musteriEntity, velis, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ogrenciTakipSistemiApp.musteri.home.createOrEditLabel">
              <Translate contentKey="ogrenciTakipSistemiApp.musteri.home.createOrEditLabel">Create or edit a Musteri</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : musteriEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="musteri-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="adLabel" for="ad">
                    <Translate contentKey="ogrenciTakipSistemiApp.musteri.ad">Ad</Translate>
                  </Label>
                  <AvField id="musteri-ad" type="text" name="ad" />
                </AvGroup>
                <AvGroup>
                  <Label id="soyadLabel" for="soyad">
                    <Translate contentKey="ogrenciTakipSistemiApp.musteri.soyad">Soyad</Translate>
                  </Label>
                  <AvField id="musteri-soyad" type="text" name="soyad" />
                </AvGroup>
                <AvGroup>
                  <Label for="velis">
                    <Translate contentKey="ogrenciTakipSistemiApp.musteri.veli">Veli</Translate>
                  </Label>
                  <AvInput
                    id="musteri-veli"
                    type="select"
                    multiple
                    className="form-control"
                    name="velis"
                    value={musteriEntity.velis && musteriEntity.velis.map(e => e.id)}
                  >
                    <option value="" key="0" />
                    {velis
                      ? velis.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/musteri" replace color="info">
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
  velis: storeState.veli.entities,
  musteriEntity: storeState.musteri.entity,
  loading: storeState.musteri.loading,
  updating: storeState.musteri.updating,
  updateSuccess: storeState.musteri.updateSuccess
});

const mapDispatchToProps = {
  getVelis,
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
)(MusteriUpdate);

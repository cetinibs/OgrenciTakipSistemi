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
import { getEntity, updateEntity, createEntity, reset } from './adres.reducer';
import { IAdres } from 'app/shared/model/adres.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAdresUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAdresUpdateState {
  isNew: boolean;
  musteriId: string;
}

export class AdresUpdate extends React.Component<IAdresUpdateProps, IAdresUpdateState> {
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
      const { adresEntity } = this.props;
      const entity = {
        ...adresEntity,
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
    this.props.history.push('/entity/adres');
  };

  render() {
    const { adresEntity, musteris, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ogrenciTakipSistemiApp.adres.home.createOrEditLabel">
              <Translate contentKey="ogrenciTakipSistemiApp.adres.home.createOrEditLabel">Create or edit a Adres</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : adresEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="adres-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="evAdresLabel" for="evAdres">
                    <Translate contentKey="ogrenciTakipSistemiApp.adres.evAdres">Ev Adres</Translate>
                  </Label>
                  <AvField id="adres-evAdres" type="text" name="evAdres" />
                </AvGroup>
                <AvGroup>
                  <Label id="isAdresLabel" for="isAdres">
                    <Translate contentKey="ogrenciTakipSistemiApp.adres.isAdres">Is Adres</Translate>
                  </Label>
                  <AvField id="adres-isAdres" type="text" name="isAdres" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="ogrenciTakipSistemiApp.adres.email">Email</Translate>
                  </Label>
                  <AvField id="adres-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="telefonCepLabel" for="telefonCep">
                    <Translate contentKey="ogrenciTakipSistemiApp.adres.telefonCep">Telefon Cep</Translate>
                  </Label>
                  <AvField id="adres-telefonCep" type="text" name="telefonCep" />
                </AvGroup>
                <AvGroup>
                  <Label id="telefonSabitLabel" for="telefonSabit">
                    <Translate contentKey="ogrenciTakipSistemiApp.adres.telefonSabit">Telefon Sabit</Translate>
                  </Label>
                  <AvField id="adres-telefonSabit" type="text" name="telefonSabit" />
                </AvGroup>
                <AvGroup>
                  <Label for="musteri.id">
                    <Translate contentKey="ogrenciTakipSistemiApp.adres.musteri">Musteri</Translate>
                  </Label>
                  <AvInput id="adres-musteri" type="select" className="form-control" name="musteri.id">
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
                <Button tag={Link} id="cancel-save" to="/entity/adres" replace color="info">
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
  adresEntity: storeState.adres.entity,
  loading: storeState.adres.loading,
  updating: storeState.adres.updating,
  updateSuccess: storeState.adres.updateSuccess
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
)(AdresUpdate);

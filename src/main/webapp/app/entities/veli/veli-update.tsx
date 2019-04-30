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
import { getEntity, updateEntity, createEntity, reset } from './veli.reducer';
import { IVeli } from 'app/shared/model/veli.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVeliUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVeliUpdateState {
  isNew: boolean;
  musteriId: string;
}

export class VeliUpdate extends React.Component<IVeliUpdateProps, IVeliUpdateState> {
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
      const { veliEntity } = this.props;
      const entity = {
        ...veliEntity,
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
    this.props.history.push('/entity/veli');
  };

  render() {
    const { veliEntity, musteris, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ogrenciTakipSistemiApp.veli.home.createOrEditLabel">
              <Translate contentKey="ogrenciTakipSistemiApp.veli.home.createOrEditLabel">Create or edit a Veli</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : veliEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="veli-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="adLabel" for="ad">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.ad">Ad</Translate>
                  </Label>
                  <AvField id="veli-ad" type="text" name="ad" />
                </AvGroup>
                <AvGroup>
                  <Label id="soyadLabel" for="soyad">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.soyad">Soyad</Translate>
                  </Label>
                  <AvField id="veli-soyad" type="text" name="soyad" />
                </AvGroup>
                <AvGroup>
                  <Label id="adresLabel" for="adres">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.adres">Adres</Translate>
                  </Label>
                  <AvField id="veli-adres" type="text" name="adres" />
                </AvGroup>
                <AvGroup>
                  <Label id="meslegiLabel" for="meslegi">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.meslegi">Meslegi</Translate>
                  </Label>
                  <AvField id="veli-meslegi" type="text" name="meslegi" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.email">Email</Translate>
                  </Label>
                  <AvField id="veli-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="telefonCepLabel" for="telefonCep">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.telefonCep">Telefon Cep</Translate>
                  </Label>
                  <AvField id="veli-telefonCep" type="text" name="telefonCep" />
                </AvGroup>
                <AvGroup>
                  <Label id="telefonSabitLabel" for="telefonSabit">
                    <Translate contentKey="ogrenciTakipSistemiApp.veli.telefonSabit">Telefon Sabit</Translate>
                  </Label>
                  <AvField id="veli-telefonSabit" type="text" name="telefonSabit" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/veli" replace color="info">
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
  veliEntity: storeState.veli.entity,
  loading: storeState.veli.loading,
  updating: storeState.veli.updating,
  updateSuccess: storeState.veli.updateSuccess
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
)(VeliUpdate);

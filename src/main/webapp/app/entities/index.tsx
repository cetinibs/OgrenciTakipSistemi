import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Musteri from './musteri';
import Adres from './adres';
import Veli from './veli';
import Odeme from './odeme';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/musteri`} component={Musteri} />
      <ErrorBoundaryRoute path={`${match.url}/adres`} component={Adres} />
      <ErrorBoundaryRoute path={`${match.url}/veli`} component={Veli} />
      <ErrorBoundaryRoute path={`${match.url}/odeme`} component={Odeme} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;

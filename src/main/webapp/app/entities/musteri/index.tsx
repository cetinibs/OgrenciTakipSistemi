import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Musteri from './musteri';
import MusteriDetail from './musteri-detail';
import MusteriUpdate from './musteri-update';
import MusteriDeleteDialog from './musteri-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MusteriUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MusteriUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MusteriDetail} />
      <ErrorBoundaryRoute path={match.url} component={Musteri} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MusteriDeleteDialog} />
  </>
);

export default Routes;

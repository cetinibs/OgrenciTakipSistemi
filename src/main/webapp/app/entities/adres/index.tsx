import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Adres from './adres';
import AdresDetail from './adres-detail';
import AdresUpdate from './adres-update';
import AdresDeleteDialog from './adres-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AdresUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AdresUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AdresDetail} />
      <ErrorBoundaryRoute path={match.url} component={Adres} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AdresDeleteDialog} />
  </>
);

export default Routes;

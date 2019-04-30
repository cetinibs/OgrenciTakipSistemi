import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Veli from './veli';
import VeliDetail from './veli-detail';
import VeliUpdate from './veli-update';
import VeliDeleteDialog from './veli-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VeliUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VeliUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VeliDetail} />
      <ErrorBoundaryRoute path={match.url} component={Veli} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={VeliDeleteDialog} />
  </>
);

export default Routes;

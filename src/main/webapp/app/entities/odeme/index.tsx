import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Odeme from './odeme';
import OdemeDetail from './odeme-detail';
import OdemeUpdate from './odeme-update';
import OdemeDeleteDialog from './odeme-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OdemeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OdemeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OdemeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Odeme} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OdemeDeleteDialog} />
  </>
);

export default Routes;

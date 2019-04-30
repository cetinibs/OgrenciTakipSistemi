import axios from 'axios';
import {
  ICrudSearchAction,
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOdeme, defaultValue } from 'app/shared/model/odeme.model';

export const ACTION_TYPES = {
  SEARCH_ODEMES: 'odeme/SEARCH_ODEMES',
  FETCH_ODEME_LIST: 'odeme/FETCH_ODEME_LIST',
  FETCH_ODEME: 'odeme/FETCH_ODEME',
  CREATE_ODEME: 'odeme/CREATE_ODEME',
  UPDATE_ODEME: 'odeme/UPDATE_ODEME',
  DELETE_ODEME: 'odeme/DELETE_ODEME',
  RESET: 'odeme/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOdeme>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OdemeState = Readonly<typeof initialState>;

// Reducer

export default (state: OdemeState = initialState, action): OdemeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ODEMES):
    case REQUEST(ACTION_TYPES.FETCH_ODEME_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ODEME):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ODEME):
    case REQUEST(ACTION_TYPES.UPDATE_ODEME):
    case REQUEST(ACTION_TYPES.DELETE_ODEME):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ODEMES):
    case FAILURE(ACTION_TYPES.FETCH_ODEME_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ODEME):
    case FAILURE(ACTION_TYPES.CREATE_ODEME):
    case FAILURE(ACTION_TYPES.UPDATE_ODEME):
    case FAILURE(ACTION_TYPES.DELETE_ODEME):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ODEMES):
    case SUCCESS(ACTION_TYPES.FETCH_ODEME_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ODEME):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ODEME):
    case SUCCESS(ACTION_TYPES.UPDATE_ODEME):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ODEME):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/odemes';
const apiSearchUrl = 'api/_search/odemes';

// Actions

export const getSearchEntities: ICrudSearchAction<IOdeme> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ODEMES,
  payload: axios.get<IOdeme>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IOdeme> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ODEME_LIST,
    payload: axios.get<IOdeme>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOdeme> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ODEME,
    payload: axios.get<IOdeme>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOdeme> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ODEME,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IOdeme> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ODEME,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOdeme> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ODEME,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

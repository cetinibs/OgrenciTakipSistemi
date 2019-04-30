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

import { IAdres, defaultValue } from 'app/shared/model/adres.model';

export const ACTION_TYPES = {
  SEARCH_ADRES: 'adres/SEARCH_ADRES',
  FETCH_ADRES_LIST: 'adres/FETCH_ADRES_LIST',
  FETCH_ADRES: 'adres/FETCH_ADRES',
  CREATE_ADRES: 'adres/CREATE_ADRES',
  UPDATE_ADRES: 'adres/UPDATE_ADRES',
  DELETE_ADRES: 'adres/DELETE_ADRES',
  RESET: 'adres/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAdres>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AdresState = Readonly<typeof initialState>;

// Reducer

export default (state: AdresState = initialState, action): AdresState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ADRES):
    case REQUEST(ACTION_TYPES.FETCH_ADRES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ADRES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ADRES):
    case REQUEST(ACTION_TYPES.UPDATE_ADRES):
    case REQUEST(ACTION_TYPES.DELETE_ADRES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ADRES):
    case FAILURE(ACTION_TYPES.FETCH_ADRES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ADRES):
    case FAILURE(ACTION_TYPES.CREATE_ADRES):
    case FAILURE(ACTION_TYPES.UPDATE_ADRES):
    case FAILURE(ACTION_TYPES.DELETE_ADRES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ADRES):
    case SUCCESS(ACTION_TYPES.FETCH_ADRES_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ADRES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ADRES):
    case SUCCESS(ACTION_TYPES.UPDATE_ADRES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ADRES):
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

const apiUrl = 'api/adres';
const apiSearchUrl = 'api/_search/adres';

// Actions

export const getSearchEntities: ICrudSearchAction<IAdres> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ADRES,
  payload: axios.get<IAdres>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IAdres> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ADRES_LIST,
    payload: axios.get<IAdres>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAdres> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ADRES,
    payload: axios.get<IAdres>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAdres> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ADRES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IAdres> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ADRES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAdres> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ADRES,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

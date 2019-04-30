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

import { IMusteri, defaultValue } from 'app/shared/model/musteri.model';

export const ACTION_TYPES = {
  SEARCH_MUSTERIS: 'musteri/SEARCH_MUSTERIS',
  FETCH_MUSTERI_LIST: 'musteri/FETCH_MUSTERI_LIST',
  FETCH_MUSTERI: 'musteri/FETCH_MUSTERI',
  CREATE_MUSTERI: 'musteri/CREATE_MUSTERI',
  UPDATE_MUSTERI: 'musteri/UPDATE_MUSTERI',
  DELETE_MUSTERI: 'musteri/DELETE_MUSTERI',
  RESET: 'musteri/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMusteri>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type MusteriState = Readonly<typeof initialState>;

// Reducer

export default (state: MusteriState = initialState, action): MusteriState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MUSTERIS):
    case REQUEST(ACTION_TYPES.FETCH_MUSTERI_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MUSTERI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MUSTERI):
    case REQUEST(ACTION_TYPES.UPDATE_MUSTERI):
    case REQUEST(ACTION_TYPES.DELETE_MUSTERI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MUSTERIS):
    case FAILURE(ACTION_TYPES.FETCH_MUSTERI_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MUSTERI):
    case FAILURE(ACTION_TYPES.CREATE_MUSTERI):
    case FAILURE(ACTION_TYPES.UPDATE_MUSTERI):
    case FAILURE(ACTION_TYPES.DELETE_MUSTERI):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MUSTERIS):
    case SUCCESS(ACTION_TYPES.FETCH_MUSTERI_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_MUSTERI):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MUSTERI):
    case SUCCESS(ACTION_TYPES.UPDATE_MUSTERI):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MUSTERI):
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

const apiUrl = 'api/musteris';
const apiSearchUrl = 'api/_search/musteris';

// Actions

export const getSearchEntities: ICrudSearchAction<IMusteri> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MUSTERIS,
  payload: axios.get<IMusteri>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IMusteri> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MUSTERI_LIST,
    payload: axios.get<IMusteri>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IMusteri> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MUSTERI,
    payload: axios.get<IMusteri>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMusteri> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MUSTERI,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IMusteri> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MUSTERI,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMusteri> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MUSTERI,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

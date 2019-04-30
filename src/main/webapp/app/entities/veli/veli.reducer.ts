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

import { IVeli, defaultValue } from 'app/shared/model/veli.model';

export const ACTION_TYPES = {
  SEARCH_VELIS: 'veli/SEARCH_VELIS',
  FETCH_VELI_LIST: 'veli/FETCH_VELI_LIST',
  FETCH_VELI: 'veli/FETCH_VELI',
  CREATE_VELI: 'veli/CREATE_VELI',
  UPDATE_VELI: 'veli/UPDATE_VELI',
  DELETE_VELI: 'veli/DELETE_VELI',
  RESET: 'veli/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVeli>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type VeliState = Readonly<typeof initialState>;

// Reducer

export default (state: VeliState = initialState, action): VeliState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_VELIS):
    case REQUEST(ACTION_TYPES.FETCH_VELI_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VELI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VELI):
    case REQUEST(ACTION_TYPES.UPDATE_VELI):
    case REQUEST(ACTION_TYPES.DELETE_VELI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_VELIS):
    case FAILURE(ACTION_TYPES.FETCH_VELI_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VELI):
    case FAILURE(ACTION_TYPES.CREATE_VELI):
    case FAILURE(ACTION_TYPES.UPDATE_VELI):
    case FAILURE(ACTION_TYPES.DELETE_VELI):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_VELIS):
    case SUCCESS(ACTION_TYPES.FETCH_VELI_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_VELI):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VELI):
    case SUCCESS(ACTION_TYPES.UPDATE_VELI):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VELI):
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

const apiUrl = 'api/velis';
const apiSearchUrl = 'api/_search/velis';

// Actions

export const getSearchEntities: ICrudSearchAction<IVeli> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_VELIS,
  payload: axios.get<IVeli>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IVeli> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_VELI_LIST,
    payload: axios.get<IVeli>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IVeli> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VELI,
    payload: axios.get<IVeli>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVeli> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VELI,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IVeli> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VELI,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVeli> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VELI,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

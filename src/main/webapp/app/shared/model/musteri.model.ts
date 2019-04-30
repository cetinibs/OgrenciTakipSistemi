import { IAdres } from 'app/shared/model/adres.model';
import { IOdeme } from 'app/shared/model/odeme.model';
import { IVeli } from 'app/shared/model/veli.model';

export interface IMusteri {
  id?: number;
  ad?: string;
  soyad?: string;
  adres?: IAdres[];
  odemes?: IOdeme[];
  velis?: IVeli[];
}

export const defaultValue: Readonly<IMusteri> = {};

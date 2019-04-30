import { IMusteri } from 'app/shared/model/musteri.model';

export interface IAdres {
  id?: number;
  evAdres?: string;
  isAdres?: string;
  email?: string;
  telefonCep?: string;
  telefonSabit?: string;
  musteri?: IMusteri;
}

export const defaultValue: Readonly<IAdres> = {};

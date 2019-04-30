import { IMusteri } from 'app/shared/model/musteri.model';

export interface IVeli {
  id?: number;
  ad?: string;
  soyad?: string;
  adres?: string;
  meslegi?: string;
  email?: string;
  telefonCep?: string;
  telefonSabit?: string;
  musteris?: IMusteri[];
}

export const defaultValue: Readonly<IVeli> = {};

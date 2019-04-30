import { Moment } from 'moment';
import { IMusteri } from 'app/shared/model/musteri.model';

export interface IOdeme {
  id?: number;
  tarih?: Moment;
  odemeAdi?: string;
  odemeDetayi?: string;
  odeme?: number;
  musteri?: IMusteri;
}

export const defaultValue: Readonly<IOdeme> = {};

import {FishGender} from './FishGender';
import {Specie} from './Specie';
import {Pool} from './Pool';

export class Fish {
  id: number;
  name: string;
  gender: FishGender;
  distinctSign: string;
  arrivalDate: Date;
  returnDate: Date;
  specie: string;
  pool: number;
}

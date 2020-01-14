import {AnimalGender} from "./AnimalGender";
import {Specie} from "./Specie";

export class Animal {
  id: number;
  name: string;
  gender: AnimalGender;
  distinctSign: string;
  arrivalDate: Date;
  returnDate: Date;
  specie: Specie;
}

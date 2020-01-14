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

  constructor(id: number, name: string, gender: AnimalGender, distinctSign: string, arrivalDate: Date, returnDate: Date, specie: Specie) {
    this.id = id;
    this.name = name;
    this.distinctSign = distinctSign;
    this.gender = gender;
    this.arrivalDate = arrivalDate;
    this.returnDate = returnDate;
    this.specie = specie;
  }


}

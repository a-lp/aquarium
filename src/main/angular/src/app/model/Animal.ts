import {AnimalGender} from "./AnimalGender";

export class Animal {
  id: number;
  name: string;
  gender: AnimalGender;
  distinctSign: string;
  arrivalDate: Date;
  returnDate: Date;

  constructor(id: number, name: string, gender: AnimalGender, distinctSign: string, arrivalDate: Date, returnDate: Date) {
    this.id = id;
    this.name = name;
    this.distinctSign = distinctSign;
    this.gender = gender;
    this.arrivalDate = arrivalDate;
    this.returnDate = returnDate;
  }


}

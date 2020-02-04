import {Fish} from "./Fish";
import {Alimentation} from "./Alimentation";

export class Specie {
  private _id: number;
  name: string;
  lifeSpan: number;
  extinctionLevel: number;
  alimentation: Alimentation;
  animalList: Array<Fish>;
}

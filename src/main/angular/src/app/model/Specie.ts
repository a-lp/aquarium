import {Animal} from "./Animal";
import {Alimentation} from "./Alimentation";

export class Specie{
  name: string;
  lifeSpan: number;
  extinctionLevel: number;
  alimentation: Alimentation;
  animalList: Array<Animal>;
}

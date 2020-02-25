import {Fish} from './Fish';
import {Alimentation} from './Alimentation';

export class Specie {
  id: number;
  name: string;
  lifeSpan: number;
  extinctionLevel: number;
  alimentation: Alimentation;
  fishList: Array<Fish>;
}

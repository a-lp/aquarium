import {Fish} from './Fish';

export class Pool {
  id: number;
  maxCapacity: number;
  volume: number;
  condition: WaterCondition;
  fishes: Array<Fish>;

}

export enum WaterCondition {
  CLEAN = 'CLEAN', DIRTY = 'DIRTY'
}

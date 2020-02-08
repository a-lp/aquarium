import {Fish} from './Fish';
import {Sector} from './Sector';
import {Staff} from './Staff';
import {Schedule} from './Schedule';


export class Pool {
  id: number;
  maxCapacity: number;
  volume: number;
  condition: WaterCondition;
  fishes: Array<Fish>;
  sector: Sector;
  responsible: Staff | null;
  scheduledActivities: Array<Schedule> = [];
}

export enum WaterCondition {
  CLEAN = 'CLEAN', DIRTY = 'DIRTY'
}

import {Pool} from './Pool';
import {PoolActivity} from './PoolActivity';

export class Schedule {
  id: number;
  startPeriod: Date;
  endPeriod: Date;
  pool: Pool;
  activities: Array<PoolActivity> = [];
}

import {Pool} from './Pool';
import {PoolActivity} from './PoolActivity';

export class Schedule {
  id: number;
  startPeriod: Date;
  endPeriod: Date;
  repeated: boolean;
  pool: Pool;
  activities: Array<PoolActivity>;
}

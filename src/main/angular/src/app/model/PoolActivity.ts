import {Staff} from './Staff';
import {Schedule} from './Schedule';

export class PoolActivity {
  id: number;
  description: string;
  startActivity: Date;
  endActivity: Date;
  openToPublic: boolean;
  staffList: Array<Staff> = [];
  schedule: Schedule;
}

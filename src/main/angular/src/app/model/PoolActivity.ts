import {Staff} from './Staff';
import {Schedule} from './Schedule';

export class PoolActivity {
  id: number;
  description: string;
  startActivity: string;
  endActivity: string;
  openToPublic: boolean;
  repeated: boolean;
  staffList: Array<Staff> = [];
  schedule: Schedule;
}

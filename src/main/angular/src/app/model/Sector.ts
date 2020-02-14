import {Pool} from './Pool';
import {Staff} from './Staff';

export class Sector {
  id: number;
  name: string;
  location: string;
  pools: Array<Pool> = [];
  staffList: Array<Staff> = [];
}

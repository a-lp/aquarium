import {StaffRole} from './StaffRole';
import {Pool} from './Pool';
import {Sector} from './Sector';

export class Staff {
  id: number;
  name: string;
  surname: string;
  address: string;
  birthday: Date;
  socialSecurity: string;
  role: StaffRole = StaffRole.WORKER;
  poolResponsabilities: Array<Pool> = [];
  sectors: Array<Sector> = [];
}

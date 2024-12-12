import { Name } from './Name';
import { Behavior } from './Behavior';

export interface ForAssessingChildrensBehavior {
    findBehaviorByName(childName: Name): Behavior;
}


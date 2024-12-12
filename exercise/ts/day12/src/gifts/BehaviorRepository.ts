// src/gifts/BehaviorRepository.ts
import {Name} from './Name';
import {Behavior} from './Behavior';

export class BehaviorRepository { //TODO this is the adapter for tests, introduce port without setter
    private readonly behaviors: Map<Name, Behavior> = new Map();

    setBehavior(childName: Name, behavior: Behavior): void {
        this.behaviors.set(childName, behavior);
    }

    findBehaviorByName(childName: Name): Behavior {
        return this.behaviors.get(childName);
    }
}

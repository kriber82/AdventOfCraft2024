// src/gifts/BehaviorRepository.ts
import { Name } from '../src/gifts/Name';
import { Behavior } from '../src/gifts/Behavior';
import { ForAssessingChildrensBehavior } from '../src/gifts/ForAssessingChildrensBehavior';

export class BehaviorAssessmentForTest implements ForAssessingChildrensBehavior {
    private readonly behaviors: Map<Name, Behavior> = new Map();

    setBehavior(childName: Name, behavior: Behavior): void {
        this.behaviors.set(childName, behavior);
    }

    findBehaviorByName(childName: Name): Behavior {
        const behavior = this.behaviors.get(childName);

        if (!behavior) {
            throw new Error('No behavior record found for the child');
        }

        return behavior;
    }
}


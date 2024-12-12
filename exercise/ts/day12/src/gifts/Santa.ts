// src/gifts/Santa.ts
import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";
import {Name} from "./Name";
import {Behavior} from "./Behavior";
import {BehaviorRepository} from "./BehaviorRepository";

export class Santa {
    private readonly childrenRepository: ChildrenRepository;
    private readonly behaviorRepository: BehaviorRepository;

    constructor(childrenRepository: ChildrenRepository, behaviorRepository: BehaviorRepository) {
        this.childrenRepository = childrenRepository;
        this.behaviorRepository = behaviorRepository;
    }

    chooseToyForChild(childName: Name): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);
        const behavior = this.behaviorRepository.findBehaviorByName(childName);

        switch (behavior) {
            case Behavior.Naughty:
                return foundChild.wishlist.getThirdChoice();
            case Behavior.Nice:
                return foundChild.wishlist.getSecondChoice();
            case Behavior.VeryNice:
                return foundChild.wishlist.getFirstChoice();
            default:
                return undefined;
        }
    }
}

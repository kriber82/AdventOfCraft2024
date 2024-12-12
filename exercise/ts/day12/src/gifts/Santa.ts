import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";
import {Name} from "./Name";
import {Behavior} from "./Behavior";

export class Santa {
    private readonly childrenRepository: ChildrenRepository;

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository;
    }

    chooseToyForChild(childName: Name): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);

        switch (foundChild.desires.behavior) {
            case Behavior.Naughty:
                return foundChild.desires.wishlist.getThirdChoice();
            case Behavior.Nice:
                return foundChild.desires.wishlist.getSecondChoice();
            case Behavior.VeryNice:
                return foundChild.desires.wishlist.getFirstChoice();
            default:
                return undefined;
        }
    }
}

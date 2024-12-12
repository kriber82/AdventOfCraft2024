import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";
import { Behavior } from './Behavior';
import {Name} from "./Name";

export class Santa {
    private readonly childrenRepository: ChildrenRepository;

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository;
    }

    chooseToyForChild(childName: Name): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);

        switch (foundChild.behavior) {
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

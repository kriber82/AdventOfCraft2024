import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";
import { Behavior } from './Behavior';

export class Santa {
    private readonly childrenRepository: ChildrenRepository;

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository;
    }

    chooseToyForChild(childName: string): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);

        switch (foundChild.behavior) {
            case Behavior.Naughty:
                return foundChild.wishlist[2];
            case Behavior.Nice:
                return foundChild.wishlist[1];
            case Behavior.VeryNice:
                return foundChild.wishlist[0];
            default:
                return undefined;
        }

        return undefined;
    }
}

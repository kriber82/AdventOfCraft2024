import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";

export class Santa {
    private readonly childrenRepository: ChildrenRepository;

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository
    }

    chooseToyForChild(childName: string): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);

        switch (foundChild.behavior) {
            case 'naughty':
            return foundChild.wishlist[2];
            case 'nice':
            return foundChild.wishlist[1];
            case 'very nice':
            return foundChild.wishlist[0];
            default:
                return undefined;
        }

        return undefined;
    }
}

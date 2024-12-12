import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";

export class Santa {
    private readonly childrenRepository: ChildrenRepository;

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository
    }

    chooseToyForChild(childName: string): Toy | undefined {
            const foundChild = this.childrenRepository.findByName(childName);


        if (foundChild.behavior === 'naughty') {
            return foundChild.wishlist[2];
        } else if (foundChild.behavior === 'nice') {
            return foundChild.wishlist[1];
        } else if (foundChild.behavior === 'very nice') {
            return foundChild.wishlist[0];
        }

        return undefined;
    }
}

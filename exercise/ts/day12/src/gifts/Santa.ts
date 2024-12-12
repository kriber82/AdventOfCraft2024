import { Child } from './Child';
import { Toy } from './Toy';
import {ChildrenRepository} from "./ChildrenRepository";

export class Santa {
    private readonly childrenRepository: ChildrenRepository = new ChildrenRepository()

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository
    }

    addChild(child: Child): void {
        this.childrenRepository.addChild(child);
    }

    chooseToyForChild(childName: string): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);

        if (!foundChild) {
            throw new Error('No such child found');
        }

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
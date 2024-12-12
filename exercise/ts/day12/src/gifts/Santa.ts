import { Toy } from './Toy';
import { ChildrenRepository } from "./ChildrenRepository";
import { Name } from "./Name";

export class Santa {
    private readonly childrenRepository: ChildrenRepository;

    constructor(childrenRepository: ChildrenRepository) {
        this.childrenRepository = childrenRepository;
    }

    chooseToyForChild(childName: Name): Toy | undefined {
        const foundChild = this.childrenRepository.findByName(childName);
        return foundChild.desires.chooseToy();
    }
}

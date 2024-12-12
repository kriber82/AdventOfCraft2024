import {Child} from "./Child";

export class ChildrenRepository {
    private readonly childrenRepository: Child[] = [];

    addChild(child: Child): void {
        this.childrenRepository.push(child);
    }

    findByName(childName: string): Child {
        const foundChild = this.childrenRepository.find(child => child.name === childName);

        if (!foundChild) {
            throw new Error('No such child found');
        }

        return foundChild;
    }
}

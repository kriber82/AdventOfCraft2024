import {Child} from "./Child";
import {Name} from "./Name";

export class ChildrenRepository {
    private readonly children: Child[] = [];

    addChild(child: Child): void {
        this.children.push(child);
    }

    findByName(childName: Name): Child {
        const foundChild = this.children.find(child => child.name === childName);

        if (!foundChild) {
            throw new Error('No such child found');
        }

        return foundChild;
    }
}

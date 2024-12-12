import {Child} from "./Child";

export class ChildrenRepository {
    private readonly childrenRepository: Child[] = [];

    addChild(child: Child): void {
        this.childrenRepository.push(child);
    }

    findByName(childName: string): Child | undefined {
        return this.childrenRepository.find(child => child.name === childName);
    }
}
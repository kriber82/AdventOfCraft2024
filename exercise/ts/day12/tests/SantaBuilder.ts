// tests/SantaBuilder.ts
import { Santa } from '../src/gifts/Santa';
import { ChildrenRepository } from '../src/gifts/ChildrenRepository';
import { BehaviorRepository } from '../src/gifts/BehaviorRepository';
import { Child } from '../src/gifts/Child';
import { Name } from '../src/gifts/Name';
import { Behavior } from '../src/gifts/Behavior';
import { Wishlist } from '../src/gifts/Wishlist';

export class SantaBuilder {
    private childrenRepository: ChildrenRepository;
    private behaviorRepository: BehaviorRepository;

    constructor() {
        this.childrenRepository = new ChildrenRepository();
        this.behaviorRepository = new BehaviorRepository();
    }

    withChild(name: Name, behavior: Behavior, wishlist: Wishlist): SantaBuilder {
        const child = new Child(name, wishlist);
        this.childrenRepository.addChild(child);
        this.behaviorRepository.setBehavior(name, behavior);
        return this;
    }

    build(): Santa {
        return new Santa(this.childrenRepository, this.behaviorRepository);
    }
}

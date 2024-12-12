import { Santa } from '../src/gifts/Santa';
import { ChildrenRepository } from '../src/gifts/ChildrenRepository';
import { Child } from '../src/gifts/Child';
import { Name } from '../src/gifts/Name';
import { Behavior } from '../src/gifts/Behavior';
import { Desires } from '../src/gifts/Desires';
import { Wishlist } from '../src/gifts/Wishlist';

export class SantaBuilder {
    private childrenRepository: ChildrenRepository;

    constructor() {
        this.childrenRepository = new ChildrenRepository();
    }

    withChild(name: Name, behavior: Behavior, wishlist: Wishlist): SantaBuilder {
        const desires = new Desires(behavior, wishlist);
        const child = new Child(name, desires);
        this.childrenRepository.addChild(child);
        return this;
    }

    build(): Santa {
        return new Santa(this.childrenRepository);
    }
}

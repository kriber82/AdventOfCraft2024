// tests/SantaBuilder.ts
import { Santa } from '../src/gifts/Santa';
import { BehaviorRepository } from '../src/gifts/BehaviorRepository';
import { WishlistRepository } from '../src/gifts/WishlistRepository';
import { Child } from '../src/gifts/Child';
import { Name } from '../src/gifts/Name';
import { Behavior } from '../src/gifts/Behavior';
import { Wishlist } from '../src/gifts/Wishlist';

export class SantaBuilder {
    private behaviorRepository: BehaviorRepository;
    private wishlistRepository: WishlistRepository;

    constructor() {
        this.behaviorRepository = new BehaviorRepository();
        this.wishlistRepository = new WishlistRepository();
    }

    withChild(name: Name, behavior: Behavior, wishlist: Wishlist): SantaBuilder {
        const child = new Child(name);
        if (behavior)
            this.behaviorRepository.setBehavior(name, behavior);
        if (wishlist)
            this.wishlistRepository.setWishlist(name, wishlist);
        return this;
    }

    build(): Santa {
        return new Santa(
            this.behaviorRepository,
            this.wishlistRepository
        );
    }
}


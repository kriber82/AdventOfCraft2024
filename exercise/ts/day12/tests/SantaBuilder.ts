// tests/SantaBuilder.ts
import { Santa } from '../src/gifts/Santa';
import { BehaviorAssessmentForTest } from './BehaviorAssessmentForTest';
import { WishlistRepositoryForTest } from './WishlistRepositoryForTest';
import { Name } from '../src/gifts/Name';
import { Behavior } from '../src/gifts/Behavior';
import { Wishlist } from '../src/gifts/Wishlist';

export class SantaBuilder {
    private readonly behaviorRepository: BehaviorAssessmentForTest;
    private readonly wishlistRepository: WishlistRepositoryForTest;

    constructor() {
        this.behaviorRepository = new BehaviorAssessmentForTest();
        this.wishlistRepository = new WishlistRepositoryForTest();
    }

    withChild(name: Name, behavior: Behavior, wishlist: Wishlist): SantaBuilder {
        if (behavior) this.behaviorRepository.setBehavior(name, behavior);
        if (wishlist) this.wishlistRepository.setWishlist(name, wishlist);
        return this;
    }

    build(): Santa {
        return new Santa(
            this.behaviorRepository,
            this.wishlistRepository
        );
    }
}


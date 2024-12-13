// src/gifts/Santa.ts
import { Toy } from './Toy';
import { Name } from "./Name";
import { Behavior } from "./Behavior";
import { ForAssessingChildrensBehavior } from './ForAssessingChildrensBehavior';
import { ForGettingWishlists } from './ForGettingWishlists';
import { Wishlist } from './Wishlist';

export class Santa {
    constructor(
        private readonly behaviorRepository: ForAssessingChildrensBehavior,
        private readonly wishlistRepository: ForGettingWishlists
    ) {}

    chooseToyForChild(childName: Name): Toy | undefined {
        const behavior = this.behaviorRepository.findBehaviorByName(childName);
        const wishlist = this.wishlistRepository.findWishlistByChildName(childName);
        return Santa.selectPresentBasedOnBehavior(wishlist, behavior);
    }

    private static selectPresentBasedOnBehavior(wishlist: Wishlist, behavior: Behavior) {
        switch (behavior) {
            case Behavior.Naughty:
                return wishlist.getThirdChoice();
            case Behavior.Nice:
                return wishlist.getSecondChoice();
            case Behavior.VeryNice:
                return wishlist.getFirstChoice();
        }
    }
}


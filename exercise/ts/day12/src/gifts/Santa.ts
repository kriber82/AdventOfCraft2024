// src/gifts/Santa.ts
import { Toy } from './Toy';
import { Name } from "./Name";
import { Behavior } from "./Behavior";
import { BehaviorRepository } from "./BehaviorRepository";
import { WishlistRepository } from "./WishlistRepository";
import {Wishlist} from "./Wishlist";

export class Santa {
    private readonly behaviorRepository: BehaviorRepository;
    private readonly wishlistRepository: WishlistRepository;

    constructor(
        behaviorRepository: BehaviorRepository,
        wishlistRepository: WishlistRepository
    ) {
        this.behaviorRepository = behaviorRepository;
        this.wishlistRepository = wishlistRepository;
    }

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


// src/gifts/WishlistRepository.ts
import { Wishlist } from './Wishlist';
import { Name } from './Name';

export class WishlistRepository { //TODO this is the adapter for tests, introduce port without setter
    private readonly wishlists: Map<Name, Wishlist> = new Map();

    setWishlist(childName: Name, wishlist: Wishlist): void {
        this.wishlists.set(childName, wishlist);
    }

    findWishlistByChildName(childName: Name): Wishlist {
        const wishlist = this.wishlists.get(childName);

        if (!wishlist) { // TODO write test
            throw new Error('No wishlist found for this child');
        }

        return wishlist;
    }
}


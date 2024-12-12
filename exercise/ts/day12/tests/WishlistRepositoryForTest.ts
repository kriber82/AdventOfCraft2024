// src/gifts/WishlistRepository.ts
import { Name } from '../src/gifts/Name';
import { Wishlist } from '../src/gifts/Wishlist';
import { ForGettingWishlists } from '../src/gifts/ForGettingWishlists';

export class WishlistRepositoryForTest implements ForGettingWishlists {
    private readonly wishlists: Map<Name, Wishlist> = new Map();

    setWishlist(childName: Name, wishlist: Wishlist): void {
        this.wishlists.set(childName, wishlist);
    }

    findWishlistByChildName(childName: Name): Wishlist {
        const wishlist = this.wishlists.get(childName);

        if (!wishlist) {
            throw new Error('No wishlist found for this child');
        }

        return wishlist;
    }
}


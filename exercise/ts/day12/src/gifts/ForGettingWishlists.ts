// src/gifts/ForGettingWishlists.ts
import { Name } from './Name';
import { Wishlist } from './Wishlist';

export interface ForGettingWishlists {
    findWishlistByChildName(childName: Name): Wishlist;
}


import { Behavior } from './Behavior';
import { Wishlist } from './Wishlist';
import { Toy } from './Toy';

export class Desires {
    constructor(public behavior: Behavior, public wishlist: Wishlist) {}

    chooseToy(): Toy | undefined {
        switch (this.behavior) {
            case Behavior.Naughty:
                return this.wishlist.getThirdChoice();
            case Behavior.Nice:
                return this.wishlist.getSecondChoice();
            case Behavior.VeryNice:
                return this.wishlist.getFirstChoice();
            default:
                return undefined;
        }
    }
}

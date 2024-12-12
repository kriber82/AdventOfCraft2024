import { Toy } from './Toy';

export class Child {
    public wishlist: Toy[] = []; //TODO first class collection!

    constructor(public name: string, public behavior: string) { //TODO 3 members

    }

    setWishlist(firstChoice: Toy, secondChoice: Toy, thirdChoice: Toy): void {
        this.wishlist = [firstChoice, secondChoice, thirdChoice];
    }
}
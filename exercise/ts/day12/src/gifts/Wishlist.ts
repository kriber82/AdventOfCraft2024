import { Toy } from './Toy';

export class Wishlist {
    private readonly toys: Toy[] = [];

    constructor (firstChoice: Toy, secondChoice: Toy, thirdChoice: Toy) {
        this.toys = [firstChoice, secondChoice, thirdChoice];
    }

    getFirstChoice(): Toy {
        return this.toys[0]
    }

    getSecondChoice(): Toy {
        return this.toys[1];
    }

    getThirdChoice(): Toy {
        return this.toys[2];
    }
}

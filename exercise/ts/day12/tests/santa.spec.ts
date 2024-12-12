import { Toy } from "../src/gifts/Toy";
import { Behavior } from "../src/gifts/Behavior";
import { SantaBuilder } from "./SantaBuilder";
import { Wishlist } from "../src/gifts/Wishlist";
import { Name } from "../src/gifts/Name";

describe("Santa's gift selection process", () => {
    const Playstation = new Toy('playstation');
    const Ball = new Toy('ball');
    const Plush = new Toy('plush');

    const bobby = new Name('bobby');

    let wishlist: Wishlist = new Wishlist(Playstation, Plush, Ball);

    it('should give the third choice to a naughty child', () => {
        const santa = new SantaBuilder()
            .withChild(bobby, Behavior.Naughty, wishlist)
            .build();

        expect(santa.chooseToyForChild(bobby)).toBe(Ball);
    });

    it('should give the second choice to a nice child', () => {
        const santa = new SantaBuilder()
            .withChild(bobby, Behavior.Nice, wishlist)
            .build();

        expect(santa.chooseToyForChild(bobby)).toBe(Plush);
    });

    it('should give the first choice to a very nice child', () => {
        const santa = new SantaBuilder()
            .withChild(bobby, Behavior.VeryNice, wishlist)
            .build();

        expect(santa.chooseToyForChild(bobby)).toBe(Playstation);
    });

    it('currently returns undefined if a child has an unknown behavior', () => {
        const santa = new SantaBuilder()
            .withChild(bobby, undefined as unknown as Behavior, wishlist)
            .build();

        expect(santa.chooseToyForChild(bobby)).toBe(undefined);
    });

    it('should throw an exception if the child does not exist', () => {
        const santa = new SantaBuilder()
            .withChild(bobby, Behavior.VeryNice, wishlist)
            .build();

        expect(() => santa.chooseToyForChild(new Name('alice'))).toThrowError('No such child found');
    });
});

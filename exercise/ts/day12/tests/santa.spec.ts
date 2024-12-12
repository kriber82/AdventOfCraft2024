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

    it('should throw an exception if the behavior of a child is unknown', () => { //TODO should we assume something instead?
        const santa = new SantaBuilder()
            .withChild(bobby, undefined, wishlist)
            .build();

        expect(() => santa.chooseToyForChild(bobby)).toThrowError('No behavior record found for the child');
    });

    it('should throw an exception if the childs whislist does not exist', () => {
        const santa = new SantaBuilder()
            .withChild(bobby, Behavior.VeryNice, undefined)
            .build();

        expect(() => santa.chooseToyForChild(bobby)).toThrowError('No wishlist found for this child');
    });
});

import {Toy} from "../src/gifts/Toy";
import {Child} from "../src/gifts/Child";
import {Santa} from "../src/gifts/Santa";
import {ChildrenRepository} from "../src/gifts/ChildrenRepository";
import {Behavior} from "../src/gifts/Behavior";
import {Wishlist} from "../src/gifts/Wishlist";
import {Name} from "../src/gifts/Name";

describe("Santa's gift selection process", () => {
    const Playstation = new Toy('playstation');
    const Ball = new Toy('ball');
    const Plush = new Toy('plush');

    const childName = new Name('bobby')

    let childrenRepository: ChildrenRepository;
    let wishlist: Wishlist

    beforeEach(() => {
         childrenRepository = new ChildrenRepository();
         wishlist = new Wishlist(Playstation, Plush, Ball)
    })

    it('should give the third choice to a naughty child', () => {
        const bobby = new Child(childName, Behavior.Naughty, wishlist);
        childrenRepository.addChild(bobby);
        const santa = new Santa(childrenRepository);

        expect(santa.chooseToyForChild(childName)).toBe(Ball);
    });

    it('should give the second choice to a nice child', () => {
        const bobby = new Child(childName, Behavior.Nice, wishlist);
        childrenRepository.addChild(bobby);
        const santa = new Santa(childrenRepository);

        expect(santa.chooseToyForChild(childName)).toBe(Plush);
    });

    it('should give the first choice to a very nice child', () => {
        const bobby = new Child(childName, Behavior.VeryNice, wishlist);
        childrenRepository.addChild(bobby);
        const santa = new Santa(childrenRepository);

        expect(santa.chooseToyForChild(childName)).toBe(Playstation);
    });

    it('currently returns undefined if a child has a unknown behavior', () => {
        const bobby = new Child(childName, undefined, wishlist);
        childrenRepository.addChild(bobby);
        const santa = new Santa(childrenRepository);

        expect(santa.chooseToyForChild(childName)).toBe(undefined);
    });

    it('should throw an exception if the child does not exist', () => {
        const bobby = new Child(childName, Behavior.VeryNice, wishlist);
        childrenRepository.addChild(bobby);
        const santa = new Santa(childrenRepository);

        expect(() => santa.chooseToyForChild(new Name('alice'))).toThrowError('No such child found');
    });
});

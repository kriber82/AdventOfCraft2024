import {Toy} from "../src/gifts/Toy";
import {Child} from "../src/gifts/Child";
import {Santa} from "../src/gifts/Santa";
import {ChildrenRepository} from "../src/gifts/ChildrenRepository";

describe("Santa's gift selection process", () => {
    const Playstation = new Toy('playstation');
    const Ball = new Toy('ball');
    const Plush = new Toy('plush');
    let childrenRepository: ChildrenRepository;

    beforeEach(() => {
         childrenRepository = new ChildrenRepository();
    })

    it('should give the third choice to a naughty child', () => {
        const bobby = new Child('bobby', 'naughty');
        bobby.setWishlist(Playstation, Plush, Ball);

        const santa = new Santa(childrenRepository);
        childrenRepository.addChild(bobby);

        expect(santa.chooseToyForChild('bobby')).toBe(Ball);
    });

    it('should give the second choice to a nice child', () => {
        const bobby = new Child('bobby', 'nice');
        bobby.setWishlist(Playstation, Plush, Ball);

        const santa = new Santa(childrenRepository);
        childrenRepository.addChild(bobby);

        expect(santa.chooseToyForChild('bobby')).toBe(Plush);
    });

    it('should give the first choice to a very nice child', () => {
        const bobby = new Child('bobby', 'very nice');
        bobby.setWishlist(Playstation, Plush, Ball);

        const santa = new Santa(childrenRepository);
        childrenRepository.addChild(bobby);

        expect(santa.chooseToyForChild('bobby')).toBe(Playstation);
    });

    it('currently returns undefined if a child has a unknown behavior', () => {
        const bobby = new Child('bobby', 'quite nice');
        bobby.setWishlist(Playstation, Plush, Ball);

        const santa = new Santa(childrenRepository);
        childrenRepository.addChild(bobby);

        expect(santa.chooseToyForChild('bobby')).toBe(undefined);
    });

    it('should throw an exception if the child does not exist', () => {
        const santa = new Santa(childrenRepository);
        const bobby = new Child('bobby', 'very nice');
        bobby.setWishlist(Playstation, Plush, Ball);

        childrenRepository.addChild(bobby);

        expect(() => santa.chooseToyForChild('alice')).toThrowError('No such child found');
    });
});

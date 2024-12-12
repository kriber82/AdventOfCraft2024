import { Behavior } from './Behavior';
import {Wishlist} from "./Wishlist";
import {Name} from "./Name";

export class Child {
    constructor(public name: Name, public behavior: Behavior, public wishlist: Wishlist) { //TODO 3 members
    }
}
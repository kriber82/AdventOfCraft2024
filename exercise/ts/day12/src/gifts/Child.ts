import { Toy } from './Toy';
import { Behavior } from './Behavior';
import {Wishlist} from "./Wishlist";

export class Child {
    constructor(public name: string, public behavior: Behavior, public wishlist: Wishlist) { //TODO 3 members
    }
}
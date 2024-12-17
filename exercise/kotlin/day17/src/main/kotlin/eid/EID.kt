package eid

import arrow.core.Either

class EID {
    companion object {
        fun parse(): Either<ParsingError, EID> {
            return Either.Right(EID())
        }
    }

}

package eid

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

class EID {
    companion object {
        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensure (eidCandidate.length > 7) {
                    ParsingError.InputTooShort()
                }
                EID()
            }
        }
    }

}

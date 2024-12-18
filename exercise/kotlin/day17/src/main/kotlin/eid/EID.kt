package eid

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

class EID {
    companion object {
        const val validEidLength = 8

        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensure (eidCandidate.length >= validEidLength) {
                    ParsingError.InputTooShort()
                }
                ensure (eidCandidate.length <= validEidLength) {
                    ParsingError.InputTooLong()
                }
                EID()
            }
        }
    }

}

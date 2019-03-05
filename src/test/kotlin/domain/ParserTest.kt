package domain

import org.junit.jupiter.api.Assertions
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ParserTest : Spek({
    describe("Parser test"){

        it("should not return empty list") {
            Assertions.assertTrue(Parser.parseBeers().count() != 0)
        }

    }
})


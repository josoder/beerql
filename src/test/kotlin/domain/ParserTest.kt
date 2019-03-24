package domain

import org.junit.jupiter.api.Assertions

class ParserTest {
    fun `should not return empty list`() {
        Assertions.assertTrue(Parser.parseBeers().count() != 0)
    }
}


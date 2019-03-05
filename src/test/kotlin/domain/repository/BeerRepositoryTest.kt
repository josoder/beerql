package domain.repository

import domain.Parser
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class BeerRepositoryTest {
    private lateinit var beerRepository: BeerRepository

    private fun addTestData() {
        beerRepository.createTable()

        beerRepository.insertList(Parser.parseBeers())
    }

    @Before
    fun setUp() {
        beerRepository = BeerRepository()
    }

    @Test
    fun createTableAndInitDataTest() {
        addTestData()

        transaction {
            assertTrue(Beers.exists())
        }

        assertFalse(beerRepository.getAll().isEmpty())
    }

    @Test
    fun getWithLimitTest() {
        addTestData()

        assertTrue(beerRepository.getAll(limit = 100).count() == 100)
    }
}

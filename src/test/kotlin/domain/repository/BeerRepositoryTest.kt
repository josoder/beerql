package domain.repository

import domain.Parser
import domain.model.Beer
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
        beerRepository.createTable()
    }

    @Test
    fun `should create table and add data`() {
        addTestData()

        transaction {
            assertTrue(Beers.exists())
        }

        assertFalse(beerRepository.getAll().isEmpty())
    }

    @Test
    fun `should get a limited amount of beers`() {
        addTestData()

        assertTrue(beerRepository.getAll(limit = 100).count() == 100)
    }

    @Test
    fun `should create beer and return id`() {
        val newId = beerRepository.createBeer(Beer(name = "test", breweryId = null, categoryId = null))

        assertTrue(newId is Int && newId != 0)
    }

    @Test
    fun `should find beer by id`() {
        val newId = beerRepository.createBeer(Beer(name = "test"))

        val savedEntity = beerRepository.findById(newId!!)

        assertTrue(savedEntity!!.id == newId)
    }
}

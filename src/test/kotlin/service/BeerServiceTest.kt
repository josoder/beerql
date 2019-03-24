package service

import beerql.beerqlModule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject

class BeerServiceTest : KoinComponent {
    @Before
    fun init() {
        startKoin {
            modules(beerqlModule)
        }
    }

    @Test
    fun `initBeers`() {
        val service by inject<BeerService>()

        assertTrue(service.getAllBeers().isEmpty())

        service.importBeers()

        assertTrue(service.getAllBeers().count() > 0)
    }

}

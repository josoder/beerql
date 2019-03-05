package service

import domain.repository.BeerRepository
import domain.Parser
import domain.model.Beer
import org.koin.core.KoinComponent

class BeerService(val beerRepository: BeerRepository) : KoinComponent {

    fun importBeers() {
        val beers = Parser.parseBeers()
        beerRepository.createTable()
        beerRepository.insertList(beers)
    }

    fun getAllBeers(): List<Beer> {
        return beerRepository.getAll()
    }
}
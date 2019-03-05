package domain.repository

import domain.model.Beer
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent

object Beers : IntIdTable() {
    val sequelId = integer("sequel_id").uniqueIndex()
    val breweryId = integer("brewery_id")
    val categoryId = integer("category_id")
    val name = varchar("name", 100)
    val description = text("description")
    val alcoholPercentage = float("alc_per")
}

class BeerRepository : KoinComponent {
    companion object {
        val db = Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    }

    fun createTable() {
        transaction {
            if (!Beers.exists()) SchemaUtils.create(Beers)
        }
    }

    fun insertList(beerList: List<Beer>): List<ResultRow> {
        var ids = emptyList<ResultRow>()

        transaction {
            ids = Beers.batchInsert(beerList) { beer ->
                this[Beers.sequelId] = beer.id!!
                this[Beers.breweryId] = beer.categoryId!!
                this[Beers.categoryId] = beer.categoryId!!
                this[Beers.name] = beer.name!!
                this[Beers.description] = beer.description!!
                this[Beers.alcoholPercentage] = beer.alcoholPercentage!!
            }
        }
        return ids
    }

    fun iterate() {
        transaction {
            Beers.selectAll().forEach {
                println(it[Beers.name])
            }
        }
    }

    fun getAll(limit: Int? = null): List<Beer> {
        var allBeers = emptyList<Beer>()

        if (limit != null) {
            allBeers = getWithLimit(limit)
            return allBeers
        }

        transaction {
            allBeers = if (!Beers.exists()) {
                emptyList()
            } else {
                Beers.selectAll().map { convertToBeer(it) }
            }
        }

        return allBeers
    }

    private fun getWithLimit(size: Int): List<Beer> {
        var beers = emptyList<Beer>()

        transaction {
            beers = if (!Beers.exists()) {
                emptyList()
            } else {
                Beers.selectAll()
                    .limit(size)
                    .sortedBy { Beers.name }
                    .map { convertToBeer(it) }
            }
        }
        return beers
    }

    private fun convertToBeer(resultRow: ResultRow): Beer {
        return Beer(
            id = resultRow[Beers.sequelId],
            breweryId = resultRow[Beers.breweryId],
            name = resultRow[Beers.name],
            categoryId = resultRow[Beers.categoryId],
            description = resultRow[Beers.description],
            alcoholPercentage = resultRow[Beers.alcoholPercentage]
        )
    }
}
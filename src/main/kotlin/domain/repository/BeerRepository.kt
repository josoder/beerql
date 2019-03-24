package domain.repository

import domain.model.Beer
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent

object Beers : IntIdTable() {
    val sequelId = integer("sequel_id").autoIncrement().primaryKey()
    val csv = integer("csv_id").nullable()
    val breweryId = integer("brewery_id").nullable()
    val categoryId = integer("category_id").nullable()
    val name = varchar("name", 100)
    val description = text("description").nullable()
    val alcoholPercentage = float("alc_per").nullable()
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
                this[Beers.csv] = beer.csv!!
                this[Beers.breweryId] = beer.breweryId
                this[Beers.categoryId] = beer.categoryId
                this[Beers.name] = beer.name!!
                this[Beers.description] = beer.description
                this[Beers.alcoholPercentage] = beer.alcoholPercentage
            }
        }
        return ids
    }

    fun createBeer(beer: Beer): Int? {
        var id: Int? = null

        transaction {
            id = Beers.insertAndGetId {
                it[Beers.breweryId] = beer.categoryId
                it[Beers.categoryId] = beer.categoryId
                it[Beers.name] = beer.name!!
                it[Beers.description] = beer.description
                it[Beers.alcoholPercentage] = beer.alcoholPercentage
            }.value
        }

        return id
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

    fun findById(beerId: Int): Beer? {
        var beer: Beer? = null

        transaction {
            val res = Beers.select {
                Beers.sequelId.eq(beerId)
            }.firstOrNull()

            if (res != null) beer = convertToBeer(res)
        }

        return beer
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

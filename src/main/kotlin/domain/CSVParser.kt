package domain

import com.opencsv.CSVReader
import domain.model.Beer
import domain.repository.BeerRepository
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.collections.ArrayList

class Parser {
    companion object {
        fun parseBeers(): List<Beer> {
            val beers = ArrayList<Beer>()

            try {
                val fileReader = BufferedReader(FileReader("csv/beers.csv"))
                val csvReader = CSVReader(fileReader)
                // skip headers
                csvReader.readNext()

                var record = csvReader.readNext()

                while (record != null) {
                    beers.add(Beer.convertFromString(record))
                    record = csvReader.readNext()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return beers
        }
    }
}

fun main() {
    val repo = BeerRepository()
    repo.createTable()

    val beers = Parser.parseBeers()

    repo.insertList(beers)

    repo.getAll().forEach { println(it) }

}


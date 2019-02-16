package domain

import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

private val ID = 0
private val BREWERYID = 1
private val NAME = 2
private val CATEGORY_ID = 3
private val ALCOHOL_PERCENTAGE = 5
private val DESCRIPTION = 10

data class Beer(
    var id: Int? = null,
    var breweryId: Int? = null,
    var name: String? = null,
    var categoryId: Int? = null,
    var description: String? = null,
    var alcoholPercentage: Float? = null
) {
    companion object {
        fun convertFromString(beer: Array<String>): Beer {
            val newBeer = Beer(name = beer[NAME], description = beer[DESCRIPTION])
            newBeer.id = beer[ID].toInt()
            newBeer.breweryId = beer[BREWERYID].toInt()
            newBeer.alcoholPercentage = beer[ALCOHOL_PERCENTAGE].toFloat()
            newBeer.categoryId = beer[CATEGORY_ID].toInt()
            return newBeer
        }
    }
}

fun main() {
    try {
        val fileReader = BufferedReader(FileReader("csv/beers.csv"))
        val csvReader = CSVReader(fileReader)
        // skip headers
        csvReader.readNext()

        var record = csvReader.readNext()
        val beers = ArrayList<Beer>()

        while (record != null) {
            beers.add(Beer.convertFromString(record))
            record = csvReader.readNext()
        }

    } catch (e: IOException) {
        e.printStackTrace()
    }
}
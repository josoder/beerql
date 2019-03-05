package domain.model

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

        val ID = 0
        val BREWERYID = 1
        val NAME = 2
        val CATEGORY_ID = 3
        val ALCOHOL_PERCENTAGE = 5
        val DESCRIPTION = 10

    }
}

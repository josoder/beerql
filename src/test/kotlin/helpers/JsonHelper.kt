package helpers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import domain.model.Beer
import java.io.IOException

data class KotlinGQLObject(
    var id: Int? = null,
    var name: String? = null
)


class JsonHelper {
    companion object {
        val objectMapper = ObjectMapper()
            .registerModule(KotlinModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)


        fun convertJsonArrayToBeers(json: String): List<Beer> {
            var objectList = emptyList<Beer>()

            try {
                val objects = objectMapper.readValue<List<Beer>>(json)

                objectList = objects

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return objectList
        }

        fun convertGQLJsonToBeers(json: String): List<Beer> {
            val objectMapper = ObjectMapper()
                .registerModule(KotlinModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

            var objectList = emptyList<Beer>()

            try {
                val jsonArray = objectMapper.readTree(json)["data"]["beers"]

                val objects = convertJsonArrayToBeers(jsonArray.toString())

                objectList = objects

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return objectList
        }
    }
}
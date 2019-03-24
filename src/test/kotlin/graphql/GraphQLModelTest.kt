package graphql

import domain.Parser
import domain.repository.BeerRepository
import helpers.JsonHelper
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.function.Executable
import kotlin.test.assertFalse

class GraphQLModelTest {

    companion object {
        private lateinit var beerRepository: BeerRepository

        @BeforeClass
        @JvmStatic
        fun init() {
            beerRepository = BeerRepository()
            beerRepository.createTable()
            beerRepository.insertList(Parser.parseBeers())

        }
    }

    @Test
    fun `should get all beers`() {
        val graphQLModel = GraphQLModel(beerRepository)

        val jsonString = graphQLModel.schema.execute("{beers(){id, name}}")
        val objectList = JsonHelper.convertGQLJsonToBeers(jsonString)

        assertTrue(objectList.size == 20)
        assertTrue(objectList[0].description == null && objectList[0].breweryId == null)
        assertFalse(objectList[0].id == null && objectList[0].name == null)
    }

    @Test
    fun `should set the limit to the size variable`() {
        val graphQLModel = GraphQLModel(beerRepository)

        val json = graphQLModel.schema.execute("{beers(size: 100){id, name}}")

        val objects = JsonHelper.convertGQLJsonToBeers(json)

        assertTrue(objects.size == 100)
    }

    @Test
    fun `should get beer with id`() {
        val graphQLModel = GraphQLModel(beerRepository)

        val json = graphQLModel.schema.execute("{beer(id: 1){id, name}}")

        val res = JsonHelper.convertGQLJsonToBeer(json)

        assertAll("beer(id: 1)",
            Executable { assertTrue(res != null) },
            Executable { assertTrue(res!!.id == 1) }
        )
    }


}

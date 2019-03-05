package graphql

import domain.repository.BeerRepository
import domain.Parser
import helpers.JsonHelper
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
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
    fun testGetAllBeers() {
        val graphQLModel = GraphQLModel(beerRepository)

        val jsonString = graphQLModel.schema.execute("{beers(){id, name}}")
        val objectList = JsonHelper.convertGQLJsonToBeers(jsonString)

        assertTrue(objectList.size == 20)
        assertTrue(objectList[0].description == null && objectList[0].breweryId == null)
        assertFalse(objectList[0].id == null && objectList[0].name == null)
    }

    @Test
    fun getAllWithLimit() {
        val graphQLModel = GraphQLModel(beerRepository)

        val json = graphQLModel.schema.execute("{beers(size: 100){id, name}}")

        val objects = JsonHelper.convertGQLJsonToBeers(json)

        assertTrue(objects.size == 100)
    }



}

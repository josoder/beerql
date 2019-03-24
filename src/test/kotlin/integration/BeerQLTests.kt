package integration

import beerql.beerql
import helpers.JsonHelper
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class BeerQLITTest {
    @Test
    fun testInit() = withTestApplication(Application::beerql) {
        with(handleRequest(HttpMethod.Get, "/api/beers")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue(JsonHelper.convertJsonArrayToBeers(response.content!!).isEmpty())
        }

        with(handleRequest(HttpMethod.Get, "/api/beers/init")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }

        with(handleRequest(HttpMethod.Get, "/api/beers")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertFalse(JsonHelper.convertJsonArrayToBeers(response.content!!).isEmpty())
        }
    }
}

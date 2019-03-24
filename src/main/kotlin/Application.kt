package beerql

import com.fasterxml.jackson.databind.DeserializationFeature
import domain.repository.BeerRepository
import graphql.GraphQLModel
import io.ktor.application.Application
import io.ktor.application.ApplicationStarted
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import routes.beersRoute
import routes.graphQLRoute
import service.BeerService

// Register coin DP module
val beerqlModule = module {
    factory { BeerRepository() }
    factory { BeerService(get()) }
    single { GraphQLModel(get()) }
}


/**
 *
 * An Application instance is the main unit of a Ktor Application. When a request comes in (a request can be HTTP,
 * HTTP/2 or WebSocket requests), it is converted to an ApplicationCall and goes through a pipeline which is owned by
 * the Application. The pipeline consists of one or more interceptors that are previously installed, providing certain
 * functionality such as routing, compression, etc. that ends handling the request.
 * Normally, a Ktor program configures the Application pipeline through modules that install and configure features.
 *
 * Application.beerql defines a module, A Ktor module is just a user-defined function receiving the Application class
 * that is in charge of configuring the server pipeline, install features, registering routes, handling requests, etc.
 *
 * From the official ktor docs: https://ktor.io/servers/application.html
 */
fun Application.beerql() {
    /**
     * install ktor features
     * All the standard available features described here: https://ktor.io/servers/features.html
     */
    install(CallLogging)
    install(DefaultHeaders)
    /**
     * Adds dependency injection with the beerqlModule defined above
     */
    installKoin {
        logger()
        modules(beerqlModule)
    }
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
    install(CORS)
    {
        allowSameOrigin
        anyHost()
    }
    /**
     * Register all available routes for the application.
     * Split up into multiple files for scalability.
     */
    routing {
        graphQLRoute()
        beersRoute()
    }
}

/**
 * Entry point, as specified in application.conf
 * Initialize database with content and call Application.beerql() which will install dependencies(features in ktor lang)
 */
fun Application.main() {
    // initialize database and add the data from beers.csv
    environment.monitor.subscribe(ApplicationStarted) {
        val beerService by inject<BeerService>()
        beerService.importBeers()
    }

    beerql()
}


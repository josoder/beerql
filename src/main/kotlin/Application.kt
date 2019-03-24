import com.fasterxml.jackson.databind.DeserializationFeature
import domain.repository.BeerRepository
import graphql.GraphQLModel
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module
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

fun Application.main() {
    /**
     * install ktor features
     * All the standard available features described here: https://ktor.io/servers/features.html
     */
    install(CallLogging)
    install(DefaultHeaders)
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
    install(Routing) {
        graphQLRoute()
        beersRoute()
    }
}

fun main() {
    // start embedded server using netty as backend and wait for connections
    embeddedServer(Netty, 8080, module = Application::main).start(wait = true)
}

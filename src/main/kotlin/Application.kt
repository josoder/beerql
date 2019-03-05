import com.fasterxml.jackson.databind.DeserializationFeature
import domain.repository.BeerRepository
import graphql.GraphQLModel
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.ext.installKoin
import routes.GraphQlRequest
import service.BeerService


val beerqlModule = module {
    factory { BeerRepository() }
    factory { BeerService(get()) }
    single { GraphQLModel(get()) }
}

fun Application.main() {
    install(DefaultHeaders)
    install(ContentNegotiation) {
        jackson {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
    installKoin {
        logger()
        modules(beerqlModule)
    }
    install(CallLogging)
    install(Routing) {
        val graphQLModel by inject<GraphQLModel>()
        val beerService by inject<BeerService>()

        get("/") {
            call.respondText("Hello world", ContentType.Application.Json)
        }

        post("/beerql") {

            val req = call.receive<GraphQlRequest>()
            log.info(req.toString())

            call.respondText(graphQLModel.schema.execute(req.query), ContentType.Application.Json)
        }

        get("/init") {
            beerService.importBeers()
            call.respondText("beers successfully imported", ContentType.Application.Json)
        }

        get("/beers") {
            call.respond(beerService.getAllBeers())
        }
    }

}

fun main() {
    embeddedServer(Netty,8080, module = Application::main).start()
}

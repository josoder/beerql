package routes

import domain.model.Beer
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import org.koin.ktor.ext.inject
import service.BeerService

fun Routing.beersRoute() {
    val beerService by inject<BeerService>()


    get("api/beers/init") {
        beerService.importBeers()
        call.respondText("beers successfully imported", ContentType.Application.Json)
    }

    get("api/beers") {
        call.respond(beerService.getAllBeers())
    }

    post("api/beers") {
        val beer = call.receive<Beer>()

        val id = beerService.createBeer(beer)

        if (id != null) {
            call.respond(HttpStatusCode.Created, id)
        } else {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}


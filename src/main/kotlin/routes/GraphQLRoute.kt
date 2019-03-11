package routes

import graphql.GraphQLModel
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import org.koin.ktor.ext.inject


fun Routing.beerql() {
    val graphQLModel by inject<GraphQLModel>()

    post("/beerql") {
        val req = call.receive<GraphQlRequest>()

        call.respondText(
            graphQLModel.schema.execute(req.query), ContentType.Application.Json)
    }
}


data class GraphQlRequest(val query: String)

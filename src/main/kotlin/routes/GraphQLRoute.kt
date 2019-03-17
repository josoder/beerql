package routes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.GraphQLModel
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.post
import org.koin.ktor.ext.inject


fun Routing.graphQLRoute() {
    val graphQLModel by inject<GraphQLModel>()

    post("/beerql") {
        val req = call.receive<GraphQlRequest>()
        println(req)
        val variables = jacksonObjectMapper().writeValueAsString(req.variables)
        call.respondText(
            graphQLModel.schema.execute(req.query, variables), ContentType.Application.Json)
    }
}


data class GraphQlRequest(val query: String, val variables: Map<String, Any> = emptyMap())


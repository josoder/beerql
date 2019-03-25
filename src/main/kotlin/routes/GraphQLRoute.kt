package routes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import graphql.GraphQLModel
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.post
import org.koin.ktor.ext.inject


data class GraphQlRequest(val query: String = "", val variables: Map<String, Any>? = emptyMap())

/**
 * A typical graphql request will contain a query string with the actual query which we can represent as a String
 * in kotlin.
 *
 * It will also contain an optional JSON object with variables. Which we will represent as a nullable
 *  Map<String, Any> in kotlin.
 *
 *  req: {
 *  query: '{
 *  field(arg: "value") {
 *      contentField1,
 *      contentField2
 *
 *  }',
 *  variables: { var1: val1 }
 *  }
 *
 */
fun Route.graphQLRoute() {
    val graphQLModel by inject<GraphQLModel>()

    post("/beerql") {
        // parse json as a GraphQlRequest
        val req = call.receive<GraphQlRequest>()

        println(req)

        val variables = if (req.variables == null) null
        else jacksonObjectMapper().writeValueAsString(req.variables)

        var result = ""

        try {
            result = graphQLModel.schema.execute(req.query, variables)
        } catch (e: Exception) {
            // return the exception message as json
            call.respondText(
                """{"error": "${e.message}"}""", ContentType.Application.Json,
                HttpStatusCode.InternalServerError
            )
        }

        call.respondText(result, ContentType.Application.Json, HttpStatusCode.Created)
    }
}



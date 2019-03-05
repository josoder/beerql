package ktor

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.jackson.jackson
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

val server = embeddedServer(Netty, port = 8080) {
    install(DefaultHeaders)
    install(ContentNegotiation) {
        jackson {  }
    }
    install(CallLogging)
    install(Routing)  {
        get("/init") {

            call.respondText("", ContentType.Application.Json)
        }
    }
}
package graphql

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.pgutkowski.kgraphql.KGraphQL
import domain.model.Beer
import domain.repository.BeerRepository
import org.koin.core.KoinComponent
import org.koin.core.get

class GraphQLModel(private val beerRepository: BeerRepository) : KoinComponent {


    val schema = KGraphQL.schema {
        configure {
            useDefaultPrettyPrinter = true
            objectMapper = jacksonObjectMapper()
        }

        query("beers") {
            resolver { size: Int -> beerRepository.getAll(size) }
                .withArgs {
                    arg<Int> { name = "size"; defaultValue = 20 }
                }
        }

        query("beer") {
            resolver { id: Int -> beerRepository.findById(id) }
                .withArgs {
                    arg<Int> { name = "id" }
                }
        }

        // bug in graphiql requires schema to contain mutation definition
        mutation("placeholder") {
            resolver { -> listOf(Beer()) }
        }

        type<Beer>()
    }
}

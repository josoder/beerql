# beerql

beerql is a simple web application using native kotlin only (except for junit for testing) 

It uses [ktor](https://ktor.io/") web framework to serve the API, <br>
[exposed](https://github.com/JetBrains/Exposed) with h2 to store the data, <br>
[Koin](https://github.com/InsertKoinIO/koin) for dependency injection, <br>
and [Kgraphql](https://github.com/pgutkowski/KGraphQL) to implement graphql. 


# Ktor
Ktor is built using gradle and hosted with an embedded Netty server. <br>

Ktor is started with the following line in [build.gradle](build.gradle)

<code> application {
    mainClassName = "io.ktor.server.netty.EngineMain"
} </code>


As explained by the official docs the EngineMain will pick up the configuration file [main/resources](
src/main/resources/application.conf)

From the official docs:

<i>When Ktor is started using a EngineMain, or by calling the commandLineEnvironment, it tries to load a HOCON file called
application.conf from the application resources. You can change the location of the file using command line arguments.
(https://ktor.io/servers/configuration.html#available-config). </i>

The application.conf file specifies which module(s) to install and the entry-point of the application launch.
the beerql module is defined in [Application.kt](src/main/kotlin/Application.kt).

More information about the Ktor lifecycle can be found [here](https://ktor.io/servers/lifecycle.html). 


# Setup
<li> <code> ./gradlew run </code>

### or run with docker:
<li> <code>./gradlew build</code>
<li> <code>docker build -t beerql .</code>
<li> <code>docker run -p 8080:8080 --rm beerql</code>


<br>

http://localhost:8080/api/beerql exposes the graphql root

Use graphiql standalone or the chrome extension in the link below to explore the content 

https://chrome.google.com/webstore/detail/chromeiql/fkkiamalmpiidkljmicmjfbieiclmeij

Currently there is only 2 queries defined beers(size:Int) and beer(id:Int)



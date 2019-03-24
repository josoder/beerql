# beerql

beerql is a simple application using native kotlin only to serve graphql. 

It uses [ktor](https://ktor.io/") to expose the API, <br>
[exposed](https://github.com/JetBrains/Exposed) with h2 to store the data, <br>
[Koin](https://github.com/InsertKoinIO/koin) for dependency injection, <br>
and [Kgraphql](https://github.com/pgutkowski/KGraphQL) to implement graphql. 

# Setup
<li> ./gradlew run
<li> go to localhost:8080/api/init (will parse the content of csv/beers.csv into h2)
<li> localhost:8080/api/beerql exposes the graphql root

Use graphiql standalone or the chrome extension in the link below to explore the content 

https://chrome.google.com/webstore/detail/chromeiql/fkkiamalmpiidkljmicmjfbieiclmeij


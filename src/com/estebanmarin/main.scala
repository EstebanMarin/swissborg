package com.estebanmarin

import cats.effect.*
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import com.estebanmarin.algebras.http.HttpClient
import com.estebanmarin.algebras.models.*
import com.estebanmarin.algebras.*

// now that we have the data, we will
// Use the Bellman-Ford algorithm to detect negative weight cycles
// (in the logarithmic space, a negative weight cycle corresponds to an arbitrage opportunity).
// To detect arbitrage opportunities, you need to find cycles in the graph of currency pairs where the product of the exchange rates in the cycle is greater than 1.
// This can be done using graph algorithms like the Bellman-Ford algorithm.

object ArbitragePuzzle extends IOApp.Simple:
  // Create an HttpClient resource
  def clientResource: Resource[IO, Client[IO]] =
    EmberClientBuilder.default[IO].build

  def run: IO[Unit] =
    clientResource.use { client =>
      // instantiate algebras
      val httpClient: HttpClient[IO] = HttpClient.impl[IO]
      val graphDataStructure: GraphDataStructure[IO] =
        GraphDataStructure.impl[IO]
      val bellmanFordAlg: BellmanFordAlg[IO] = BellmanFordAlg.impl[IO]
      for
        rateData: APIResponse <- httpClient.getRates(client)
        edges: Map[fromTokenToToken, RLogarithmicScale] <-
          graphDataStructure.createEdgesOfGraphLogarithmic(rateData.rates)
        graph: GraphLogarithmicSpace <-
          graphDataStructure.createGraphfromRates(rateData.rates)
        _ <- bellmanFordAlg.bellmanFordAlg(
          graph,
          edges,
          ???,
          ???
        ) // run the algorithm
        _ <- bellmanFordAlg.bellmanFordAlg(
          graph,
          rateData.rates,
          ???,
          ???
        ) // run the algorithm
        _ <- IO.println(edges)
      yield ()

    }

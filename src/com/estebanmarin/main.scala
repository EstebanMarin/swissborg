package com.estebanmarin

import cats.effect.*
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import com.estebanmarin.algebras.http.HttpClient
import com.estebanmarin.algebras.GraphDataStructure
import com.estebanmarin.algebras.models.*

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
      for
        rateData: APIResponse <- httpClient.getRates(client)
        graph: Graph <- GraphDataStructure.createGraphfromRates(rateData.rates)
        _ <- IO.println(rateData.rates)
      // _ <- IO.println(graph.toString())
      yield ()

    }

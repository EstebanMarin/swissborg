package com.estebanmarin

import cats.effect.*
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import com.estebanmarin.algebras.http.HttpClient

object ArbitragePuzzle extends IOApp.Simple:
  // Create an HttpClient resource
  def clientResource: Resource[IO, Client[IO]] =
    EmberClientBuilder.default[IO].build

  def run: IO[Unit] =
    clientResource.use { client =>
      // instantiate algebras
      val httpClient: HttpClient[IO] = HttpClient.impl[IO]
      for 
        rates <- httpClient.getRates(client)
        _ <- IO.println(rates)  
      yield ()

    }

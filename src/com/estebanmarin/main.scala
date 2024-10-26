package com.estebanmarin

import cats.effect.*
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits._
import org.http4s.Method._
import org.http4s.Request
import org.http4s.headers.Accept
import org.http4s.MediaType

// I am starting to use the new IOApp.Simple
// I will post to the api to get things rolling
// "https://api.swissborg.io/v1/challenge/rates"
// Lets create a http4s client

object Hello extends IOApp.Simple:
  // Create an HttpClient resource
  def clientResource: Resource[IO, Client[IO]] =
    EmberClientBuilder.default[IO].build
  // Method to perform a GET request
  def getRates(client: Client[IO]): IO[Unit] =
    val request =
      Request[IO](GET, uri"https://api.swissborg.io/v1/challenge/rates")
        .withHeaders(Accept(MediaType.application.json))
    client.expect[String](request).flatMap(response => IO.println(response))

  // Use the client resource within the run method
  def run: IO[Unit] =
    clientResource.use { client =>
      getRates(client)
    }

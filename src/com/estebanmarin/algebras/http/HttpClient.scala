package com.estebanmarin.algebras.http

import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.Client
import org.http4s.implicits.*
import org.http4s.Method.*
import org.http4s.Request
import org.http4s.headers.Accept
import org.http4s.MediaType
import com.estebanmarin.algebras.models.Rate
import org.http4s.circe.*
import org.http4s.*



trait HttpClient[F[_]]:
  def getRates(client: Client[F]): F[String]
  def getRates2(client: Client[F]): F[List[Rate]]

object HttpClient:

  def impl[F[_]: Async]: HttpClient[F] = new HttpClient[F]:
    def getRates(client: Client[F]): F[String] =
      val request =
        Request[F](GET, uri"https://api.swissborg.io/v1/challenge/rates")
          .withHeaders(Accept(MediaType.application.json))
      client.expect[String](request).flatMap(response => response.pure[F])

    // given EntityDecoder[F, Token] = ???
    def getRates2(client: Client[F]): F[List[Rate]] = 
        val request = 
            Request[F](GET, uri"https://api.swissborg.io/v1/challenge/rates")
            .withHeaders(Accept(MediaType.application.json))
        client.expect[List[Rate]](request)
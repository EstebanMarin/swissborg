package com.estebanmarin.algebras.http

import cats.effect.*
import cats.syntax.all.*
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits._
import org.http4s.Method._
import org.http4s.Request
import org.http4s.headers.Accept
import org.http4s.MediaType

trait HttpClient[F[_]]:
  def getRates(client: Client[F]): F[String]

object HttpClient:

  def impl[F[_]: Async]: HttpClient[F] = new HttpClient[F]:
    def getRates(client: Client[F]): F[String] =
      val request =
        Request[F](GET, uri"https://api.swissborg.io/v1/challenge/rates")
          .withHeaders(Accept(MediaType.application.json))
      client.expect[String](request).flatMap(response => response.toString().pure[F])
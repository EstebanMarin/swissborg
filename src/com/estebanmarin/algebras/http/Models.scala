package com.estebanmarin.algebras.models

import io.circe.*
import org.http4s.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.EntityDecoder
import cats.effect.kernel.Async

// Double probably might not be the best choice for a value, but it's just an example
final case class Token(name: String) derives Codec.AsObject
final case class Rate(name: Token, value: Double) derives Codec.AsObject
final case class APIResponse(rates: Map[String, String]) derives Codec.AsObject
object APIResponse:
    given Codec[APIResponse] = deriveCodec[APIResponse] 
    given [F[_]: Async]: EntityDecoder[F, APIResponse] = jsonOf[F, APIResponse]
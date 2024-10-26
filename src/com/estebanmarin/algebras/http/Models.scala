package com.estebanmarin.algebras.models

import io.circe.*
import org.http4s.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.EntityDecoder
import cats.effect.kernel.Async

// Double probably might not be the best choice for a value, but it's just an example
// final case class Token(name: String) derives Codec.AsObject
// final case class Rate(value: Double) derives Codec.AsObject
final case class APIResponse(rates: Map[String, String]) derives Codec.AsObject
object APIResponse:
  given Codec[APIResponse] = deriveCodec[APIResponse]
  given [F[_]: Async]: EntityDecoder[F, APIResponse] = jsonOf[F, APIResponse]


type Token = String
type Rate = Double
type fromTokenToToken = (Token, Token)

final case class APIResponseRefactor(rates: Map[fromTokenToToken, Rate])
object APIResponseRefactor:
  given Decoder[fromTokenToToken] = Decoder.decodeString.emap { str =>
    str.split("-") match
      case Array(from, to) => Right((from, to))
      case _ => Left("Invalid token pair format")
  }

  given Encoder[fromTokenToToken] = Encoder.encodeString.contramap {
    case (from, to) => s"$from-$to"
  }

  given Codec[APIResponseRefactor] = ???
  given [F[_]: Async]: EntityDecoder[F, APIResponseRefactor] = jsonOf[F, APIResponseRefactor]
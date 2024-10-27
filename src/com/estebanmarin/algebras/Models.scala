package com.estebanmarin.algebras.models

import io.circe.*
import org.http4s.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.EntityDecoder
import cats.effect.kernel.Async
import scala.CanEqual.derived
import cats.Show

type Token = String
type Rate = Double
type RLogarithmicScale = Double
type fromTokenToToken = (Token, Token)
type Weight = (Rate, RLogarithmicScale)
type GraphLogarithmicSpace = Map[Token, Map[Token, Weight]]

final case class APIResponse(rates: Map[fromTokenToToken, Rate])
object APIResponse:
  given KeyDecoder[fromTokenToToken] = KeyDecoder.decodeKeyString.map { str =>
    str.split("-") match
      case Array(from, to) => (from, to)
      case _ => throw new IllegalArgumentException("Invalid token pair format")
  }

  given KeyEncoder[fromTokenToToken] = KeyEncoder.encodeKeyString.contramap {
    case (from, to) => s"$from-$to"
  }

  given Decoder[fromTokenToToken] = Decoder.decodeString.emap { str =>
    str.split("-") match
      case Array(from, to) => Right((from, to))
      case _               => Left("Invalid token pair format")
  }

  given Encoder[fromTokenToToken] = Encoder.encodeString.contramap {
    case (from, to) => s"$from-$to"
  }

  given Decoder[APIResponse] = Decoder.instance { cursor =>
    for rates <- cursor.downField("rates").as[Map[fromTokenToToken, Rate]]
    yield APIResponse(rates)
  }

  given Encoder[APIResponse] = Encoder.instance { response =>
    Json.obj(
      "rates" -> response.rates.asJson
    )
  }

  given [F[_]: Async]: EntityDecoder[F, APIResponse] = jsonOf[F, APIResponse]

package com.estebanmarin.algebras.models

import io.circe.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.EntityDecoder

// Double probably might not be the best choice for a value, but it's just an example
final case class Token(name: String) derives Decoder, Encoder
final case class Rate(name: Token, value: Double) derives Decoder, Encoder

// object Token:
//     given Codec[Token] = deriveCodec[Token]
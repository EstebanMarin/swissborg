package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.IO

object GraphDataStructure:
  def createGraphfromRates(rates: Map[fromTokenToToken, Rate]): IO[Graph] = IO {
    rates.foldLeft(Map.empty[Token, Map[Token, Rate]]) {
      case (graph, ((from, to), rate)) =>
        val updatedMap = graph.getOrElse(from, Map.empty) + (to -> rate)
        graph + (from -> updatedMap)
    }
  }

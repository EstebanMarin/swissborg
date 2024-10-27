package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.IO
import cats.Monad
import cats.implicits.given

trait GraphDataStructure[F[_]]:
  def createGraphfromRates(rates: Map[fromTokenToToken, Rate]): F[Graph]
  def printGraph(graph: Graph): F[String]
object GraphDataStructure:
  def impl[F[_]: Monad]: GraphDataStructure[F] = new GraphDataStructure[F]:
    def createGraphfromRates(rates: Map[fromTokenToToken, Rate]): F[Graph] = 
      val graphs = rates.foldLeft(Map.empty[Token, Map[Token, Rate]]) {
        case (graph, ((from, to), rate)) =>
          val updatedMap = graph.getOrElse(from, Map.empty) + (to -> rate)
          graph + (from -> updatedMap)
      }
      graphs.pure[F]

    def printGraph(graph: Graph): F[String] = 
      graph.toString().pure[F]
    
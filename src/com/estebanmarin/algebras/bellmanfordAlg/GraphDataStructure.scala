package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.IO
import cats.Monad
import cats.implicits.given

trait GraphDataStructure[F[_]]:
  def createUniqueVertices(
      rates: Map[fromTokenToToken, Rate]
  ): F[Map[Token, Double]]
  def createEdgesOfGraphLogarithmic(
      rates: Map[fromTokenToToken, Rate]
  ): F[Map[fromTokenToToken, RLogarithmicScale]]
  def createGraphfromRates(
      rates: Map[fromTokenToToken, Rate]
  ): F[GraphLogarithmicSpace]
  def printGraph(graph: GraphLogarithmicSpace): F[String]
object GraphDataStructure:
  def impl[F[_]: Monad]: GraphDataStructure[F] = new GraphDataStructure[F]:
    def createUniqueVertices(
        rates: Map[fromTokenToToken, Rate]
    ): F[Map[Token, Double]] =
      rates.keys
        .foldLeft(Map.empty[Token, Double]) { case (vertices, (from, to)) =>
          vertices + (from -> Double.PositiveInfinity)
        }
        .toSet
        .toMap
        .pure[F]
    def createEdgesOfGraphLogarithmic(
        rates: Map[fromTokenToToken, Rate]
    ): F[Map[fromTokenToToken, RLogarithmicScale]] =
      rates
        .map { case ((from, to), rate) =>
          val negativeLogarithmicSpace =
            (-1) * math.log10(rate)
          ((from, to), negativeLogarithmicSpace)
        }
        .pure[F]
    def createGraphfromRates(
        rates: Map[fromTokenToToken, Rate]
    ): F[GraphLogarithmicSpace] =
      val graphs = rates.foldLeft(Map.empty[Token, Map[Token, Weight]]) {
        case (graph, ((from, to), rate)) =>
          val negativeLogarithmicSpace =
            (-1) * math.log10(rate)
          val weight = (rate, negativeLogarithmicSpace)
          val updatedMap = graph.getOrElse(from, Map.empty) + (to -> weight)
          graph + (from -> updatedMap)
      }
      graphs.pure[F]

    def printGraph(graph: GraphLogarithmicSpace): F[String] =
      graph.toString().pure[F]

package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.*
import cats.implicits.given

// using mutable data structures for performance reasons
// since we are could with a large amount of data
// we are using a principle of localized functional programming
// as long as we con control the side effects and ensure that no data is shared

import scala.collection.mutable
import scala.annotation.tailrec

// step 0.1 lets improve the modeling of the problem
// step 1.0 lets build the graph from the data and build a suitable data structure to represent the graph
// step 1.1 lets develop the algorithm
// step 2 lets develop the logarithmic transformation to detect arbitrage opportunities

// Algorithm Bellman-Ford algorithm to detect negative weight cycles
// in the logarithmic space, a negative weight cycle corresponds to an arbitrage opportunity.
// 1. Initialize distances and predecessors.
// 2. Relax edges repeatedly.
// 3. Check for negative weight cycles.
// 4. Return the result in the effect type F.

trait BellmanFordAlg[F[_]]:
  def bellmanFordAlg(
      graph: GraphLogarithmicSpace,
      edges: Map[fromTokenToToken, RLogarithmicScale],
      uniqueVertices: Map[Token, Double],
      idStartingVertex: Token
  ): F[Map[Token, RLogarithmicScale]]
  def negativeCycles(
      graph: GraphLogarithmicSpace,
      knowOptimalDistance: Map[Token, RLogarithmicScale],
      predecessors: Map[Token, Token],
      iterations: Int
  ): F[Map[String, Double]]

object BellmanFordAlg:
  def impl[F[_]: Sync]: BellmanFordAlg[F] = new BellmanFordAlg[F]:
    def bellmanFordAlg(
        graph: GraphLogarithmicSpace,
        edges: Map[fromTokenToToken, RLogarithmicScale],
        uniqueVertices: Map[Token, Double],
        idStartingVertex: Token
    ): F[Map[Token, RLogarithmicScale]] =
      for
        _ <- Sync[F].delay(println(s"Starting Algorithm"))
        distanceFromToken: Map[Token, Double] =
          uniqueVertices.updated(idStartingVertex, 0.0)
        predecessors: Map[Token, Token] = Map.empty
        finalDistance: Map[Token, RLogarithmicScale] <- relaxEdges(
          graph,
          distanceFromToken,
          predecessors,
          uniqueVertices.size
        )
        arbitrageOportunity <- negativeCycles(
          graph,
          finalDistance,
          predecessors,
          uniqueVertices.size
        )
        _ <- Sync[F].delay(
          println(s"Arbitrage Oportunity: ${finalDistance =!= arbitrageOportunity}")
        )
      yield finalDistance

    private def relaxEdges(
        graph: GraphLogarithmicSpace,
        distances: Map[Token, RLogarithmicScale],
        predecessors: Map[Token, Token],
        iterations: Int
    ): F[Map[String, Double]] =
      Sync[F].delay {
        val multableDistances = mutable.Map.from(distances)
        for _ <- 1 to iterations do
          for (u, neighbors) <- graph do
            for (v, weight) <- neighbors do
              if multableDistances(u) + weight._1 < multableDistances(v) then
                multableDistances.update(v, multableDistances(u) + weight._1)
                // predecessors.update(v, u)
        multableDistances.toMap
      }

    // once you know the optimal distance, you can check for negative weight cycles
    // thats running the algorithm again and checking if the distance  relaxed is the same as where we started
    def negativeCycles(
        graph: GraphLogarithmicSpace,
        knowOptimalDistance: Map[Token, RLogarithmicScale],
        predecessors: Map[Token, Token],
        iterations: Int
    ): F[Map[String, Double]] =
      relaxEdges(graph, knowOptimalDistance, predecessors, iterations)

// Conceptual Explanation
// Currency Exchange as a Graph:

// Imagine each currency as a node in a graph.
// Each directed edge between two nodes represents an exchange rate from one currency to another.
// Logarithmic Transformation:

// To detect arbitrage opportunities, we transform the exchange rates using the logarithm.
// Specifically, for an exchange rate ( r ) from currency ( A ) to currency ( B ), we use the weight ( -\log(r) ).
// This transformation turns the problem of finding a product of exchange rates greater than 1 into finding a sum of logarithms less than 0.
// Negative Weight Cycles:

// In the transformed graph, a negative weight cycle corresponds to a sequence of currency exchanges that results in a net gain.
// If you can traverse a cycle and the sum of the weights (negative logarithms of the exchange rates) is negative, it means the product of the exchange rates in the original graph is greater than 1.
// This implies that you can start with a certain amount of currency, follow the cycle, and end up with more of the same currency, creating an arbitrage opportunity.

// Mathematical Explanation
// Original Exchange Rates:

// Suppose you have exchange rates ( r_{AB} ) from currency ( A ) to currency ( B ), ( r_{BC} ) from currency ( B ) to currency ( C ), and ( r_{CA} ) from currency ( C ) to currency ( A ).
// Product of Exchange Rates:

// An arbitrage opportunity exists if ( r_{AB} \times r_{BC} \times r_{CA} > 1 ).
// Logarithmic Transformation:

// Taking the logarithm of the product, we get: [ \log(r_{AB} \times r_{BC} \times r_{CA}) = \log(r_{AB}) + \log(r_{BC}) + \log(r_{CA}) ]
// An arbitrage opportunity exists if: [ \log(r_{AB}) + \log(r_{BC}) + \log(r_{CA}) > 0 ]
// By multiplying by -1, we get: [ -\log(r_{AB}) + -\log(r_{BC}) + -\log(r_{CA}) < 0 ]
// This means that the sum of the weights in the transformed graph is negative, indicating a negative weight cycle.
// AND bellman ford algorithm is used to detect negative weight cycles in a graph

// see https://www.ijisrt.com/assets/upload/files/IJISRT20MAY047.pdf
// see also https://www.thealgorists.com/Algo/ShortestPaths/Arbitrage

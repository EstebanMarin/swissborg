package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*

// step 0.1 lets improve the modeling of the problem
// step 1.0 lets build the graph from the data and build a suitable data structure to represent the graph
// step 1.1 lets develop the algorithm
// step 2 lets develop the logarithmic transformation to detect arbitrage opportunities

trait BellmanFordAlg[F[_]]:
  def arbitrageOpportunity(graph: GraphLogarithmicSpace): F[Unit]

object BellmanFordAlg:
  def impl[F[_]]: BellmanFordAlg[F] = new BellmanFordAlg[F]:
    def arbitrageOpportunity(graph: GraphLogarithmicSpace): F[Unit] =
      ???

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

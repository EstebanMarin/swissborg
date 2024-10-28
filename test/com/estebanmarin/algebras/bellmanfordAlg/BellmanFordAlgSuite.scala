package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.*

class BellmanFordAlgSuite extends munit.CatsEffectSuite {
  // Define the BellmanFordAlg instance
  val bellmanFordAlg: BellmanFordAlg[IO] = BellmanFordAlg.impl[IO]

  test("BellmanFordAlg should detect negative weight cycles") {
    val graph: GraphLogarithmicSpace = Map(
      "A" -> Map("B" -> (1, 0.0)),
      "B" -> Map("C" -> (1, 0.0)),
      "C" -> Map("A" -> (1, 0.0))
    )
    val edges: Map[fromTokenToToken, RLogarithmicScale] = Map(
      ("A", "B") -> 0.0,
      ("B", "C") -> 0.0,
      ("C", "A") -> 0.0
    )
    val uniqueVertices: Map[Token, Double] = Map(
      "A" -> 0.0,
      "B" -> 0.0,
      "C" -> 0.0
    )
    val relaxedSystem: Map[Token, Double] =
      bellmanFordAlg
        .bellmanFordAlg(graph, edges, uniqueVertices, "A")
        .unsafeRunSync()
    assertEquals(relaxedSystem, Map("A" -> 0.0, "B" -> 1.0, "C" -> 2.0))
  }
  
}

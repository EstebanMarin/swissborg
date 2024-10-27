package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.*

class  GraphDataStructureSuite extends munit.CatsEffectSuite {
  // Define the GraphDataStructure instance
  val graphDataStructure: GraphDataStructure[IO] = GraphDataStructure.impl[IO]

  test("GraphDataStructure should create a graph from rates") {
    val rates: Map[fromTokenToToken, Rate] = Map(
      ("A", "A") -> 1,
      ("A", "B") -> 12,
      ("A", "C") -> 0.5,
      ("B", "A") -> 0.5,
      ("B", "B") -> 1,
      ("B", "C") -> 2,
      ("C", "A") -> 1,
      ("C", "B") -> 1.5,
      ("C", "C") -> 1
    )
    val expectedGraph: Graph = Map(
      "A" -> Map(("A", 1), ("B", 12)),
      "B" -> Map(("C", 0.5), ("B", 1)),
      "C" -> Map(("B", 2), ("C", 1), ("A", 1.5))
    )
    val result: Graph =
      graphDataStructure.createGraphfromRates(rates).unsafeRunSync()
    assertEquals(result, expectedGraph)
  }

}

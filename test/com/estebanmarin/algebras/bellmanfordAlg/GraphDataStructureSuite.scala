package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.*

class GraphDataStructureSuite extends munit.CatsEffectSuite {
  test("GraphDataStructure should create a graph from rates") {
    val rates: Map[fromTokenToToken, Rate] = Map(
      ("A", "A") -> 1,
      ("A", "B") -> 12,
      ("B", "C") -> 0.5,
      ("B", "B") -> 1,
      ("C", "B") -> 2,
      ("C", "C") -> 1,
      ("C", "A") -> 1.5
    )
    val expectedGraph: Graph = Map(
      "A" -> Map(("A", 1), ("B", 12)),
      "B" -> Map(("C", 0.5), ("B", 1)),
      "C" -> Map(("B", 2), ("C", 1), ("A", 1.5))
    )
    val result: Graph =
      GraphDataStructure.createGraphfromRates(rates).unsafeRunSync()
    assertEquals(result, expectedGraph)
  }

}

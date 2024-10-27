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
    val expectedGraph: GraphLogarithmicSpace = Map(
      "A" -> Map("A" -> (1, 0.0), "B" -> (12, -2.4849066497880004), "C" -> (0.5, -0.6931471805599453)),
      "B" -> Map("A" -> (0.5, -0.6931471805599453), "B" -> (1, 0.0), "C" -> (2, -0.6931471805599453)),
      "C" -> Map("A" -> (1, 0.0), "B" -> (1.5, -0.4054651081081644), "C" -> (1, 0.0))
    )
    val result: GraphLogarithmicSpace =
      graphDataStructure.createGraphfromRates(rates).unsafeRunSync()
    assertEquals(result, expectedGraph)
  }

}

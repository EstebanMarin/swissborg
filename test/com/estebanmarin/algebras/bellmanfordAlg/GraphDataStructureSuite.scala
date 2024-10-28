package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.*

class GraphDataStructureSuite extends munit.CatsEffectSuite {
  // Define the GraphDataStructure instance
  val graphDataStructure: GraphDataStructure[IO] = GraphDataStructure.impl[IO]

  test("Create unique Vertices") {
    val rates: Map[fromTokenToToken, Rate] = Map(
      ("1", "4") -> 5,
      ("4", "3") -> 3,
      ("3", "2") -> 10,
      ("2", "2") -> 1,
      ("1", "2") -> 4
    )
    val expectedEdges: Map[Token, Double] =
      Map(
        "1" -> Double.MaxValue,
        "4" -> Double.MaxValue,
        "3" -> Double.MaxValue,
        "2" -> Double.MaxValue
      )
    val result: Map[Token, Double] =
      graphDataStructure.createUniqueVertices(rates).unsafeRunSync()
    assertEquals(result, expectedEdges)
  }
  test("Create edges of graph") {
    val rates: Map[fromTokenToToken, Rate] = Map(
      ("1", "4") -> 5,
      ("4", "3") -> 3,
      ("3", "2") -> 10,
      ("1", "2") -> 4
    )
    val expectedEdges: Map[fromTokenToToken, RLogarithmicScale] =
      Map(
        ("1", "4") -> -0.6989700043360189,
        ("4", "3") -> -0.47712125471966244,
        ("3", "2") -> -1,
        ("1", "2") -> -0.6020599913279624
      )
    val result: Map[fromTokenToToken, RLogarithmicScale] =
      graphDataStructure.createEdgesOfGraphLogarithmic(rates).unsafeRunSync()
    assertEquals(result, expectedEdges)
  }

  test("Simple rate -log(rate) transformation") {
    val rates: Map[fromTokenToToken, Rate] = Map(
      ("A", "A") -> 1
    )
    val expectedGraph: GraphLogarithmicSpace = Map(
      "A" -> Map("A" -> (1, 0.0))
    )
    val result: GraphLogarithmicSpace =
      graphDataStructure.createGraphfromRates(rates).unsafeRunSync()
    assertEquals(result, expectedGraph)
  }

  test("GraphDataStructure should create a graph from rates") {
    val rates: Map[fromTokenToToken, Rate] = Map(
      ("A", "A") -> 1,
      ("A", "B") -> 12,
      ("B", "A") -> 0.5,
      ("B", "B") -> 1
    )
    val expectedGraph: GraphLogarithmicSpace = Map(
      "A" -> Map("A" -> (1, 0.0), "B" -> (12, -1.0791812460476249)),
      "B" -> Map("A" -> (0.5, 0.3010299956639812), "B" -> (1, 0.0))
    )
    val result: GraphLogarithmicSpace =
      graphDataStructure.createGraphfromRates(rates).unsafeRunSync()
    assertEquals(result, expectedGraph)
  }

}

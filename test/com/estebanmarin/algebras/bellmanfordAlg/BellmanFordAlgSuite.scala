package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*
import cats.effect.*
import cats.syntax.all.given

class BellmanFordAlgSuite extends munit.CatsEffectSuite {
  // Define the BellmanFordAlg instance
  val bellmanFordAlg: BellmanFordAlg[IO] = BellmanFordAlg.impl[IO]

  test("BellmanFordAlg should detect negative cycles") {
    // see https://youtu.be/FtN3BYH2Zes?si=Mn3gKZV66u1hEVSR&t=805
    val graph: GraphLogarithmicSpace = Map(
      "1" -> Map("2" -> (4, 0.602), "4" -> (5, 0.699)),
      "2" -> Map("4" -> (5, 0.699)),
      "3" -> Map("2" -> (-10, 0.0)),
      "4" -> Map("3" -> (3, 0.477))
    )
    val edges: Map[fromTokenToToken, RLogarithmicScale] = Map(
      ("1", "2") -> 4,
      ("1", "4") -> 5,
      ("3", "2") -> -10,
      ("4", "3") -> 3,
      ("2", "4") -> 5
    )
    val uniqueVertices: Map[Token, Double] = Map(
      "1" -> 0,
      "2" -> Double.PositiveInfinity,
      "3" -> Double.PositiveInfinity,
      "4" -> Double.PositiveInfinity
    )
    val relaxedSystem: Map[Token, Double] =
      bellmanFordAlg
        .bellmanFordAlg(graph, edges, uniqueVertices, "1")
        .unsafeRunSync()

    val secondPass =
      bellmanFordAlg
        .negativeCycles(graph, relaxedSystem, Map.empty, uniqueVertices.size)
        .unsafeRunSync()

    assert(
      relaxedSystem =!= secondPass
    )
  }
  test("BellmanFordAlg should arrive to the correct result simple example") {
    // see https://youtu.be/FtN3BYH2Zes?si=CFsbPJ4Jr0GNI57p&t=760
    val graph: GraphLogarithmicSpace = Map(
      "1" -> Map("2" -> (4, 0.602), "4" -> (5, 0.699)),
      "3" -> Map("2" -> (-10, 0.0)),
      "4" -> Map("3" -> (3, 0.477))
    )
    val edges: Map[fromTokenToToken, RLogarithmicScale] = Map(
      ("1", "2") -> 4,
      ("1", "4") -> 5,
      ("3", "2") -> -10,
      ("4", "3") -> 3
    )
    val uniqueVertices: Map[Token, Double] = Map(
      "1" -> 0,
      "2" -> Double.PositiveInfinity,
      "3" -> Double.PositiveInfinity,
      "4" -> Double.PositiveInfinity
    )
    val relaxedSystem: Map[Token, Double] =
      bellmanFordAlg
        .bellmanFordAlg(graph, edges, uniqueVertices, "1")
        .unsafeRunSync()

    // there are no negative cycles
    val secondPass =
      bellmanFordAlg
        .negativeCycles(graph, relaxedSystem, Map.empty, uniqueVertices.size)
        .unsafeRunSync()

    assertEquals(
      relaxedSystem,
      Map("1" -> 0.0, "2" -> -2.0, "3" -> 8.0, "4" -> 5.0)
    )
    assert(
      relaxedSystem === secondPass
    )
  }

}

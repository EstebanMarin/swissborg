package com.estebanmarin.algebras

import com.estebanmarin.algebras.models.*

trait GraphDataStructure[F[_]]:
  def createGraphfromRates(rates: Map[fromTokenToToken, Rate]): F[Graph]

object GraphDataStructure:
    def impl[F[_]]: GraphDataStructure[F] = new GraphDataStructure[F]:
      def createGraphfromRates(rates: Map[fromTokenToToken, Rate]): F[Graph] = 
        ???

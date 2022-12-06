package com.google.common.geometry.spark

import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.Column
import com.swoop.alchemy.spark.expressions.WithHelper

object S2GeometryFunctions extends WithHelper {

  def s2LatLonToCellId(col1: Column, col2: Column, col3: Column): Column = withExpr {
    S2LatLonToCellId(col1.expr, col2.expr, col3.expr)
  }

  def s2CellIdToLatLon(col1: Column): Column = withExpr {
    S2CellIdToLatLon(col1.expr)
  }

}

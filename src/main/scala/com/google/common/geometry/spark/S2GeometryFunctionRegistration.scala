package com.google.common.geometry.spark

import com.swoop.alchemy.spark.expressions.NativeFunctionRegistration
import org.apache.spark.sql.catalyst.expressions.ExpressionInfo

object S2GeometryFunctionRegistration extends NativeFunctionRegistration{

  val expressions: Map[String, (ExpressionInfo, FunctionBuilder)] = Map(
    expression[S2LatLonToCellId]("s2_lat_lon_to_cell_id"),
    // TODO add more
  )
}

//activate for calling in sparkSQL with "S2GeometryFunctionRegistration.registerFunctions(spark)"


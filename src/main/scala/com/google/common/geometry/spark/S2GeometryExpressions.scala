package com.google.common.geometry.spark

import com.google.common.geometry.{S2CellId, S2LatLng}
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.codegen.{CodegenContext, ExprCode}
import org.apache.spark.sql.types.{ByteType, DataType, DoubleType, LongType, IntegerType}

@ExpressionDescription(
  usage = "_FUNC_(latitude_degrees, longitude_degrees, s2_level) - " +
    "Returns the s2 cell id for a lat/lon pair at a given S2 level.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(10.0912348, 11.1908423, 12) as s2_cell_id;
       1224917869591003136
  """,
  group = "s2geometry_funcs",
  since = "3.3.1")
case class S2LatLonToCellId(lat: Expression, lon: Expression, s2Level: Expression)
  extends TernaryExpression with ImplicitCastInputTypes with NullIntolerant {

  override def first: Expression = lat
  override def second: Expression = lon
  override def third: Expression = s2Level

  override def inputTypes: Seq[DataType] = Seq(DoubleType, DoubleType, IntegerType)

  override def dataType: DataType = LongType

  override def nullSafeEval(lat1: Any, lon1: Any, s2Level: Any): Any = {
    val s2ll = S2LatLng.fromDegrees(lat1.asInstanceOf[Double], lon1.asInstanceOf[Double])
    S2CellId.fromLatLng(s2ll).parent(s2Level.asInstanceOf[Int]).id()
  }

  override protected def doGenCode(ctx: CodegenContext, ev: ExprCode): ExprCode = {
    nullSafeCodeGen(ctx, ev, (la, lo, le) => {
      //val a =  S2GeometryFunctions.getClass.getCanonicalName.asInstanceOf[String]
      s"""${ev.value} = S2CellId.fromLatLng(S2LatLng.fromDegrees($la, $lo)).parent($le).id();"""
    })
  }

  override def prettyName: String = "s2_lat_lon_to_cell_id"

  override protected def withNewChildrenInternal(newFirst: Expression,
                                                 newSecond: Expression,
                                                 newThird: Expression): S2LatLonToCellId =
    copy(lat = newFirst, lon = newSecond, s2Level = newThird)
}


package com.google.common.geometry.spark

import com.google.common.geometry.{S2CellId, S2LatLng}
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.codegen.CodeGenerator.boxedType
import org.apache.spark.sql.catalyst.expressions.codegen.{CodegenContext, ExprCode}
import org.apache.spark.sql.types._

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
    val expr = ctx.addReferenceObj("this", this)
    defineCodeGen(ctx, ev, (la, lo, le) =>
      s"(${boxedType(dataType)})$expr.nullSafeEval($la, $lo, $le)")
  }

  override def prettyName: String = "s2_lat_lon_to_cell_id"

  override protected def withNewChildrenInternal(newFirst: Expression,
                                                 newSecond: Expression,
                                                 newThird: Expression): S2LatLonToCellId =
    copy(lat = newFirst, lon = newSecond, s2Level = newThird)
}


@ExpressionDescription(
  usage = "_FUNC_(cell_id) - " +
    "Returns the lat/lon pair for the center of a s2 cell",
  examples =
    """
    Examples:
      > SELECT _FUNC_(1224917869591003136) as s2_cell_center;
       (10.101848678309088, 11.195717211533564)
  """,
  group = "s2geometry_funcs",
  since = "3.3.1")
case class S2CellIdToLatLon(child: Expression)
  extends UnaryExpression with ImplicitCastInputTypes with NullIntolerant {

  override def inputTypes: Seq[DataType] = Seq(LongType, ShortType, IntegerType, ByteType)

  override def dataType: DataType = StructType(
    Seq(
      StructField("lat", DoubleType, false),
      StructField("lon", DoubleType, false),
    )
  )

  override def nullSafeEval(input: Any): Any = {
    val cell = new S2CellId(input.asInstanceOf[Long])
    val ll = cell.toLatLng()
    InternalRow(ll.latDegrees(), ll.lngDegrees())
  }

  override def prettyName: String = "s2_cell_id_to_lat_lon"

  override protected def withNewChildInternal(newChild: Expression): S2CellIdToLatLon =
    copy(child = newChild)

  override protected def doGenCode(ctx: CodegenContext, ev: ExprCode): ExprCode = {
    val expr = ctx.addReferenceObj("this", this)
    defineCodeGen(ctx, ev, (cid) =>
      s"(${boxedType(dataType)})$expr.nullSafeEval($cid)")
  }

}


@ExpressionDescription(
  usage = "_FUNC_(cell_id) - " +
    "Returns the lat/lon pair for the center of a s2 cell that is nearest to the given lat/lon pair.",
  examples =
    """
    Examples:
      > SELECT _FUNC_(10.0912348, 11.1908423, 12) as s2_cell_center;
       (10.101848678309088, 11.195717211533564)
  """,
  group = "s2geometry_funcs",
  since = "3.3.1")
case class S2NearestCenter(lat: Expression, lon: Expression, s2Level: Expression)
  extends TernaryExpression with ImplicitCastInputTypes with NullIntolerant {

  override def first: Expression = lat
  override def second: Expression = lon
  override def third: Expression = s2Level

  override def inputTypes: Seq[DataType] = Seq(DoubleType, DoubleType, IntegerType)

  override def dataType: DataType = StructType(
    Seq(
      StructField("lat", DoubleType, false),
      StructField("lon", DoubleType, false),
    )
  )

  override def nullSafeEval(lat1: Any, lon1: Any, s2Level: Any): Any = {
    val s2ll = S2LatLng.fromDegrees(lat1.asInstanceOf[Double], lon1.asInstanceOf[Double])
    val ll_center = S2CellId.fromLatLng(s2ll).parent(s2Level.asInstanceOf[Int]).toLatLng()
    InternalRow(ll_center.latDegrees(), ll_center.lngDegrees())
  }

  override protected def doGenCode(ctx: CodegenContext, ev: ExprCode): ExprCode = {
    val expr = ctx.addReferenceObj("this", this)
    defineCodeGen(ctx, ev, (la, lo, le) =>
      s"(${boxedType(dataType)})$expr.nullSafeEval($la, $lo, $le)")
  }

  override def prettyName: String = "s2_nearest_center_to_lat_lon"

  override protected def withNewChildrenInternal(newFirst: Expression,
                                                 newSecond: Expression,
                                                 newThird: Expression): S2NearestCenter =
    copy(lat = newFirst, lon = newSecond, s2Level = newThird)
}
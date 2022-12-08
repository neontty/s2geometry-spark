
package io.github.neontty.s2geometry.spark

import io.github.neontty.s2geometry.spark.S2GeometryFunctions.s2LatLonToCellId
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col


object Main {
  def main(args: Array[String]) {
    println("hello world!")


    val spark = SparkSession.builder.appName("testapp").config("spark.master", "local").getOrCreate()

    import spark.implicits._


    var df = Seq(
      (-58.390481, 30.128403, 10),
      (-58.390481, 12.128403, 10),
      (-58.390481, -30.128403, 10),
      (10.0912348, 11.1908423, 12),
    ).toDF("lat1", "lon1", "lvl1")

    df = df.withColumn("s2cell", s2LatLonToCellId(
      col("lat1"),
      col("lon1"),
      col("lvl1"))
    )

    println(df.show())

    S2GeometryFunctionRegistration.registerFunctions(spark)

    df.createOrReplaceTempView("some_table")

    val df2 = spark.sql("SELECT *, s2_lat_lon_to_cell_id(lat1, lon1, lvl1) as cellid2 from some_table")
    println(df2.show())


    val df3 = spark.sql("SELECT *, s2_cell_id_to_lat_lon(s2cell) as cell_centers from some_table")
    println(df3.show())

    spark.stop()

    println("done")
  }
}

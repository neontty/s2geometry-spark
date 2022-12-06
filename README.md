# s2geometry-spark
spark native expressions wrapping java implementation of s2geometry library


project structure and spark infrastructure code is being borrowed heavily from: 
- apache/incubator-sedona
- yaooqinn/itachi
- mrpowers/bebe

Thank you for your work.


## using in pyspark

To call the function registrator from pyspark to use functions in SQL:
`spark._jvm.com.google.common.geometry.spark.S2GeometryFunctionRegistration.registerFunctions(spark._jsparkSession)`



from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
from pyspark.sql import Column
from pyspark.sql import DataFrame
from pyspark.sql import GroupedData
import pandas as pd
import os
import re
import gc


baseResultDir = "/home/wy/PycharmProjects/spark1/ContentResult/"
baseDataDir = "/home/wy/文档/courseData/"
data = pd.read_csv(os.path.join(baseDataDir, "temp.csv"))
data = data.dropna()
print("read complete")

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
courseData = spark.createDataFrame(data)
del data
gc.collect()
courseData = courseData.select(courseData.Length.cast("int"), courseData.StudyTime.cast("double"),
                               courseData.Classification)
courseData.printSchema()
courseData.show()
courseData.toPandas().to_excel(os.path.join(baseDataDir, "temp.xlsx"), index=False)

lengthGroup = courseData.groupBy("Classification").sum("Length")
studyTimeGroup = courseData.groupBy("Classification").avg("StudyTime")
lengthGroup.show()
studyTimeGroup.show()

lengthGroup.toPandas().to_excel(os.path.join(baseResultDir, "LengthGroup.xlsx"), index=False)
studyTimeGroup.toPandas().to_excel(os.path.join(baseResultDir, "StudyTimeGroup.xlsx"), index=False)

spark.stop()
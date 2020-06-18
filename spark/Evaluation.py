from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
from pyspark.sql.types import *
import pandas as pd
import pyspark
import os
import numpy as np


baseResultDir = "/home/wy/PycharmProjects/spark1/EvaluationResult/"
baseDataDir = "/home/wy/文档/courseData/"
data = pd.read_csv(os.path.join(baseDataDir, "usercourseevaluation.csv"))
data = data.drop(["ENumGood", "EvaluateTime", "UserName", "_id"], axis=1)
print(data.index)
print(data.iloc[0]["EvaluateScore"], type(data.iloc[0]["EvaluateScore"]))

for i in range(0, len(data)):
    data.loc[i, "EvaluateScore"] = float(data.iloc[i]["EvaluateScore"].strip("分"))

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
courseData = spark.createDataFrame(data)
# courseData.printSchema()
# courseData.show()

groupCourseData = courseData.groupBy("CourseName").avg("EvaluateScore")
df = groupCourseData.toPandas()
df.columns = ["CourseName", "AvgScore"]
groupCourseData = spark.createDataFrame(df)
groupCourseData.printSchema()
groupCourseData.toPandas().to_excel(os.path.join(baseResultDir, "AvgScore.xlsx"))
# print(df)
# print(groupCourseData.schema, type(groupCourseData.schema))
# groupCourseData.schema[1] = StructField("avg_score", DoubleType(), True)
# groupCourseData.printSchema()
# groupCourseData.show()
# print(type(groupCourseData))

info = np.array([[groupCourseData.filter(groupCourseData.AvgScore >= 9).count()],
                 [groupCourseData.filter(groupCourseData.AvgScore < 9).count()]])

infodf = pd.DataFrame(info, index=[">=9", "<9"])
infodf.to_excel(os.path.join(baseResultDir, "AvgScoreCount.xlsx"))

spark.stop()

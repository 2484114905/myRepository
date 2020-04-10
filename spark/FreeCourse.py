from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
import pandas as pd
import pyspark
import os
import numpy as np


baseResultDir = "/home/wy/PycharmProjects/spark1/FreeCourseResult/"
baseDataDir = "/home/wy/文档/courseData/"
data = pd.read_csv(os.path.join(baseDataDir, "freecourse.csv"))
for i in range(0, len(data)):
    if data.iloc[i]["NumComment"] < 500:
        data.loc[i, "NumComment"] = "<500"
    elif 500 <= data.iloc[i]["NumComment"] <= 5000:
        data.loc[i, "NumComment"] = "500-5000"
    elif data.iloc[i]["NumComment"] > 5000:
        data.loc[i, "NumComment"] = ">5000"

# print(type(data.iloc[0]["Score"]), data.iloc[0]["Score"])

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
courseData = spark.createDataFrame(data)
courseData.printSchema()
# courseData.show()

commentInfo = courseData.groupBy("NumComment").count()
scoreInfo = courseData.groupBy("NumComment").avg("Score")
commentInfo.show()
scoreInfo.show()
commentInfo.toPandas().to_excel(os.path.join(baseResultDir, "FreeCourseCommentInfo.xlsx"), index=False)
scoreInfo.toPandas().to_excel(os.path.join(baseResultDir, "FreeCourseScoreInfo.xlsx"), index=False)

spark.stop()

from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
import pandas as pd
import os
import numpy as np

baseResultDir = "/home/wy/PycharmProjects/spark1/UserResult/"
baseDataDir = "/home/wy/文档/courseData/"
data = pd.read_csv(os.path.join(baseDataDir, "user.csv"))
data = data.fillna("0")
# print(data.isna())
for i in range(0, len(data)):
    s = str(data.iloc[i]["Length"])
    # if len(s) == 0:
    #     s = "0"
    data.loc[i, "Length"] = s

for i in range(0, len(data)):
    s = data.iloc[i]["Length"].strip("h")
    length = float(s)
    if length < 100:
        data.loc[i, "Length"] = "<100"
    elif 100 <= length < 200:
        data.loc[i, "Length"] = "100-200"
    elif 200 <= length < 300:
        data.loc[i, "Length"] = "200-300"
    elif 300 <= length < 400:
        data.loc[i, "Length"] = "300-400"
    elif 400 <= length < 500:
        data.loc[i, "Length"] = "400-500"
    elif length >= 500 :
        data.loc[i, "Length"] = ">=500"

print("process complete")

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
userData = spark.createDataFrame(data)
userData.show()
userData.printSchema()

lengthUser = userData.groupBy("Length").count()
lengthUser.toPandas().to_excel(os.path.join(baseResultDir, "user.xlsx"), index=False)

spark.stop()

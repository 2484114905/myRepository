from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
import pandas as pd
import os

baseResultDir = "/home/wy/PycharmProjects/spark1/ChargeCourseResult/"
baseDataDir = "/home/wy/文档/courseData/"

data = pd.read_csv(os.path.join(baseDataDir, "chargecourse.csv"))
data = data.drop("PreTech", axis=1)
# data.loc[0, "Praise"] = 1.0
# s = data.iloc[0]["Charge"].strip("￥.0")
# print(type(s))
# charge = int(s)
# print(charge, type(charge))
for i in range(0, len(data)):
    s = data.iloc[i]["Charge"].strip("原价￥.0")
    if int(s) <= 100:
        data.loc[i, "Charge"] = "<=100"
    elif 100 < int(s) <= 200:
        data.loc[i, "Charge"] = "<=200"
    elif 200 < int(s) <= 300:
        data.loc[i, "Charge"] = "<=300"
    elif 300 < int(s) <= 400:
        data.loc[i, "Charge"] = "<=400"
    elif int(s) > 400:
        data.loc[i, "Charge"] = ">400"

for i in range(0, len(data)):
    s = data.iloc[i]["Praise"].strip("%")
    data.loc[i, "Praise"] = float(s) / 100
# print(data)

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)

chargeCourse = spark.createDataFrame(data)
chargeCourse.printSchema()
groupChargeCourse = chargeCourse.groupBy("Charge").count()
groupChargeCourse.show()
praiseChargeCourse = chargeCourse.groupBy("Charge").avg("Praise")
praiseChargeCourse.show()

groupChargeCourse.toPandas().to_excel(os.path.join(baseResultDir, "GroupChargeCourse.xlsx"), index=False)
praiseChargeCourse.toPandas().to_excel(os.path.join(baseResultDir, "PraiseChargeCourse.xlsx"), index=False)

spark.stop()

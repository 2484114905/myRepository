from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
from pyspark.sql import Column
from pyspark.sql import DataFrame
from pyspark.sql import GroupedData
import pandas as pd
import os
import re
import gc

baseResultDir = "/home/wy/PycharmProjects/spark1/CourseResult/"
baseDataDir = "/home/wy/文档/courseData/"
data = pd.read_csv(os.path.join(baseDataDir, "content.csv"))
data = data.drop(columns=["_id", "CourseName", "NumAnswer", "NumCode", "NumNotes", "Schedule", "UserName"], axis=1)
gc.collect()
print(data.columns)
print("read complete")
for i in range(0, len(data)):
    timeList = [int(s) for s in re.findall(r"\d+", data.iloc[i]["Length"])]
    if len(timeList) == 2:
        data.loc[i, "Length"] = timeList[0] * 60 + timeList[1]
    else:
        data.loc[i, "Length"] = timeList[0]

for i in range(0, len(data)):
    classificationList = data.iloc[i]["Classification"].strip("[] ").split(",")
    if len(classificationList) == 3:
        data.loc[i, "Classification"] = classificationList[1].strip('\" ')
    elif len(classificationList) == 2:
        data.loc[i, "Classification"] = classificationList[0].strip('\" ')

for i in range(0, len(data)):
    s = data.iloc[i]["StudyTime"].strip("已学%")
    data.loc[i, "StudyTime"] = float(s) / 100
print("process complete")

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
courseData = spark.createDataFrame(data)
courseData.printSchema()

lengthGroup = courseData.groupBy("Classification").sum("Length")
studyTimeGroup = courseData.groupBy("Classification").avg("StudyTime")
lengthGroup.show()
studyTimeGroup.show()

lengthGroup.toPandas().to_excel(os.path.join(baseResultDir, "LengthGroup.xlsx"))
studyTimeGroup.toPandas().to_excel(os.path.join(baseResultDir, "StudyTimeGroup.xlsx"))

spark.stop()

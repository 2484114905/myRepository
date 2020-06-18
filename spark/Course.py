from pyspark import SparkConf, SparkContext
from pyspark.sql import SparkSession
from pyspark.sql import Column
from pyspark.sql import DataFrame
from pyspark.sql import GroupedData
import pandas as pd
import os

baseResultDir = "/home/wy/PycharmProjects/spark1/CourseResult/"
baseDataDir = "/home/wy/文档/courseData/"
data = pd.read_csv(os.path.join(baseDataDir, "course.csv"))

for i in range(0, len(data)):
    classificationList = data.iloc[i]["Classification"].strip("[] ").split(",")
    if len(classificationList) == 3:
        data.loc[i, "Classification"] = classificationList[1].strip('\" ')
    elif len(classificationList) == 2:
        data.loc[i, "Classification"] = classificationList[0].strip('\" ')

conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
courseData = spark.createDataFrame(data)
# courseData.printSchema()

freeCourse = courseData.filter(courseData.Charge == "False")
chargeCourse = courseData.filter(courseData.Charge == "True")

ifChargeCourse = courseData.groupBy("Charge").count()
ifChargeCourse.toPandas().to_excel(os.path.join(baseResultDir, "FreeAndChargeCount.xlsx"), index=False)
ifChargeCourse.show()
# freeCourse.select("Classification").show()

freeLevelCourse = freeCourse.groupBy("level").count()
freeLevelCourse.show()
chargeLevelCourse = chargeCourse.groupBy("level").count()
chargeLevelCourse.show()

freeLevelCourse.toPandas().to_excel(os.path.join(baseResultDir, "FreeLevelCourse.xlsx"), index=False)
chargeLevelCourse.toPandas().to_excel(os.path.join(baseResultDir, "ChargeLevelCourse.xlsx"), index=False)

freeClassificationCourse = freeCourse.groupBy("Classification").count()
chargeClassificationCourse = chargeCourse.groupBy("Classification").count()
freeClassificationCourse.show()
chargeClassificationCourse.show()

freeClassificationCourse.toPandas().to_excel(os.path.join(baseResultDir, "FreeClassificationCourse.xlsx"), index=False)
chargeClassificationCourse.toPandas().to_excel(os.path.join(baseResultDir, "ChargeClassificationCourse.xlsx"),index=False)

spark.stop()

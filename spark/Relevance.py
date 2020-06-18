from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession
from pyspark.ml.fpm import FPGrowth
import os

baseResultDir = "/home/wy/PycharmProjects/spark1/Relevance/"
baseDataDir = "/home/wy/文档/courseData/"
conf = SparkConf().setAppName("SimpleAPP")
sc = SparkContext(conf=conf)
sc.setLogLevel("error")
spark = SparkSession(sc)
df = spark.read.csv(os.path.join(baseDataDir, "usercourse.csv"), header=True)


#关联分析
rdd1 = df.select("UserName","CourseName").rdd
rdd2 = rdd1.groupByKey()
rdd3 = rdd2.map(lambda x:(x[0],list(set(x[1]))))

dataframe1 = rdd3.toDF()
print(dataframe1.show())
fpg = FPGrowth(itemsCol='_2', minSupport=0.3, minConfidence=0.6)
model = fpg.fit(dataframe1)


model.freqItemsets.show()
model.freqItemsets.write.json(os.path.join(baseResultDir,"usercourse_freq_json"), mode='overwrite')
# Display generated association rules.
model.associationRules.show()
model.associationRules.write.json(os.path.join(baseResultDir, "usercourse_asso_json"), mode='overwrite')
# transform examines the input items against all the association rules and summarize the
# consequents as prediction
model.transform(dataframe1).show()
model.transform(dataframe1).write.json(os.path.join("usercourse_data_json"), mode='overwrite')
model.associationRules.toPandas().to_excel(os.path.join(baseResultDir, "usercourse_asso.xlsx"), index=False)

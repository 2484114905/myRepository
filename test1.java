package code;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class test1 {
    public static void main(String[] args){
        try {
            String filename = "hdfs://localhost:9000/user/hadoop/test.txt";
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);

            if (fs.exists(new Path(filename))){
                System.out.println("文件存在");
            }
            else{
                System.out.println("文件不存在");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

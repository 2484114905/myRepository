package code;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class Test {
    public static Configuration configuration;
    public static Connection connection;
    public static Admin admin;

    public static void main(String[] args) throws IOException{
        createTable("student", new String[]{"score"});
        insertData("student", "Lihua", "score", "English", "90");
        getData("student", "Lihua", "score", "English");

    }

    public static void init(){
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.rootdir",  "hdfs://localhost:9000/hbase");
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void close(){
        try {
            if (admin != null){
                admin.close();
            }
            if (connection != null){
                connection.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void createTable(String tableName, String[] colFamily) throws IOException{
        init();
        TableName tableName1 = TableName.valueOf(tableName);
        if (admin.tableExists(tableName1)){
            System.out.println("table exists!");
        }
        else {
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            for (String colName : colFamily){
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(colName);
                tableDescriptor.addFamily(columnDescriptor);
            }
            admin.createTable(tableDescriptor);
        }
        close();
    }

    public static void insertData(String tableName, String rowKey, String colFamily, String colName, String value)
    throws IOException {
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(colName), Bytes.toBytes(value));
        table.put(put);
        table.close();
        close();
    }

    public static void getData(String tableName, String rowKey, String colFamily, String colName)
        throws IOException {
        init();
        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(colName));
        Result result = table.get(get);
        System.out.println(new String(result.getValue(colFamily.getBytes(), colName == null ? null : colName.getBytes())));
        table.close();
        close();
    }
}

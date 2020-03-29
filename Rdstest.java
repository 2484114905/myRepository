import java.sql.*;

public class Rdstest {
    public static void main(String[] args){
        String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        String DB_URL = "jdbc:mysql://rm-bp1772qk314g0j8vrno.mysql.rds.aliyuncs.com/demo1";

        //  Database credentials
         String USER = "root";
         String PASS = "Rdsroot1234";


            Connection conn = null;
            Statement stmt = null;
            try{
                //STEP 2: Register JDBC driver
                Class.forName("com.mysql.jdbc.Driver");

                //STEP 3: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL,USER,PASS);

                //STEP 4: Execute a query
                System.out.println("Creating statement...");
                stmt = conn.createStatement();
                String sql;
                sql = "SELECT * FROM table1 WHERE ID between 99900 and 99910";
                ResultSet rs = stmt.executeQuery(sql);

                //STEP 5: Extract data from result set
                System.out.print("ID            ");
                System.out.print("name          ");
                System.out.print("date          " + "\n");
                while(rs.next()){
                    //Retrieve by column name
                    int id  = rs.getInt("id");
                    String name = rs.getString("name");
                    Date date = rs.getDate("date");


                    //Display values
                    System.out.print(String.format("%-12s", id));
                    System.out.print(String.format("%-15s", name));
                    System.out.print(String.format("%-15s", date) + "\n");
                }
                //STEP 6: Clean-up environment
                rs.close();
                stmt.close();
                conn.close();
            }catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
            }catch(Exception e){
                //Handle errors for Class.forName
                e.printStackTrace();
            }finally{
                //finally block used to close resources
                try{
                    if(stmt!=null)
                        stmt.close();
                }catch(SQLException se2){
                }// nothing we can do
                try{
                    if(conn!=null)
                        conn.close();
                }catch(SQLException se){
                    se.printStackTrace();
                }//end finally try
            }//end try
            System.out.println("There are no thing wrong!");

    }
}


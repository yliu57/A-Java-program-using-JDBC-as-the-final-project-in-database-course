import java.io.*;
import java.sql.*;
import java.util.*;
import oracle.jdbc.driver.*;

public class Student{
    static Connection con;
    static Statement stmt;

    public static void main(String argv[])
    {
	   connectToDatabase();
    }

    public static void connectToDatabase()
    {
	String driverPrefixURL="jdbc:oracle:thin:@";
	String jdbc_url="artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
	
        // IMPORTANT: DO NOT PUT YOUR LOGIN INFORMATION HERE. INSTEAD, PROMPT USER FOR HIS/HER LOGIN/PASSWD
        Scanner sc= new Scanner(System.in);
        System.out.print("username:  ");  
        String username = sc.nextLine();              
        System.out.print("password:  "); 
        String password = sc.nextLine(); 

	
        try{
	    //Register Oracle driver
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (Exception e) {
            System.out.println("Failed to load JDBC/ODBC driver.");
            return;
        }

       try{
            System.out.println(driverPrefixURL+jdbc_url);
            con=DriverManager.getConnection(driverPrefixURL+jdbc_url, username, password);
            DatabaseMetaData dbmd=con.getMetaData();
            stmt=con.createStatement();

            System.out.println("Connected.");

            if(dbmd==null){
                System.out.println("No database meta data");
            }
            else {
                System.out.println("Database Product Name: "+dbmd.getDatabaseProductName());
                System.out.println("Database Product Version: "+dbmd.getDatabaseProductVersion());
                System.out.println("Database Driver Name: "+dbmd.getDriverName());
                System.out.println("Database Driver Version: "+dbmd.getDriverVersion());
            }

            //Initialize the script runner
            //ScriptRunner sr = new ScriptRunner(con);

            //Creating a reader object
            //Reader reader = new BufferedReader(new FileReader("E:\\oracle.sql"));
            
            //Running the script
            //sr.runScript(reader);



        }catch( Exception e) {e.printStackTrace();}

    }// End of connectToDatabase()
}// End of class


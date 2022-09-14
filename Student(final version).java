import java.io.*;
import java.sql.*;
import java.util.*;
import oracle.jdbc.driver.*;
import org.apache.ibatis.jdbc.ScriptRunner;

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
        System.out.print("input your username:  ");  
        String username = sc.nextLine();              
        System.out.print("input your password:  "); 
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
        
        System.out.print("input your sql file:  (example: oracle.sql)");  
        String sqllocation = "oracle.sql"; //sc.nextLine(); 

        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(con);
        //Creating a reader object
        Reader reader = new BufferedReader(new FileReader(sqllocation));
        //Running the script
        sr.runScript(reader);

        System.out.print("The database is ready for search. Below is the menu: \n");  
        System.out.print("1. View table contents\n2. Search by Transaction_ID\n3. Search by one or more attributes\n4. Exit\n");  
        
        int user_option = 0;
        String yesorno;
        String sqlcommands;
        PreparedStatement pstmt;
        ResultSet rset;
        ResultSet rset2;
        int tran_ID;    
        int cust_ID;    // customer_ID:
        float tot;        // total:
        String upc;     // UPC:
        int qua;        // quantity:
        
        String output01;    // customer_ID:
        String output02;    // total:
        String output03;    // UPC:
        String output04;    // quantity:
        String output1; // transaction_ID (Yes/No):
        String output2; // customer_ID (Yes/No):
        String output3; // transaction_date (Yes/No):
        String output4; // payment_method (Yes/No):
        String output5; // total (Yes/No):
        String output6; // UPC (Yes/No):
        String output7; // quantity (Yes/No):
        String output8; // Distinct (Yes/No):
        boolean zero_row;
        String select_transactions = "";
        String select_transactionContains = "";
        String where_transactions = "";
        String where_transactionContains = "";
        int start_ts = 0;   // select_transactions
        int start_tsc = 0;  // select_transactionContains
        int start_wts = 0;   // where_transactions
        int start_wtsc = 0;  // where_transactionContains
        int index;
        String divisionline = "";
        while(user_option != 4){
            System.out.print("The menu is below:\n");  
            System.out.print("1. View table contents\n2. Search by Transaction_ID\n3. Search by one or more attributes\n4. Exit\n");  
            System.out.print("The option number you can input is 1,2,3 or 4.\n");  
            System.out.print("Please enter the number of your option:\n");  

            user_option = sc.nextInt();
            if(user_option == 1){
                System.out.print("Input Yes if you want to view the table\n");  
                System.out.print("Input No if you do not want to view the table\n");  

                // Product Table
                System.out.print("Product (Yes/No): ");
                yesorno = sc.nextLine();
                yesorno = sc.nextLine();
                while(!(yesorno.equals("Yes") || yesorno.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    yesorno = sc.nextLine();
                }
                if(yesorno.equals("Yes")){
                    System.out.print("Table of Product:\n");
                    System.out.format("%10s%20s%35s%60s%15s%15s%10s\n", "UPC", "BRAND", "PRODUCT_NAME", "PRODUCT_DESCRIPTION", 
                    "CATEGORY", "MARKED_PRICE", "QUANTITY"); 
                    System.out.print("---------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
                    sqlcommands = "SELECT * FROM Product";
                    pstmt = con.prepareStatement(sqlcommands);
                    rset = pstmt.executeQuery();
                    while (rset.next()){
                        System.out.format("%10.0f%20s%35s%60s%15s%15.2f%10.0f\n", rset.getFloat(1), rset.getString (2), rset.getString (3), rset.getString (4), 
                        rset.getString (5), rset.getFloat(6), rset.getFloat(7));
                        //System.out.println("(" + rset.getFloat(1)+ "," + rset.getString (2)+ "," + rset.getString (3)+ "," 
                        //+ rset.getString (4)+ ","+ rset.getString (5)+ ","+ rset.getFloat(6)+ ","+ rset.getFloat(7) + ")");
                    }
                }
            

                // Customer Table
                System.out.print("Customer (Yes/No): ");  
                yesorno = sc.nextLine();
                while(!(yesorno.equals("Yes") || yesorno.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    yesorno = sc.nextLine();
                }
                if(yesorno.equals("Yes")){
                    System.out.print("Table of Customer:\n"); 
                    System.out.format("%12s%17s%15s%10s%15s%15s\n", "CUSTOMER_ID", "FIRST_NAME", "LAST_NAME", "AGE", 
                    "GENDER", "ZIP_CODE"); 
                    System.out.print("------------------------------------------------------------------------------------\n");
                    sqlcommands = "SELECT * FROM Customer";
                    pstmt = con.prepareStatement(sqlcommands);
                    rset = pstmt.executeQuery();
                    while (rset.next()){
                        System.out.format("%12.0f%17s%15s%10.0f%15s%15.0f\n", rset.getFloat(1), rset.getString (2), rset.getString (3), rset.getFloat (4), 
                        rset.getString (5), rset.getFloat(6));
                        //System.out.println("(" + rset.getFloat(1)+ "," + rset.getString (2)+ "," + rset.getString (3)+ "," 
                        //+ rset.getFloat(4)+ ","+ rset.getString(5)+ ","+ rset.getFloat(6) + ")");
                    }
                }

                // Transactions Table
                System.out.print("Transactions (Yes/No): ");  
                yesorno = sc.nextLine();
                while(!(yesorno.equals("Yes") || yesorno.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    yesorno = sc.nextLine();
                }
                if(yesorno.equals("Yes")){
                    System.out.print("Table of Transactions:\n"); 
                    System.out.format("%15s%15s%25s%20s%10s\n", "TRANSACTION_ID", "CUSTOMER_ID", "TRANSACTION_DATE", 
                    "PAYMENT_METHOD", "TOTAL"); 
                    System.out.print("-------------------------------------------------------------------------------------\n");
                    sqlcommands = "SELECT * FROM Transactions";
                    pstmt = con.prepareStatement(sqlcommands);
                    rset = pstmt.executeQuery();
                    while (rset.next()){
                        System.out.format("%15.0f%15.0f%25s%20s%10.2f\n", rset.getFloat(1), rset.getFloat(2), rset.getString(3), 
                        rset.getString(4), rset.getFloat(5));
                        //System.out.println("(" + rset.getFloat(1)+ "," + rset.getFloat(2)+ "," + rset.getString(3)+ "," 
                        //+ rset.getString(4)+ ","+ rset.getFloat(5) + ")");
                    }
                }

                // Transaction_Contains Table
                System.out.print("Transaction_Contains (Yes/No): ");  
                yesorno = sc.nextLine();
                while(!(yesorno.equals("Yes") || yesorno.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    yesorno = sc.nextLine();
                }
                if(yesorno.equals("Yes")){
                    System.out.print("Table of Transaction_Contains:\n"); 
                    System.out.format("%15s%15s%15s\n", "TRANSACTION_ID", "UPC", "QUANTITY"); 
                    System.out.print("---------------------------------------------\n");  
                    sqlcommands = "SELECT * FROM Transaction_Contains";
                    pstmt = con.prepareStatement(sqlcommands);
                    rset = pstmt.executeQuery();
                    while (rset.next()){
                        System.out.format("%15.0f%15s%15.0f\n", rset.getFloat(1), rset.getString(2), rset.getFloat(3));
                        //System.out.println("(" + rset.getFloat(1)+ "," + rset.getString(2)+ "," + rset.getFloat(3)+ ")");
                    }
                }
            }
            else if(user_option == 2){
                System.out.print("Enter the transaction_ID you want to search:\n");  
                tran_ID = sc.nextInt();
                sqlcommands = "SELECT * FROM Transactions WHERE transaction_ID = " + tran_ID;
                pstmt = con.prepareStatement(sqlcommands);
                rset = pstmt.executeQuery();
                zero_row = !rset.next();
                if(zero_row == true){
                    System.out.print("Sorry, the transaction_ID you input is not in the database.\n"); 
                }
                else{
                    sqlcommands = "SELECT * FROM Transactions WHERE transaction_ID = " + tran_ID;
                    pstmt = con.prepareStatement(sqlcommands);
                    rset = pstmt.executeQuery();
                    System.out.print("The Transactions information of your input transaction_ID:\n"); 
                    System.out.format("%15s%15s%25s%20s%10s\n", "TRANSACTION_ID", "CUSTOMER_ID", "TRANSACTION_DATE", 
                    "PAYMENT_METHOD", "TOTAL");
                    System.out.print("-------------------------------------------------------------------------------------\n");
                    while (rset.next()){
                            System.out.format("%15.0f%15.0f%25s%20s%10.2f\n", rset.getFloat(1), rset.getFloat(2), rset.getString(3), 
                            rset.getString(4), rset.getFloat(5));
                            //System.out.println("(" + rset.getFloat(1)+ "," + rset.getFloat(2)+ "," + rset.getString(3)+ "," 
                            //+ rset.getString(4)+ ","+ rset.getFloat(5) + ")");
                    }

                    System.out.print("The average quantity of your input transaction_ID in the Transaction_Contains table:\n"); 
                    System.out.format("%25s\n", "AVERAGE QUANTITY");
                    System.out.print("-------------------------\n");
                    sqlcommands = "SELECT avg(quantity) FROM Transaction_Contains WHERE transaction_ID = " + tran_ID + "GROUP BY transaction_ID";
                    pstmt = con.prepareStatement(sqlcommands);
                    rset = pstmt.executeQuery();
                    while (rset.next()){
                        System.out.format("%25.2f\n", rset.getFloat(1));
                        //System.out.println("(" + rset.getFloat(1)+ ")");
                    }
                }
            }
            else if(user_option == 3){
                System.out.print("Input fields:\n"); 

                System.out.print("customer_ID (Yes/No): "); 
                output01 = sc.nextLine();
                output01 = sc.nextLine();
                while(!(output01.equals("Yes") || output01.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output01 = sc.nextLine();
                }
                if(output01.equals("Yes")){
                    System.out.print("customer_ID: "); 
                    cust_ID = sc.nextInt();
                    where_transactions += "customer_ID = " + cust_ID;
                    start_wts = 1;
                }
                

                System.out.print("total (Yes/No): "); 
                if(output01.equals("Yes")){output02 = sc.nextLine();}
                output02 = sc.nextLine();
                while(!(output02.equals("Yes") || output02.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output02 = sc.nextLine();
                }
                if(output02.equals("Yes")){
                    System.out.print("total: "); 
                    tot = sc.nextFloat();
                    if(start_wts == 1){
                        where_transactions += " AND total = " + tot;
                    }
                    else{
                        where_transactions += "total = " + tot;
                        start_wts = 1;
                    }
                }

                System.out.print("UPC (Yes/No): "); 
                if(output02.equals("Yes")){output03 = sc.nextLine();}
                output03 = sc.nextLine();
                while(!(output03.equals("Yes") || output03.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output03 = sc.nextLine();
                }
                if(output03.equals("Yes")){
                    System.out.print("UPC: ");
                    upc = sc.nextLine();
                    where_transactionContains += "UPC = " + upc;
                    start_wtsc = 1;
                }
                
                System.out.print("quantity (Yes/No): "); 
                output04 = sc.nextLine();
                while(!(output04.equals("Yes") || output04.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output04 = sc.nextLine();
                }
                if(output04.equals("Yes")){
                    System.out.print("quantity: "); 
                    qua = sc.nextInt();
                    if(start_wtsc == 1){
                        where_transactionContains += " AND quantity = " + qua;
                    }
                    else{
                        where_transactionContains += "quantity = " + qua;
                        start_wtsc = 1;
                    }
                }

                System.out.print("\nOutput fields:\n");

                System.out.print("transaction_ID (Yes/No): "); 
                output1 = sc.nextLine();
                while(!(output1.equals("Yes") || output1.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output1 = sc.nextLine();
                }
                if(output1.equals("Yes")){
                    select_transactions += "transaction_ID";
                    start_ts = 1;
                    select_transactionContains += "transaction_ID";
                    start_tsc = 1;
                }

                System.out.print("customer_ID (Yes/No): "); 
                output2 = sc.nextLine();
                while(!(output2.equals("Yes") || output2.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output2 = sc.nextLine();
                }
                if(output2.equals("Yes")){
                    if(start_ts == 1){
                        select_transactions += ", customer_ID";
                    }
                    else{
                        select_transactions += "customer_ID";
                        start_ts = 1;
                    }
                }

                System.out.print("transaction_date (Yes/No): "); 
                output3 = sc.nextLine();
                while(!(output3.equals("Yes") || output3.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output3 = sc.nextLine();
                }
                if(output3.equals("Yes")){
                    if(start_ts == 1){
                        select_transactions += ", transaction_date";
                    }
                    else{
                        select_transactions += "transaction_date";
                        start_ts = 1;
                    }
                }

                System.out.print("payment_method (Yes/No): "); 
                output4 = sc.nextLine();
                while(!(output4.equals("Yes") || output4.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output4 = sc.nextLine();
                }
                if(output4.equals("Yes")){
                    if(start_ts == 1){
                        select_transactions += ", payment_method";
                    }
                    else{
                        select_transactions += "payment_method";
                        start_ts = 1;
                    }
                }
                
                System.out.print("total (Yes/No): "); 
                output5 = sc.nextLine();
                while(!(output5.equals("Yes") || output5.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output5 = sc.nextLine();
                }
                if(output5.equals("Yes")){
                    if(start_ts == 1){
                        select_transactions += ", total";
                    }
                    else{
                        select_transactions += "total";
                        start_ts = 1;
                    }
                }

                System.out.print("UPC (Yes/No): "); 
                output6 = sc.nextLine();
                while(!(output6.equals("Yes") || output6.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output6 = sc.nextLine();
                }
                if(output6.equals("Yes")){
                    if(start_tsc == 1){
                        select_transactionContains += ", UPC";
                    }
                    else{
                        select_transactionContains += "UPC";
                        start_tsc = 1;
                    }
                }

                System.out.print("quantity (Yes/No): "); 
                output7 = sc.nextLine();
                while(!(output7.equals("Yes") || output7.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output7 = sc.nextLine();
                }
                if(output7.equals("Yes")){
                    if(start_tsc == 1){
                        select_transactionContains += ", quantity";
                    }
                    else{
                        select_transactionContains += "quantity";
                        start_tsc = 1;
                    }
                }

                System.out.print("\nDistinct (Yes/No): "); 
                output8 = sc.nextLine();
                while(!(output8.equals("Yes") || output8.equals("No"))){
                    System.out.print("Your input is invalid, please enter Yes or No: ");  
                    output8 = sc.nextLine();
                }
                if(output8.equals("Yes")){
                    select_transactions = "Distinct " + select_transactions;
                    select_transactionContains = "Distinct " + select_transactionContains;
                }

                // User Input is finished

                if(output01.equals("No") && output02.equals("No") && output03.equals("No") && output04.equals("No")){
                    System.out.print("You input NOTHING in input fields so the search is fail.\n"); 
                }
                else if(output1.equals("No") && output2.equals("No") && output3.equals("No") && output4.equals("No")
                        && output5.equals("No") && output6.equals("No") && output7.equals("No") && output8.equals("No")){
                    System.out.print("You input NOTHING in onput fields so the search is fail.\n"); 
                }
                else{
                    
                    if(output01.equals("No") && output02.equals("No")){
                        System.out.print("No Transactions Table will be printed out.\n"); 
                    }
                    else{
                        // Print Transactions Table
                        System.out.print("The Transactions Table:\n"); 

                        if(output1.equals("Yes")){
                            System.out.format("%15s", "TRANSACTION_ID");
                            divisionline += "---------------";
                        }
                            
                        if(output2.equals("Yes")){
                            System.out.format("%15s", "CUSTOMER_ID");
                            divisionline += "---------------";
                        }

                        if(output3.equals("Yes")){
                            System.out.format("%25s", "TRANSACTION_DATE");
                            divisionline += "-------------------------";
                        }

                        if(output4.equals("Yes")){
                            System.out.format("%20s", "PAYMENT_METHOD");
                            divisionline += "--------------------";
                        }

                        if(output5.equals("Yes")){
                            System.out.format("%10s", "TOTAL");
                            divisionline += "----------";
                        }
                        divisionline += "\n";
                        System.out.print("\n" + divisionline);
			             divisionline = "";

                        sqlcommands = "SELECT " + select_transactions + " FROM Transactions WHERE " + where_transactions;
                        //System.out.println(sqlcommands);
                        pstmt = con.prepareStatement(sqlcommands);
                        rset = pstmt.executeQuery();
                        if(!rset.next()){
                            System.out.println("There are no matched tuples in the database.");
                        }
                        else{
                            sqlcommands = "SELECT " + select_transactions + " FROM Transactions WHERE " + where_transactions;
                            pstmt = con.prepareStatement(sqlcommands);
                            rset = pstmt.executeQuery();
                            while (rset.next()){   
                                index = 1;
                                if(output1.equals("Yes")){
                                    System.out.format("%15.0f", rset.getFloat(index));
                                    index +=1;
                                }
                                
                                if(output2.equals("Yes")){
                                    System.out.format("%15.0f", rset.getFloat(index));
                                    index +=1;
                                }

                                if(output3.equals("Yes")){
                                    System.out.format("%25s", rset.getString(index));
                                    index +=1;
                                }

                                if(output4.equals("Yes")){
                                    System.out.format("%20s", rset.getString(index));
                                    index +=1;
                                }

                                if(output5.equals("Yes")){
                                    System.out.format("%10.2f", rset.getFloat(index));
                                    index +=1;
                                }

                                System.out.print("\n");
                            }
                        }
                    }

                    // Print Transaction_Contains Table
                    if(output03.equals("No") && output04.equals("No")){
                        System.out.print("No Transaction_Contains Table will be printed out.\n"); 
                    }
                    else{
                        System.out.print("The Transaction_Contains Table:\n"); 

                        if(output1.equals("Yes")){
                            System.out.format("%15s", "TRANSACTION_ID");
                            divisionline += "---------------";
                        }
                            
                        if(output6.equals("Yes")){
                            System.out.format("%15s", "UPC");
                            divisionline += "---------------";
                        }

                        if(output7.equals("Yes")){
                            System.out.format("%15s", "QUANTITY");
                            divisionline += "---------------";
                        }

                        divisionline += "\n";
                        System.out.print("\n" + divisionline);
			            divisionline = "";

                        sqlcommands = "SELECT " + select_transactionContains + " FROM Transaction_Contains WHERE " + where_transactionContains;
                        //System.out.println(sqlcommands);
                        pstmt = con.prepareStatement(sqlcommands);
                        rset = pstmt.executeQuery();
                        if(!rset.next()){
                            System.out.println("There are no matched tuples in the database.");
                        }
                        else{
                            sqlcommands = "SELECT " + select_transactionContains + " FROM Transaction_Contains WHERE " + where_transactionContains;
                            pstmt = con.prepareStatement(sqlcommands);
                            rset = pstmt.executeQuery();
                            while (rset.next()){
                                index = 1;
                                if(output1.equals("Yes")){
                                    System.out.format("%15.0f", rset.getFloat(index));
                                    index +=1;
                                }
                                
                                if(output6.equals("Yes")){
                                    System.out.format("%15s", rset.getString(index));
                                    index +=1;
                                }

                                if(output7.equals("Yes")){
                                    System.out.format("%15.0f", rset.getFloat(index));
                                    index +=1;
                                }

                                System.out.print("\n");
                            }
                        }
                    }
                }
            }
            else if(user_option == 4){
                System.out.print("Exit the program.\n");
            }
            else{
                System.out.print("The option number you input is invalid. Please enter the correct option number.\n");
            }
        }

        }
        catch( Exception e) {e.printStackTrace();}

    }// End of connectToDatabase()
}// End of class

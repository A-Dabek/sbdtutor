package sbdproject;

import java.sql.*;

//klasa obsługująca połączenie się z bazą danych

public class DBcontrol {
    
    //zmienne kontekstowe
    static UserAccount context_user = null;
    static Course context_course = null;
     
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    private static Connection conn = null;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
    
    public static Connection getConnection() {
        return conn;
    }
    
    public static void dbConnect(String DB_USER, String DB_PASS) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver is missing");
        }
        
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Connected as " + DB_USER);
        } catch (SQLException e) {
            System.out.println("Connection failed" + e.getMessage());
        }
    }
    
    public static void dbDisconnect() {
        try {
            System.out.println("Disconnected");
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Connection failed" + e.getMessage());
        }
    }
    
}

package COMU_Havayolu_1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
	
	public static final String DB_URL = "jdbc:mysql://localhost/deneme";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    public static void main(String[] args) {
    	
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Bağlantı başarılı!");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}

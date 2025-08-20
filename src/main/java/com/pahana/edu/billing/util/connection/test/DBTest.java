import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.pahana.edu.billing.util.DBConnection;

public class DBTest {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, username, role FROM users")) {

            while (rs.next()) {
                System.out.println("User: " + rs.getString("username") + " | Role: " + rs.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ViewMyComplaints {
    public static void viewMyComplaints(String studentRegisterNumber, Scanner scanner) {
        // Get a connection to the database
        try (Connection connection = JDBCConnection.getConnection()) {
            // Prepare the SQL statement to query the complaints
            String sql = "SELECT id, category, description, status, created_at FROM complaints WHERE register_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, studentRegisterNumber);

                // Execute the SQL statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if any complaints are found
                    boolean complaintsFound = false;
                    while (resultSet.next()) {
                        complaintsFound = true;
                        int id = resultSet.getInt("id");
                        String category = resultSet.getString("category");
                        String description = resultSet.getString("description");
                        String status = resultSet.getString("status");
                        String createdAt = resultSet.getString("created_at");

                        // Print the complaint details
                        System.out.println("Complaint ID: " + id);
                        System.out.println("Category: " + category);
                        System.out.println("Description: " + description);
                        System.out.println("Status: " + status);
                        System.out.println("Created At: " + createdAt);
                        System.out.println();
                    }

                    // If no complaints are found, print a message
                    if (!complaintsFound) {
                        System.out.println("No complaints found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve complaints.");
            e.printStackTrace();
        }
    }
}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class RaiseComplaint {
    public static void raiseComplaint(String studentRegisterNumber, Scanner scanner) {
        // Prompt the user for input
        System.out.print("Enter the category of your complaint: ");
        String category = scanner.nextLine();
        System.out.print("Enter a description of your complaint: ");
        String description = scanner.nextLine();

        // Get a connection to the database
        try (Connection connection = JDBCConnection.getConnection()) {
            // Prepare the SQL statement to insert the complaint
            String sql = "INSERT INTO complaints (register_number, category, description) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, studentRegisterNumber);
                preparedStatement.setString(2, category);
                preparedStatement.setString(3, description);

                // Execute the SQL statement
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the insert was successful
                if (rowsAffected == 1) {
                    // Get the generated complaint ID
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int complaintId = generatedKeys.getInt(1);
                            System.out.println("Complaint submitted successfully. Your complaint ID is: " + complaintId);
                        }
                    }
                } else {
                    System.out.println("Failed to submit complaint.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to submit complaint.");
            e.printStackTrace();
        }
    }
}
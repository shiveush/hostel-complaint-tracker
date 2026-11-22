import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminPanel {
    public static void viewAllComplaints(Scanner scanner) {
        try (Connection connection = JDBCConnection.getConnection()) {
            String sql = "SELECT c.id, c.register_number, s.name, c.category, c.description, c.status, c.created_at " +
                         "FROM complaints c " +
                         "JOIN students s ON c.register_number = s.register_number " +
                         "ORDER BY c.created_at DESC";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String registerNumber = resultSet.getString("register_number");
                    String name = resultSet.getString("name");
                    String category = resultSet.getString("category");
                    String description = resultSet.getString("description");
                    String status = resultSet.getString("status");
                    String createdAt = resultSet.getString("created_at");

                    System.out.println("Complaint ID: " + id);
                    System.out.println("Register Number: " + registerNumber);
                    System.out.println("Student Name: " + name);
                    System.out.println("Category: " + category);
                    System.out.println("Description: " + description);
                    System.out.println("Status: " + status);
                    System.out.println("Created At: " + createdAt);
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve complaints.");
            e.printStackTrace();
        }
    }

    public static void updateComplaintStatus(Scanner scanner) {
        int complaintId;
        String newStatus;

        System.out.print("Enter the complaint ID: ");
        complaintId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        while (true) {
            System.out.print("Enter the new status (Pending/In Progress/Resolved): ");
            newStatus = scanner.nextLine();
            if (newStatus.equalsIgnoreCase("Pending") || newStatus.equalsIgnoreCase("In Progress") || newStatus.equalsIgnoreCase("Resolved")) {
                break;
            } else {
                System.out.println("Invalid status. Please try again.");
            }
        }

        try (Connection connection = JDBCConnection.getConnection()) {
            String sql = "UPDATE complaints SET status = ? WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newStatus);
                preparedStatement.setInt(2, complaintId);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected == 1) {
                    System.out.println("Status updated successfully.");
                } else {
                    System.out.println("Complaint ID not found.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to update complaint status.");
            e.printStackTrace();
        }
    }
}
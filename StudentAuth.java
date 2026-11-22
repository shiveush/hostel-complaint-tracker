import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class StudentAuth {
    public static void registerStudent(Scanner scanner) throws SQLIntegrityConstraintViolationException, SQLException {
        // Prompt the user for input
        System.out.print("Enter your register number: ");
        String registerNumber = scanner.nextLine();
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Hash the password using SHA-256
        String hashedPassword = hashPassword(password);

        // Get a connection to the database
        try (Connection connection = JDBCConnection.getConnection()) {
            // Prepare the SQL statement to insert the student
            String sql = "INSERT INTO students (register_number, name, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, registerNumber);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, hashedPassword);

                // Execute the SQL statement
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the insert was successful
                if (rowsAffected == 1) {
                    System.out.println("Student registered successfully.");
                } else {
                    throw new SQLException("Failed to register student.");
                }
            }
        }
    }

    public static String loginStudent(Scanner scanner) {
        // Prompt the user for input
        System.out.print("Enter your register number: ");
        String registerNumber = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        // Hash the password using SHA-256
        String hashedPassword = hashPassword(password);

        // Get a connection to the database
        try (Connection connection = JDBCConnection.getConnection()) {
            // Prepare the SQL statement to check the student's credentials
            String sql = "SELECT password FROM students WHERE register_number = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, registerNumber);

                // Execute the SQL statement
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        if (storedPassword.equals(hashedPassword)) {
                            return registerNumber;
                        } else {
                            System.out.println("Invalid password.");
                        }
                    } else {
                        System.out.println("Student not found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to login student.");
            e.printStackTrace();
        }

        return null;
    }

    private static String hashPassword(String password) {
        try {
            // Create a SHA-256 message digest
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
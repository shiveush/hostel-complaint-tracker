import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ComplaintTrackerApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String studentRegisterNumber = null;
        String adminPassword = "admin123";

        while (true) {
            System.out.println("\nComplaint Tracker App");
            System.out.println("1. Student Login");
            System.out.println("2. Student Register (new student)");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = readInt(scanner);
            scanner.nextLine(); // consume leftover newline
            switch (choice) {
                case 1:
                    studentRegisterNumber = StudentAuth.loginStudent(scanner);
                    if (studentRegisterNumber != null) {
                        System.out.println("Student login successful.");
                        studentMenu(scanner, studentRegisterNumber);
                        studentRegisterNumber = null; // reset after returning from menu
                    }
                    else {
                        System.out.println("Student login failed.");
                    }
                    break;
                case 2:
                    try {
                        StudentAuth.registerStudent(scanner);
                    } catch (SQLIntegrityConstraintViolationException e) {
                        System.out.println("Register number already exists, please login instead.");
                    } catch (SQLException e) {
                        System.err.println("Failed to register student.");
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.print("Enter admin password: ");
                    String enteredPassword = scanner.nextLine();
                    if (enteredPassword.equals(adminPassword)) {
                        System.out.println("Admin login successful.");
                        adminMenu(scanner);
                    }
                    else {
                        System.out.println("Invalid admin password.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            // Continue to main menu after each operation
        }
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                scanner.nextLine(); // consume invalid input
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void studentMenu(Scanner scanner, String studentRegisterNumber) {
        int choice;

        while (true) {
            System.out.println("\nStudent Menu");
            System.out.println("1. Raise a complaint");
            System.out.println("2. View my complaints");
            System.out.println("3. Return to main menu");
            System.out.print("Enter your choice: ");
            choice = readInt(scanner);
            scanner.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    RaiseComplaint.raiseComplaint(studentRegisterNumber, scanner);
                    break;
                case 2:
                    ViewMyComplaints.viewMyComplaints(studentRegisterNumber, scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminMenu(Scanner scanner) {
        int choice;

        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. View all complaints");
            System.out.println("2. Update complaint status");
            System.out.println("3. Return to main menu");
            System.out.print("Enter your choice: ");
            choice = readInt(scanner);
            scanner.nextLine(); // consume leftover newline

            switch (choice) {
                case 1:
                    AdminPanel.viewAllComplaints(scanner);
                    break;
                case 2:
                    AdminPanel.updateComplaintStatus(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
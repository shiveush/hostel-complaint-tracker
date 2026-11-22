# Complaint Tracker — College/Hostel Complaint Management System

A console-based Java application that lets students raise and track complaints (maintenance, mess, wifi, safety, etc.) at their college or hostel, while admins manage and resolve them through a simple status workflow.

## Problem

Students in colleges and hostels often have no structured way to report issues — complaints get lost in word-of-mouth or informal messages, with no record of what was raised, by whom, or whether it was resolved. This project solves that with a simple, auditable complaint-tracking system.

## Features

- **Student registration & login** — register number + password authentication, passwords hashed with SHA-256 before storage
- **Raise a complaint** — category, description, timestamped automatically
- **View my complaints** — students can check the status of their own submitted complaints
- **Admin login** — password-protected admin access
- **View all complaints** — admin sees every complaint with the student's register number *and* name (via SQL join), filterable by status/category
- **Update complaint status** — admin moves complaints through Pending → In Progress → Resolved
- **Input validation** — handles invalid menu input and duplicate registrations gracefully without crashing

## Tech Stack

- **Language:** Core Java (JDK, no frameworks)
- **Database:** MySQL / MariaDB
- **Connectivity:** JDBC (MySQL Connector/J)
- **Security:** SHA-256 password hashing, `PreparedStatement` throughout to prevent SQL injection

No Maven/Gradle — plain `.java` files, compiled and run directly, to keep the project lightweight and dependency-free.

## Database Schema

See [`schema.sql`](./schema.sql) for the full setup. Summary:

```sql
CREATE TABLE students (
    register_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    register_number VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(500) NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (register_number) REFERENCES students(register_number)
);
```

## How to Run

### Prerequisites
- JDK installed
- MySQL or MariaDB server running
- MySQL Connector/J (JDBC driver) — [download here](https://dev.mysql.com/downloads/connector/j/)

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/<your-username>/<repo-name>.git
   cd <repo-name>
   ```

2. **Create the database**
   ```bash
   mysql -u root -p < schema.sql
   ```

3. **Add the JDBC driver**
   - Place the downloaded `mysql-connector-j-x.x.x.jar` into a `lib/` folder in the project root

4. **Set your database credentials**
   - Open `JDBCConnection.java`
   - Replace `YOUR_MYSQL_USERNAME` and `YOUR_MYSQL_PASSWORD` with your actual MySQL credentials

5. **Compile and run**
   ```bash
   javac -cp lib/mysql-connector-j-x.x.x.jar *.java
   java -cp .:lib/mysql-connector-j-x.x.x.jar ComplaintTrackerApp
   ```
   *(On Windows, use `;` instead of `:` in the classpath.)*

## Sample Usage

```
Complaint Tracker App
1. Student Login
2. Student Register (new student)
3. Admin Login
4. Exit
Enter your choice: 2
Enter your register number: 713923104100
Enter your name: Shiveush P
Enter your password: ********
Student registered successfully.
```

## Project Structure

```
.
├── ComplaintTrackerApp.java   # Main entry point & menu logic
├── JDBCConnection.java        # Database connection handling
├── StudentAuth.java           # Student registration & login (SHA-256 hashed passwords)
├── RaiseComplaint.java        # Student: raise a complaint
├── ViewMyComplaints.java      # Student: view own complaints
├── AdminPanel.java            # Admin: view all / update complaint status
├── schema.sql                 # Database schema
└── README.md
```

## Future Improvements

- Admin table with hashed passwords (currently a single hardcoded admin password, sufficient for this project's scope)
- Photo attachment support for complaints
- Email notifications on status change

## Author

Shiveush P — B.E. Computer Science Engineering student
# Student Management System - Prototype

Java-based interactive prototype for a Student Management System with CSV persistence, validation, and full CRUD operations.

## Prototype Screens
1. **Login (`LoginPrototype.java`)**: User authentication screen.
   - **Default Credentials**: Username `admin` / Password `admin` (or `1234`)
2. **Dashboard (`DashboardPrototype.java`)**: Main navigation hub displaying total student metrics and actions.
3. **Add / Update Student (`AddStudentPrototype.java`)**: Form for adding new students or updating existing student profiles with input validation.
4. **Student List (`StudentListPrototype.java`)**: Dynamic table with live search/filter, update, delete, and refresh actions.

## Data & Persistence
- **Sample Data**: Stored in `data/students.csv`. Data persists across sessions when adding, updating, or deleting students.
- **SQL Schema & Reference Data**: See `database/student_management.sql`.

## How to Run

### Option 1: Quick Run (Batch Script)
Double-click `run.bat` in the root folder, or run:
```cmd
run.bat
```

### Option 2: Manual Terminal Execution
Open terminal in the `prototype` folder:
```cmd
cd prototype
javac *.java
java LoginPrototype
```

You can also run any screen directly:
- `java DashboardPrototype`
- `java AddStudentPrototype`
- `java StudentListPrototype`

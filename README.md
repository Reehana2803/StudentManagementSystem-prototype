# Student Management System - Prototype

Java-based interactive prototype for a Student Management System with CSV persistence, validation, and full CRUD operations.

## 📊 Current Registered Students (`data/students.csv`)

| ID | Student Name | Age | Department | Phone | Email |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`101`** | Reehana shaik | 20 | CSE | 9876543210 | `reehana@gmail.com` |
| **`102`** | Asha shaik | 20 | ECE | 9876543211 | `asha@gmail.com` |
| **`103`** | Mohith | 20 | ECE | 9876543210 | `mohith@gmail.com` |
| **`12345566`** | shaik munthaj | 20 | CSE | 9391737983 | `munthaj@gmail.com` |
| **`104`** | R NEHA | 19 | CSE | 9876543210 | `neha@gmail.com` |
| **`105`** | sk karishma | 19 | DS | 6478283783 | `karishma@gmail.com` |

---

## 🖥️ Prototype Screens
1. **Login (`LoginPrototype.java`)**: User authentication screen.
   - **Default Credentials**: Username `admin` / Password `admin` (or `1234`)
2. **Dashboard (`DashboardPrototype.java`)**: Main navigation hub displaying total student metrics and actions (*Add, View, Update, Delete, Search, and Logout*).
3. **Add / Update Student (`AddStudentPrototype.java`)**: Dual-mode form with full client validation (positive integer ID check, uniqueness check, age bounds, phone regex, email pattern).
4. **Student List (`StudentListPrototype.java`)**: Interactive table dynamically reading from CSV with live search/filter, row selection for updates and deletions, and refresh.
5. **Interactive Web Dashboard (`index.html`)**: Interactive output viewer and prototype dashboard.

---

## 💾 Data & Persistence
- **Sample Data**: Stored in `data/students.csv`. Data persists across sessions when adding, updating, or deleting students.
- **SQL Schema & Reference Data**: See `database/student_management.sql`.

---

## 🚀 How to Run

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

You can also run any individual screen directly:
- `java DashboardPrototype`
- `java AddStudentPrototype`
- `java StudentListPrototype`

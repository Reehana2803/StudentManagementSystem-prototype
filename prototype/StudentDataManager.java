import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StudentDataManager {

    public static File getCsvFile() {
        File f1 = new File("data/students.csv");
        if (f1.exists()) return f1;

        File f2 = new File("../data/students.csv");
        if (f2.exists()) return f2;

        File currentDir = new File(".").getAbsoluteFile();
        if (currentDir.getName().equalsIgnoreCase("prototype")) {
            return new File(currentDir.getParentFile(), "data/students.csv");
        }
        return f1;
    }

    public static synchronized List<Student> loadStudents() {
        List<Student> list = new ArrayList<>();
        File file = getCsvFile();
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (isFirstLine) {
                    isFirstLine = false;
                    if (line.toLowerCase().startsWith("id,")) {
                        continue;
                    }
                }
                String[] parts = parseCsvLine(line);
                if (parts.length >= 6) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        int age = Integer.parseInt(parts[2].trim());
                        String dept = parts[3].trim();
                        String phone = parts[4].trim();
                        String email = parts[5].trim();
                        list.add(new Student(id, name, age, dept, phone, email));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static synchronized boolean saveAllStudents(List<Student> students) {
        File file = getCsvFile();
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            pw.println("id,name,age,department,phone,email");
            for (Student s : students) {
                pw.println(s.toCsvLine());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static synchronized boolean addStudent(Student newStudent) {
        List<Student> students = loadStudents();
        for (Student s : students) {
            if (s.getId() == newStudent.getId()) {
                return false; // Duplicate ID
            }
        }
        students.add(newStudent);
        return saveAllStudents(students);
    }

    public static synchronized boolean updateStudent(Student updatedStudent) {
        List<Student> students = loadStudents();
        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == updatedStudent.getId()) {
                students.set(i, updatedStudent);
                found = true;
                break;
            }
        }
        if (found) {
            return saveAllStudents(students);
        }
        return false;
    }

    public static synchronized boolean deleteStudent(int id) {
        List<Student> students = loadStudents();
        boolean removed = students.removeIf(s -> s.getId() == id);
        if (removed) {
            return saveAllStudents(students);
        }
        return false;
    }

    public static synchronized Student getStudentById(int id) {
        List<Student> students = loadStudents();
        for (Student s : students) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public static synchronized List<Student> searchStudents(String query) {
        List<Student> all = loadStudents();
        if (query == null || query.trim().isEmpty()) {
            return all;
        }
        String q = query.trim().toLowerCase();
        List<Student> result = new ArrayList<>();
        for (Student s : all) {
            if (String.valueOf(s.getId()).contains(q) ||
                s.getName().toLowerCase().contains(q) ||
                s.getDepartment().toLowerCase().contains(q) ||
                s.getPhone().toLowerCase().contains(q) ||
                s.getEmail().toLowerCase().contains(q)) {
                result.add(s);
            }
        }
        return result;
    }

    private static String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardPrototype {

    private static JFrame currentFrame;

    public static void openDashboard() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        if (currentFrame != null) {
            currentFrame.dispose();
        }

        JFrame frame = new JFrame("Student Management System - Dashboard");
        currentFrame = frame;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(440, 430);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        // Header Title
        JLabel title = new JLabel("STUDENT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setBounds(20, 20, 390, 30);
        frame.add(title);

        // Subtitle / Counter
        int studentCount = StudentDataManager.loadStudents().size();
        JLabel countLabel = new JLabel("Total Students Registered: " + studentCount, SwingConstants.CENTER);
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        countLabel.setForeground(new Color(60, 60, 60));
        countLabel.setBounds(20, 50, 390, 20);
        frame.add(countLabel);

        // Action Buttons
        JButton add = new JButton("Add Student");
        JButton view = new JButton("View Students");
        JButton update = new JButton("Update Student");
        JButton delete = new JButton("Delete Student");
        JButton search = new JButton("Search Student");
        JButton logout = new JButton("Logout");

        Font btnFont = new Font("SansSerif", Font.PLAIN, 13);
        add.setFont(btnFont);
        view.setFont(btnFont);
        update.setFont(btnFont);
        delete.setFont(btnFont);
        search.setFont(btnFont);
        logout.setFont(btnFont);

        int startY = 80;
        int btnWidth = 220;
        int btnHeight = 35;
        int gap = 45;
        int startX = 105;

        add.setBounds(startX, startY, btnWidth, btnHeight);
        view.setBounds(startX, startY + gap, btnWidth, btnHeight);
        update.setBounds(startX, startY + gap * 2, btnWidth, btnHeight);
        delete.setBounds(startX, startY + gap * 3, btnWidth, btnHeight);
        search.setBounds(startX, startY + gap * 4, btnWidth, btnHeight);
        logout.setBounds(startX, startY + gap * 5, btnWidth, btnHeight);

        frame.add(add);
        frame.add(view);
        frame.add(update);
        frame.add(delete);
        frame.add(search);
        frame.add(logout);

        // 1. Add Student Action
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                AddStudentPrototype.openAddStudent(frame);
            }
        });

        // 2. View Students Action
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                StudentListPrototype.openStudentList(frame);
            }
        });

        // 3. Update Student Action
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame,
                        "Enter Student ID to update:",
                        "Update Student",
                        JOptionPane.QUESTION_MESSAGE);
                if (input == null || input.trim().isEmpty()) {
                    return;
                }
                try {
                    int id = Integer.parseInt(input.trim());
                    Student s = StudentDataManager.getStudentById(id);
                    if (s == null) {
                        JOptionPane.showMessageDialog(frame,
                                "No student found with ID: " + id,
                                "Not Found",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        frame.setVisible(false);
                        AddStudentPrototype.openEditStudent(frame, s);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid numeric Student ID.",
                            "Invalid ID",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 4. Delete Student Action
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(frame,
                        "Enter Student ID to delete:",
                        "Delete Student",
                        JOptionPane.QUESTION_MESSAGE);
                if (input == null || input.trim().isEmpty()) {
                    return;
                }
                try {
                    int id = Integer.parseInt(input.trim());
                    Student s = StudentDataManager.getStudentById(id);
                    if (s == null) {
                        JOptionPane.showMessageDialog(frame,
                                "No student found with ID: " + id,
                                "Not Found",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        int confirm = JOptionPane.showConfirmDialog(frame,
                                "Are you sure you want to delete student: " + s.getName() + " (ID: " + s.getId() + ")?",
                                "Confirm Deletion",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean deleted = StudentDataManager.deleteStudent(id);
                            if (deleted) {
                                JOptionPane.showMessageDialog(frame,
                                        "Student deleted successfully!",
                                        "Deleted",
                                        JOptionPane.INFORMATION_MESSAGE);
                                countLabel.setText("Total Students Registered: " + StudentDataManager.loadStudents().size());
                            } else {
                                JOptionPane.showMessageDialog(frame,
                                        "Could not delete student.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid numeric Student ID.",
                            "Invalid ID",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 5. Search Student Action
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = JOptionPane.showInputDialog(frame,
                        "Enter ID, Name, or Department to search:",
                        "Search Student",
                        JOptionPane.QUESTION_MESSAGE);
                if (query == null || query.trim().isEmpty()) {
                    return;
                }
                List<Student> results = StudentDataManager.searchStudents(query.trim());
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "No students matched your search for: \"" + query + "\"",
                            "Search Results",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Found ").append(results.size()).append(" matching student(s):\n\n");
                    for (Student s : results) {
                        sb.append(String.format("ID: %d | Name: %s | Age: %d | Dept: %s | Phone: %s | Email: %s\n",
                                s.getId(), s.getName(), s.getAge(), s.getDepartment(), s.getPhone(), s.getEmail()));
                    }
                    JOptionPane.showMessageDialog(frame,
                            sb.toString(),
                            "Search Results (" + results.size() + ")",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        // 6. Logout Action
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to log out?",
                        "Logout Confirmation",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    LoginPrototype.openLogin();
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                openDashboard();
            }
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddStudentPrototype {

    public static void openAddStudent(JFrame parentFrame) {
        showForm(parentFrame, null);
    }

    public static void openEditStudent(JFrame parentFrame, Student student) {
        showForm(parentFrame, student);
    }

    private static void showForm(JFrame parentFrame, Student studentToEdit) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        boolean isEditMode = (studentToEdit != null);
        String windowTitle = isEditMode ? "Update Student (ID: " + studentToEdit.getId() + ")" : "Add Student";

        JFrame frame = new JFrame(windowTitle);
        frame.setSize(440, 430);
        frame.setLocationRelativeTo(parentFrame != null ? parentFrame : null);
        frame.setLayout(null);
        frame.setResizable(false);

        // Header Title
        JLabel titleLabel = new JLabel(isEditMode ? "UPDATE STUDENT DETAILS" : "REGISTER NEW STUDENT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setBounds(30, 15, 370, 25);
        frame.add(titleLabel);

        String[] labels = {"Student ID:", "Name:", "Age:", "Department:", "Phone:", "Email:"};
        JTextField[] fields = new JTextField[labels.length];

        int startY = 55;
        int gapY = 42;

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("SansSerif", Font.PLAIN, 13));
            fields[i] = new JTextField();
            label.setBounds(40, startY + i * gapY, 110, 30);
            fields[i].setBounds(160, startY + i * gapY, 230, 30);
            frame.add(label);
            frame.add(fields[i]);
        }

        // If editing, populate the fields
        if (isEditMode) {
            fields[0].setText(String.valueOf(studentToEdit.getId()));
            fields[0].setEditable(false);
            fields[0].setToolTipText("Student ID cannot be changed.");
            fields[1].setText(studentToEdit.getName());
            fields[2].setText(String.valueOf(studentToEdit.getAge()));
            fields[3].setText(studentToEdit.getDepartment());
            fields[4].setText(studentToEdit.getPhone());
            fields[5].setText(studentToEdit.getEmail());
        }

        JButton saveBtn = new JButton(isEditMode ? "UPDATE" : "SAVE");
        JButton clearBtn = new JButton("CLEAR");
        JButton backBtn = new JButton("BACK");

        Font btnFont = new Font("SansSerif", Font.BOLD, 12);
        saveBtn.setFont(btnFont);
        clearBtn.setFont(btnFont);
        backBtn.setFont(btnFont);

        int btnY = startY + labels.length * gapY + 10;
        saveBtn.setBounds(50, btnY, 95, 35);
        clearBtn.setBounds(160, btnY, 95, 35);
        backBtn.setBounds(270, btnY, 95, 35);

        frame.add(saveBtn);
        frame.add(clearBtn);
        frame.add(backBtn);

        // Save / Update Action
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idStr = fields[0].getText().trim();
                String name = fields[1].getText().trim();
                String ageStr = fields[2].getText().trim();
                String dept = fields[3].getText().trim();
                String phone = fields[4].getText().trim();
                String email = fields[5].getText().trim();

                // Validation 1: Empty check
                if (idStr.isEmpty() || name.isEmpty() || ageStr.isEmpty() || dept.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please fill in all fields before saving.",
                            "Validation Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validation 2: ID
                int id;
                try {
                    id = Integer.parseInt(idStr);
                    if (id <= 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Student ID must be a positive integer.",
                            "Invalid ID",
                            JOptionPane.ERROR_MESSAGE);
                    fields[0].requestFocus();
                    return;
                }

                // Validation 3: Age
                int age;
                try {
                    age = Integer.parseInt(ageStr);
                    if (age < 5 || age > 120) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid age between 5 and 120.",
                            "Invalid Age",
                            JOptionPane.ERROR_MESSAGE);
                    fields[2].requestFocus();
                    return;
                }

                // Validation 4: Phone
                if (phone.length() < 7 || phone.length() > 15 || !phone.matches("[0-9+\\- ]+")) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid phone number (digits only, 7-15 characters).",
                            "Invalid Phone",
                            JOptionPane.ERROR_MESSAGE);
                    fields[4].requestFocus();
                    return;
                }

                // Validation 5: Email
                if (!email.contains("@") || !email.contains(".")) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid email address.",
                            "Invalid Email",
                            JOptionPane.ERROR_MESSAGE);
                    fields[5].requestFocus();
                    return;
                }

                Student student = new Student(id, name, age, dept, phone, email);

                if (isEditMode) {
                    boolean success = StudentDataManager.updateStudent(student);
                    if (success) {
                        JOptionPane.showMessageDialog(frame,
                                "Student updated successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        if (parentFrame != null) {
                            parentFrame.setVisible(true);
                            if (parentFrame instanceof Runnable) {
                                ((Runnable) parentFrame).run();
                            }
                        } else {
                            DashboardPrototype.openDashboard();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to update student.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Check duplicate
                    if (StudentDataManager.getStudentById(id) != null) {
                        JOptionPane.showMessageDialog(frame,
                                "A student with ID " + id + " already exists!\nPlease choose another ID.",
                                "Duplicate ID",
                                JOptionPane.WARNING_MESSAGE);
                        fields[0].requestFocus();
                        return;
                    }

                    boolean success = StudentDataManager.addStudent(student);
                    if (success) {
                        int choice = JOptionPane.showConfirmDialog(frame,
                                "Student registered successfully!\nWould you like to add another student?",
                                "Success",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE);
                        if (choice == JOptionPane.YES_OPTION) {
                            for (JTextField f : fields) {
                                f.setText("");
                            }
                            fields[0].requestFocus();
                        } else {
                            frame.dispose();
                            if (parentFrame != null) {
                                parentFrame.setVisible(true);
                            } else {
                                DashboardPrototype.openDashboard();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to save student.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Clear Action
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isEditMode) {
                    fields[0].setText("");
                }
                for (int i = 1; i < fields.length; i++) {
                    fields[i].setText("");
                }
                fields[isEditMode ? 1 : 0].requestFocus();
            }
        });

        // Back Action
        ActionListener backAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                } else {
                    DashboardPrototype.openDashboard();
                }
            }
        };
        backBtn.addActionListener(backAction);

        // Window closing behavior
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                } else {
                    DashboardPrototype.openDashboard();
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                openAddStudent(null);
            }
        });
    }
}

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class StudentListPrototype implements Runnable {

    private static JFrame currentFrame;
    private static DefaultTableModel tableModel;
    private static JTable table;
    private static JTextField searchField;
    private static JLabel statusLabel;

    public static void openStudentList(JFrame parentFrame) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        if (currentFrame != null) {
            currentFrame.dispose();
        }

        JFrame frame = new JFrame("Student List - Student Management System");
        currentFrame = frame;
        frame.setSize(800, 480);
        frame.setLocationRelativeTo(parentFrame != null ? parentFrame : null);
        frame.setLayout(new BorderLayout(10, 10));

        // Top Panel: Search & Controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        JLabel searchLabel = new JLabel("Search (ID / Name / Dept / Email):");
        searchLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(resetBtn);

        frame.add(topPanel, BorderLayout.NORTH);

        // Center Panel: Table
        String[] columns = {"ID", "Name", "Age", "Department", "Phone", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only view
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Adjust column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(140); // Name
        table.getColumnModel().getColumn(2).setPreferredWidth(50);  // Age
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Department
        table.getColumnModel().getColumn(4).setPreferredWidth(110); // Phone
        table.getColumnModel().getColumn(5).setPreferredWidth(180); // Email

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel: Action Buttons and Status
        JPanel southContainer = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));

        JButton addBtn = new JButton("Add Student");
        JButton editBtn = new JButton("Update Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back to Dashboard");

        Font btnFont = new Font("SansSerif", Font.PLAIN, 12);
        addBtn.setFont(btnFont);
        editBtn.setFont(btnFont);
        deleteBtn.setFont(btnFont);
        refreshBtn.setFont(btnFont);
        backBtn.setFont(btnFont);

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);

        statusLabel = new JLabel(" Total: 0 students", SwingConstants.LEFT);
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 12, 5, 12));

        southContainer.add(buttonPanel, BorderLayout.CENTER);
        southContainer.add(statusLabel, BorderLayout.SOUTH);
        frame.add(southContainer, BorderLayout.SOUTH);

        // Load Initial Data
        refreshTableData(null);

        // Search Handlers
        ActionListener performSearch = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim();
                refreshTableData(query);
            }
        };
        searchBtn.addActionListener(performSearch);
        searchField.addActionListener(performSearch);

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                refreshTableData(null);
            }
        });

        // Real-time search filter as user types
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText().trim();
                refreshTableData(text.isEmpty() ? null : text);
            }
        });

        // Add Student
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                AddStudentPrototype.openAddStudent(frame);
            }
        });

        // Update Student
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame,
                            "Please select a student from the table to update.",
                            "No Student Selected",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                Student s = StudentDataManager.getStudentById(studentId);
                if (s != null) {
                    frame.setVisible(false);
                    AddStudentPrototype.openEditStudent(frame, s);
                }
            }
        });

        // Delete Student
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame,
                            "Please select a student from the table to delete.",
                            "No Student Selected",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int studentId = (int) tableModel.getValueAt(selectedRow, 0);
                String studentName = (String) tableModel.getValueAt(selectedRow, 1);

                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete student: " + studentName + " (ID: " + studentId + ")?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = StudentDataManager.deleteStudent(studentId);
                    if (success) {
                        JOptionPane.showMessageDialog(frame,
                                "Student deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshTableData(searchField.getText().trim());
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Failed to delete student.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Refresh Button
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTableData(searchField.getText().trim());
            }
        });

        // Back to Dashboard
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                } else {
                    DashboardPrototype.openDashboard();
                }
            }
        });

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

    private static void refreshTableData(String query) {
        if (tableModel == null) return;
        tableModel.setRowCount(0);

        List<Student> students = (query == null || query.isEmpty())
                ? StudentDataManager.loadStudents()
                : StudentDataManager.searchStudents(query);

        for (Student s : students) {
            tableModel.addRow(s.toTableRow());
        }

        if (statusLabel != null) {
            if (query != null && !query.isEmpty()) {
                statusLabel.setText(" Showing " + students.size() + " matching student(s) for \"" + query + "\"");
            } else {
                statusLabel.setText(" Total: " + students.size() + " student(s) registered");
            }
        }
    }

    @Override
    public void run() {
        refreshTableData(searchField != null ? searchField.getText().trim() : null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                openStudentList(null);
            }
        });
    }
}

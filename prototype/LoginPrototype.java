import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPrototype {

    public static void openLogin() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        JFrame frame = new JFrame("Student Management System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 290);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setResizable(false);

        // Header Title
        JLabel titleLabel = new JLabel("System Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBounds(40, 15, 330, 30);
        frame.add(titleLabel);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userLabel.setBounds(45, 60, 90, 30);
        JTextField username = new JTextField();
        username.setBounds(145, 60, 215, 30);
        frame.add(userLabel);
        frame.add(username);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        passLabel.setBounds(45, 105, 90, 30);
        JPasswordField password = new JPasswordField();
        password.setBounds(145, 105, 215, 30);
        frame.add(passLabel);
        frame.add(password);

        // Hint
        JLabel hintLabel = new JLabel("Default login: admin / admin", SwingConstants.CENTER);
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setBounds(45, 142, 315, 20);
        frame.add(hintLabel);

        // Buttons
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginBtn.setBounds(145, 175, 100, 32);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        clearBtn.setBounds(260, 175, 100, 32);

        frame.add(loginBtn);
        frame.add(clearBtn);

        // Login Action
        ActionListener performLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText().trim();
                String pass = new String(password.getPassword()).trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter both username and password.",
                            "Missing Input",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Check prototype credentials
                if (("admin".equalsIgnoreCase(user) && "admin".equals(pass)) ||
                    ("admin".equalsIgnoreCase(user) && "1234".equals(pass))) {
                    JOptionPane.showMessageDialog(frame,
                            "Login Successful! Welcome, " + user + ".",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    DashboardPrototype.openDashboard();
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid username or password!\nHint: admin / admin",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        loginBtn.addActionListener(performLogin);

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username.setText("");
                password.setText("");
                username.requestFocus();
            }
        });

        // Trigger login on Enter key
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin.actionPerformed(null);
                }
            }
        };
        username.addKeyListener(enterKeyAdapter);
        password.addKeyListener(enterKeyAdapter);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                openLogin();
            }
        });
    }
}

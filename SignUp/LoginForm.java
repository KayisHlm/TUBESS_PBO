import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {
    public LoginForm() {
        setTitle("Login Form");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Log In");
        JButton toSignupBtn = new JButton("Sign Up");
        JLabel msg = new JLabel("", SwingConstants.CENTER);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(toSignupBtn);
        panel.add(msg);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            User user = UserDatabase.findUserByEmail(email, password);
            if (user != null) {
                dispose();
                new Dashboard(user);
            } else {
                msg.setText("Invalid credentials.");
            }
        });

        toSignupBtn.addActionListener(e -> {
            dispose();
            new SignUpForm();
        });

        add(panel);
        setVisible(true);
    }
}

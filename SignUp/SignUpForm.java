import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignUpForm extends JFrame {
    public SignUpForm() {
        
        setTitle("Sign Up Form");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(10, 1, 10, 10));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField positionField = new JTextField();

        JRadioButton admin = new JRadioButton("Admin");
        JRadioButton employee = new JRadioButton("Employee");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(admin);
        roleGroup.add(employee);

        JButton signUpBtn = new JButton("Sign Up");
        JButton backBtn = new JButton("Back to Login");
        JLabel msg = new JLabel("", SwingConstants.CENTER);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Full Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Position:"));
        panel.add(positionField);
        panel.add(admin);
        panel.add(employee);
        panel.add(signUpBtn);
        panel.add(backBtn);
        panel.add(msg);

        signUpBtn.addActionListener(e -> {
            String role = admin.isSelected() ? "admin" : employee.isSelected() ? "employee" : "";
            if (role.isEmpty()) {
                msg.setText("Please select a role.");
                return;
            }

            User user = new User(
                idField.getText(),
                nameField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                positionField.getText(),
                role
            );
            UserDatabase.addUser(user);
            msg.setText("Registration successful!");
        });

        backBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        add(panel);
        setVisible(true);
    }
}

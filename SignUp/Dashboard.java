import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard(User user) {
        
        setTitle("Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JLabel welcome = new JLabel("Welcome, " + user.getFullname(), SwingConstants.CENTER);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (User u : UserDatabase.getAllUsers()) {
            listModel.addElement(u.getFullname() + " - " + u.getPosition());
        }

        JList<String> userList = new JList<>(listModel);
        JButton logoutBtn = new JButton("Logout");

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        panel.add(welcome, BorderLayout.NORTH);
        panel.add(new JScrollPane(userList), BorderLayout.CENTER);
        panel.add(logoutBtn, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }
}

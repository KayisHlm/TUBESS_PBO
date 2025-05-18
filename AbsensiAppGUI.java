import java.awt.*;
import java.util.*;
import javax.swing.*;

class User {
    String id, nama, role;
    public User(String id, String nama, String role) {
        this.id = id;
        this.nama = nama;
        this.role = role;
    }
}

class Absensi {
    String userId, tanggal, jamMasuk, jamKeluar;
    public Absensi(String userId, String tanggal, String jamMasuk, String jamKeluar) {
        this.userId = userId;
        this.tanggal = tanggal;
        this.jamMasuk = jamMasuk;
        this.jamKeluar = jamKeluar; 
    }

    public String toString() {
        return userId + " | " + tanggal + " | " + jamMasuk + " - " + jamKeluar;
    }
}

class AbsensiService {
    java.util.List<Absensi> daftarAbsensi = new java.util.ArrayList<>();

    void tambahAbsensi(Absensi a) {
        daftarAbsensi.add(a);
    }

    java.util.List<Absensi> getSemua() {
        return daftarAbsensi;
    }

    java.util.List<Absensi> getByUser(String userId) {
        java.util.List<Absensi> hasil = new java.util.ArrayList<>();
        for (Absensi a : daftarAbsensi) {
            if (a.userId.equals(userId)) {
                hasil.add(a);
            }
        }
        return hasil;
    }
}

public class AbsensiAppGUI {
    static AbsensiService service = new AbsensiService();
    static Map<String, User> users = new HashMap<>();
    static User currentUser;

    public static void main(String[] args) {
        users.put("admin", new User("A01", "Admin", "admin"));
        users.put("budi", new User("K01", "Budi", "karyawan"));
        users.put("siti", new User("K02", "Siti", "karyawan"));

        SwingUtilities.invokeLater(() -> loginForm());
    }

    static void loginForm() {
        JFrame frame = new JFrame("Login Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(new FlowLayout());

        JComboBox<String> comboUser = new JComboBox<>(new String[]{"admin", "budi", "siti"});
        JButton btnLogin = new JButton("Login");

        frame.add(new JLabel("Pilih User:"));
        frame.add(comboUser);
        frame.add(btnLogin);

        btnLogin.addActionListener(e -> {
            String userKey = comboUser.getSelectedItem().toString();
            currentUser = users.get(userKey);
            frame.dispose();
            if (currentUser.role.equals("admin")) {
                showAdminUI();
            } else {
                showKaryawanUI();
            }
        });

        frame.setVisible(true);
    }

    static void showAdminUI() {
        JFrame frame = new JFrame("Admin - Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        JButton btnRefresh = new JButton("Lihat Semua Absensi");
        JButton btnTambah = new JButton("Tambah Absensi");
        JButton btnLogout = new JButton("Logout");

        JPanel panelTop = new JPanel();
        panelTop.add(btnRefresh);
        panelTop.add(btnTambah);
        panelTop.add(btnLogout);

        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);

        btnRefresh.addActionListener(e -> {
            textArea.setText("");
            for (Absensi a : service.getSemua()) {
                textArea.append(a.toString() + "\n");
            }
        });

        btnTambah.addActionListener(e -> {
            JTextField txtUser = new JTextField();
            JTextField txtTgl = new JTextField();
            JTextField txtMasuk = new JTextField();
            JTextField txtKeluar = new JTextField();
            Object[] input = {
                    "User ID:", txtUser,
                    "Tanggal (YYYY-MM-DD):", txtTgl,
                    "Jam Masuk:", txtMasuk,
                    "Jam Keluar:", txtKeluar
            };
            int res = JOptionPane.showConfirmDialog(null, input, "Tambah Absensi", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                service.tambahAbsensi(new Absensi(txtUser.getText(), txtTgl.getText(), txtMasuk.getText(), txtKeluar.getText()));
                JOptionPane.showMessageDialog(null, "Absensi ditambahkan!");
            }
        });

        btnLogout.addActionListener(e -> {
            frame.dispose();
            loginForm();
        });

        frame.setVisible(true);
    }

    static void showKaryawanUI() {
        JFrame frame = new JFrame("Karyawan - Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        JButton btnLihat = new JButton("Lihat Absensi Saya");
        JButton btnAbsen = new JButton("Absen Hari Ini");
        JButton btnLogout = new JButton("Logout");

        JPanel panelTop = new JPanel();
        panelTop.add(btnLihat);
        panelTop.add(btnAbsen);
        panelTop.add(btnLogout);

        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);

        btnLihat.addActionListener(e -> {
            textArea.setText("");
            for (Absensi a : service.getByUser(currentUser.id)) {
                textArea.append(a.toString() + "\n");
            }
        });

        btnAbsen.addActionListener(e -> {
            JTextField txtTgl = new JTextField();
            JTextField txtMasuk = new JTextField();
            JTextField txtKeluar = new JTextField();
            Object[] input = {
                    "Tanggal (YYYY-MM-DD):", txtTgl,
                    "Jam Masuk:", txtMasuk,
                    "Jam Keluar:", txtKeluar
            };
            int res = JOptionPane.showConfirmDialog(null, input, "Input Absensi", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                service.tambahAbsensi(new Absensi(currentUser.id, txtTgl.getText(), txtMasuk.getText(), txtKeluar.getText()));
                JOptionPane.showMessageDialog(null, "Absensi berhasil disimpan!");
            }
        });

        btnLogout.addActionListener(e -> {
            frame.dispose();
            loginForm();
        });

        frame.setVisible(true);
    }
}
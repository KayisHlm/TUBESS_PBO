import java.awt.*;
import java.util.*;
import javax.swing.*;

// === Model User ===
class User {
    String id, nama, role;
    public User(String id, String nama, String role) {
        this.id = id;
        this.nama = nama;
        this.role = role;
    }
}

// === Model Absensi ===
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

// === Model Lembur ===
class Lembur {
    String userId, tanggal;
    int totalJam;
    String status; // "menunggu", "setuju", "tolak"

    public Lembur(String userId, String tanggal, int totalJam) {
        this.userId = userId;
        this.tanggal = tanggal;
        this.totalJam = totalJam;
        this.status = "menunggu";
    }

    public String toString() {
        return userId + " | " + tanggal + " | " + totalJam + " jam | Status: " + status;
    }
}

// === Service Absensi ===
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

// === Service Lembur ===
class LemburService {
    java.util.List<Lembur> daftarLembur = new ArrayList<>();

    void tambahLembur(Lembur l) {
        daftarLembur.add(l);
    }

    java.util.List<Lembur> getSemua() {
        return daftarLembur;
    }

    java.util.List<Lembur> getByUser(String userId) {
        java.util.List<Lembur> hasil = new ArrayList<>();
        for (Lembur l : daftarLembur) {
            if (l.userId.equals(userId)) {
                hasil.add(l);
            }
        }
        return hasil;
    }

    void ubahStatus(Lembur lembur, String statusBaru) {
        lembur.status = statusBaru;
    }
}

// === Main Application GUI ===
public class AbsensiAppGUI {
    static AbsensiService service = new AbsensiService();
    static LemburService lemburService = new LemburService();
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
        frame.setSize(400, 150);
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
                showAdminMenu();
            } else {
                showKaryawanMenu();
            }
        });

        frame.setVisible(true);
    }

    // ============ ADMIN MENU ===============
    static void showAdminMenu() {
        JFrame frame = new JFrame("Admin Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new FlowLayout());

        JButton btnAbsensi = new JButton("Kelola Absensi");
        JButton btnLembur = new JButton("Kelola Lembur");
        JButton btnLogout = new JButton("Logout");

        frame.add(btnAbsensi);
        frame.add(btnLembur);
        frame.add(btnLogout);

        btnAbsensi.addActionListener(e -> {
            frame.dispose();
            showAdminAbsensi();
        });

        btnLembur.addActionListener(e -> {
            frame.dispose();
            showLemburPage(true); // admin
        });

        btnLogout.addActionListener(e -> {
            frame.dispose();
            loginForm();
        });

        frame.setVisible(true);
    }

    static void showAdminAbsensi() {
        JFrame frame = new JFrame("Admin - Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        JButton btnRefresh = new JButton("Lihat Semua Absensi");
        JButton btnTambah = new JButton("Tambah Absensi");
        JButton btnBack = new JButton("Back");

        JPanel panelTop = new JPanel();
        panelTop.add(btnRefresh);
        panelTop.add(btnTambah);
        panelTop.add(btnBack);

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

        btnBack.addActionListener(e -> {
            frame.dispose();
            showAdminMenu();
        });

        frame.setVisible(true);
    }

    // ============ KARYAWAN MENU ===============
    static void showKaryawanMenu() {
        JFrame frame = new JFrame("Karyawan Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new FlowLayout());

        JButton btnAbsensi = new JButton("Lihat Absensi");
        JButton btnLembur = new JButton("Lembur");
        JButton btnLogout = new JButton("Logout");

        frame.add(btnAbsensi);
        frame.add(btnLembur);
        frame.add(btnLogout);

        btnAbsensi.addActionListener(e -> {
            frame.dispose();
            showKaryawanAbsensi();
        });

        btnLembur.addActionListener(e -> {
            frame.dispose();
            showLemburPage(false); // karyawan
        });

        btnLogout.addActionListener(e -> {
            frame.dispose();
            loginForm();
        });

        frame.setVisible(true);
    }

    static void showKaryawanAbsensi() {
        JFrame frame = new JFrame("Karyawan - Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        JButton btnLihat = new JButton("Lihat Absensi Saya");
        JButton btnAbsen = new JButton("Absen Hari Ini");
        JButton btnBack = new JButton("Back");

        JPanel panelTop = new JPanel();
        panelTop.add(btnLihat);
        panelTop.add(btnAbsen);
        panelTop.add(btnBack);

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

        btnBack.addActionListener(e -> {
            frame.dispose();
            showKaryawanMenu();
        });

        frame.setVisible(true);
    }

    // ============ LEMBUR PAGE (Admin dan Karyawan) ===============
    static void showLemburPage(boolean isAdmin) {
    JFrame frame = new JFrame("Lembur");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.setLayout(new BorderLayout());

    JTextArea textArea = new JTextArea();
    JScrollPane scroll = new JScrollPane(textArea);
    JButton btnBack = new JButton("Back");

    JPanel panelTop = new JPanel();

    // Tombol Ajukan Lembur (baik admin maupun karyawan)
    JButton btnAjukan = new JButton("Ajukan Lembur");
    panelTop.add(btnAjukan);

    Runnable refreshLemburList = () -> {
        textArea.setText("");
        java.util.List<Lembur> daftar = isAdmin ? lemburService.getSemua() : lemburService.getByUser(currentUser.id);
        for (int i = 0; i < daftar.size(); i++) {
            textArea.append("[" + i + "] " + daftar.get(i).toString() + "\n");
        }
    };

    btnAjukan.addActionListener(e -> {
        JTextField txtTgl = new JTextField();
        JTextField txtJam = new JTextField();

        JTextField txtUser = new JTextField(); // hanya untuk admin
        Object[] input;

        if (isAdmin) {
            JComboBox<String> comboUser = new JComboBox<>();
            for (User u : users.values()) {
                if (u.role.equals("karyawan")) {
                    comboUser.addItem(u.id + " - " + u.nama);
                }
            }

            input = new Object[]{
                "Pilih Karyawan:", comboUser,
                "Tanggal (YYYY-MM-DD):", txtTgl,
                "Total Jam Lembur:", txtJam
            };
            
            int res = JOptionPane.showConfirmDialog(null, input, "Ajukan Lembur", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    int jam = Integer.parseInt(txtJam.getText());
                    String selected = (String) comboUser.getSelectedItem();
                    String userId = selected.split(" - ")[0];

                    lemburService.tambahLembur(new Lembur(userId, txtTgl.getText(), jam));
                    JOptionPane.showMessageDialog(null, "Pengajuan lembur berhasil!");
                    refreshLemburList.run(); // update langsung
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Total jam harus berupa angka!");
                }
            }
        }
 else {
            input = new Object[]{
                "Tanggal (YYYY-MM-DD):", txtTgl,
                "Total Jam Lembur:", txtJam
            };
        }

        int res = JOptionPane.showConfirmDialog(null, input, "Ajukan Lembur", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                int jam = Integer.parseInt(txtJam.getText());
                String userId = isAdmin ? txtUser.getText() : currentUser.id;

                lemburService.tambahLembur(new Lembur(userId, txtTgl.getText(), jam));
                JOptionPane.showMessageDialog(null, "Pengajuan lembur berhasil!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Total jam harus berupa angka!");
            }
        }
    });

    // Tombol untuk menyetujui/menolak lembur (hanya admin)
    if (isAdmin) {
        JButton btnSetuju = new JButton("Setujui");
        JButton btnTolak = new JButton("Tolak");
        panelTop.add(btnSetuju);
        panelTop.add(btnTolak);

        btnSetuju.addActionListener(e -> {
            String selectedId = JOptionPane.showInputDialog("Masukkan nomor lembur (0,1,2,..) untuk disetujui:");
            try {
                int index = Integer.parseInt(selectedId);
                lemburService.ubahStatus(lemburService.getSemua().get(index), "setuju");
                JOptionPane.showMessageDialog(null, "Lembur disetujui!");
                refreshLemburList.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Input tidak valid.");
            }
        });

        btnTolak.addActionListener(e -> {
            String selectedId = JOptionPane.showInputDialog("Masukkan nomor lembur (0,1,2,..) untuk ditolak:");
            try {
                int index = Integer.parseInt(selectedId);
                lemburService.ubahStatus(lemburService.getSemua().get(index), "tolak");
                JOptionPane.showMessageDialog(null, "Lembur ditolak.");
                refreshLemburList.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Input tidak valid.");
            }
        });
    }

    JButton btnLihat = new JButton("Lihat Data Lembur");
    panelTop.add(btnLihat);
    panelTop.add(btnBack);

    frame.add(panelTop, BorderLayout.NORTH);
    frame.add(scroll, BorderLayout.CENTER);

    btnLihat.addActionListener(e -> {
        textArea.setText("");
        java.util.List<Lembur> daftar = isAdmin ? lemburService.getSemua() : lemburService.getByUser(currentUser.id);
        for (int i = 0; i < daftar.size(); i++) {
            textArea.append("[" + i + "] " + daftar.get(i).toString() + "\n");
        }
    });

    btnBack.addActionListener(e -> {
        frame.dispose();
        if (isAdmin) {
            showAdminMenu();
        } else {
            showKaryawanMenu();
        }
    });

    frame.setVisible(true);
    }

}
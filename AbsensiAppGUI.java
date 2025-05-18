import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    String userId, tanggal;
    LocalTime jamMasuk, jamKeluar;
    String status;

    public Absensi(String userId, String tanggal, LocalTime jamMasuk, LocalTime jamKeluar, String status) {
        this.userId = userId;
        this.tanggal = tanggal;
        this.jamMasuk = jamMasuk;
        this.jamKeluar = jamKeluar;
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        return userId + " | " + tanggal + " | " + jamMasuk.format(timeFmt) + " - " + jamKeluar.format(timeFmt)
                + " | " + status;
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
            if (a.userId.equals(userId))
                hasil.add(a);
        }
        return hasil;
    }

    void updateAbsensi(int idx, Absensi a) {
        daftarAbsensi.set(idx, a);
    }

    void deleteAbsensi(int idx) {
        daftarAbsensi.remove(idx);
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


// === Model Cuti ===
class Cuti {
    String userId, tanggalMulai, tanggalBerakhir, jenisCuti;
    String status; // "menunggu", "setuju", "tolak"

    public Cuti(String userId, String tanggalMulai, String tanggalBerakhir, String jenisCuti, String statuString) {
        this.userId = userId;
        this.tanggalMulai= tanggalMulai;
        this.tanggalBerakhir = tanggalBerakhir;
        this.status = "menunggu";
        this.jenisCuti = jenisCuti;
    }

    public String toString() {
        return (userId + " | "+ "Jenis Cuti: " + jenisCuti  + " | " + "Tanggal Mulai: "+ tanggalMulai + " | " + "Tanggal Berakhir: " + tanggalBerakhir + "|"+ "Status:" + status);
    }
}

// === Service Cuti ===
class CutiService {
    java.util.List<Cuti> daftarCuti = new ArrayList<>();

    void tambahCuti(Cuti c) {
        daftarCuti.add(c);
    }

    java.util.List<Cuti> getSemua() {
        return daftarCuti;
    }

    java.util.List<Cuti> getByUser(String userId) {
        java.util.List<Cuti> hasil = new ArrayList<>();
        for (Cuti c : daftarCuti) {
            if (c.userId.equals(userId)) {
                hasil.add(c);
            }
        }
        return hasil;
    }

    void ubahStatus(Cuti cuti, String statusBaru) {
        cuti.status = statusBaru;
    }
}

// === Main Application GUI ===
public class AbsensiAppGUI {
    static AbsensiService service = new AbsensiService();
    static LemburService lemburService = new LemburService();
    static CutiService cutiService = new CutiService();
    static Map<String, User> users = new HashMap<>();
    static Map<String, String> choices= new HashMap<>();
    static User currentUser;
    static DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        users.put("admin", new User("A01", "Admin", "admin"));
        users.put("budi", new User("K01", "Budi", "karyawan"));
        users.put("siti", new User("K02", "Siti", "karyawan"));

        choices.put("Annual", "Annual Leave");
        choices.put("Sick", "Sick Leave");
        choices.put("Maternity", "Maternity Leave");
        choices.put("Emergency", "Emergency Leave");


        SwingUtilities.invokeLater(() -> loginForm());
    }

    static void loginForm() {
        JFrame frame = new JFrame("Login Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new FlowLayout());

        JComboBox<String> comboUser = new JComboBox<>(new String[] { "admin", "budi", "siti" });
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
        JButton btnCuti= new JButton("Kelola Cuti");
        JButton btnLogout = new JButton("Logout");

        frame.add(btnAbsensi);
        frame.add(btnCuti);
        frame.add(btnLembur);
        frame.add(btnLogout);

        btnAbsensi.addActionListener(e -> {
            frame.dispose();
            showAbsensiPage(true);
        });

        btnCuti.addActionListener(e -> {
            frame.dispose();
            showCutiPage(true); // admin
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

    // ============ ABSENSI PAGE (Admin dan Karyawan) ===============
    static void showAbsensiPage(boolean isAdmin) {
        JFrame frame = new JFrame("Absensi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Tabel untuk menampilkan data absensi
        String[] cols = { "IDX", "User", "Tanggal", "Check In", "Check Out", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        // Tombol–tombol aksi
        JPanel panelTop = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnLihat = new JButton("Refresh");
        JButton btnBack = new JButton("Back");

        panelTop.add(btnTambah);
        panelTop.add(btnEdit);
        panelTop.add(btnHapus);
        panelTop.add(btnLihat);
        panelTop.add(btnBack);

        // Helper: muat ulang tabel dari service
        Runnable refresh = () -> {
            model.setRowCount(0);
            List<Absensi> all = isAdmin
                    ? service.getSemua()
                    : service.getByUser(currentUser.id);
            for (int i = 0; i < all.size(); i++) {
                Absensi a = all.get(i);
                model.addRow(new Object[] {
                        i,
                        a.userId,
                        a.tanggal,
                        a.jamMasuk.toString(),
                        a.jamKeluar.toString(),
                        a.status
                });
            }
        };

        // Aksi tombol
        btnLihat.addActionListener(e -> refresh.run());

        btnTambah.addActionListener(e -> {
            // Form input mirip AdminForm
            JTextField tfDate = new JTextField(LocalDate.now().toString());
            JTextField tfCheckIn = new JTextField(LocalTime.now().toString());
            JTextField tfCheckOut = new JTextField(LocalTime.now().toString());
            JTextField tfStatus = new JTextField();

            JComboBox<User> cbUser = new JComboBox<>();
            for (User u : users.values())
                if (u.role.equals("karyawan"))
                    cbUser.addItem(u);

            Object[] form = {
                    "User:", cbUser,
                    "Tanggal (YYYY-MM-DD):", tfDate,
                    "Check In (HH:MM):", tfCheckIn,
                    "Check Out (HH:MM):", tfCheckOut,
                    "Status:", tfStatus
            };
            int res = JOptionPane.showConfirmDialog(frame, form, "Tambah Absensi", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                User u = (User) cbUser.getSelectedItem();
                Absensi a = new Absensi(
                        u.id,
                        tfDate.getText(),
                        LocalTime.parse(tfCheckIn.getText()),
                        LocalTime.parse(tfCheckOut.getText()),
                        tfStatus.getText());
                service.tambahAbsensi(a);
                refresh.run();
            }
        });

        btnEdit.addActionListener(e -> {
            int idx = table.getSelectedRow();
            if (idx < 0)
                return;
            // Prefill form with existing
            Absensi old = service.getSemua().get(idx);
            JTextField tfDate = new JTextField(old.tanggal);
            JTextField tfCheckIn = new JTextField(old.jamMasuk.toString());
            JTextField tfCheckOut = new JTextField(old.jamKeluar.toString());
            JTextField tfStatus = new JTextField(old.status);

            JComboBox<User> cbUser = new JComboBox<>();
            for (User u : users.values())
                if (u.role.equals("karyawan"))
                    cbUser.addItem(u);
            // pilih current
            for (int i = 0; i < cbUser.getItemCount(); i++)
                if (cbUser.getItemAt(i).id.equals(old.userId))
                    cbUser.setSelectedIndex(i);

            Object[] form = {
                    "User:", cbUser,
                    "Tanggal (YYYY-MM-DD):", tfDate,
                    "Check In (HH:MM):", tfCheckIn,
                    "Check Out (HH:MM):", tfCheckOut,
                    "Status:", tfStatus
            };
            int res = JOptionPane.showConfirmDialog(frame, form, "Edit Absensi", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                User u = (User) cbUser.getSelectedItem();
                Absensi a = new Absensi(
                        u.id,
                        tfDate.getText(),
                        LocalTime.parse(tfCheckIn.getText()),
                        LocalTime.parse(tfCheckOut.getText()),
                        tfStatus.getText());
                service.updateAbsensi(idx, a);
                refresh.run();
            }
        });

        btnHapus.addActionListener(e -> {
            int idx = table.getSelectedRow();
            if (idx < 0)
                return;
            service.deleteAbsensi(idx);
            refresh.run();
        });

        btnBack.addActionListener(e -> {
            frame.dispose();
            if (isAdmin)
                showAdminMenu();
            else
                showKaryawanMenu();
        });

        // Tambahkan ke frame
        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        refresh.run();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void refreshAdminTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (int i = 0; i < service.getSemua().size(); i++) {
            Absensi a = service.getSemua().get(i);
            model.addRow(new Object[] { i, a.userId, a.tanggal, a.jamMasuk, a.jamKeluar, a.status });
        }
    }

    static void openAdminForm(DefaultTableModel model, int editIdx) {
        JDialog dlg = new JDialog((Frame) null, "Form Absensi Admin", true);
        dlg.setSize(400, 300);
        dlg.setLayout(new GridLayout(6, 2));
        JComboBox<User> cbUser = new JComboBox<>();
        for (User u : users.values())
            if (u.role.equals("karyawan"))
                cbUser.addItem(u);
        JTextField tfDate = new JTextField(LocalDate.now().format(dateFmt));
        JTextField tfIn = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        JTextField tfOut = new JTextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        JTextField tfStatus = new JTextField();
        if (editIdx >= 0) {
            Absensi a = service.getSemua().get(editIdx);
            // prefill
            for (int i = 0; i < cbUser.getItemCount(); i++)
                if (cbUser.getItemAt(i).id.equals(a.userId))
                    cbUser.setSelectedIndex(i);
            tfDate.setText(a.tanggal);
            tfIn.setText(a.jamMasuk.toString());
            tfOut.setText(a.jamKeluar.toString());
            tfStatus.setText(a.status);
        }
        dlg.add(new JLabel("User:"));
        dlg.add(cbUser);
        dlg.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        dlg.add(tfDate);
        dlg.add(new JLabel("Check In (HH:mm):"));
        dlg.add(tfIn);
        dlg.add(new JLabel("Check Out (HH:mm):"));
        dlg.add(tfOut);
        dlg.add(new JLabel("Status:"));
        dlg.add(tfStatus);
        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");
        btnSave.addActionListener(e -> {
            User u = (User) cbUser.getSelectedItem();
            String d = tfDate.getText();
            LocalTime in = LocalTime.parse(tfIn.getText());
            LocalTime out = LocalTime.parse(tfOut.getText());
            String st = tfStatus.getText();
            Absensi a = new Absensi(u.id, d, in, out, st);
            if (editIdx >= 0)
                service.updateAbsensi(editIdx, a);
            else
                service.tambahAbsensi(a);
            refreshAdminTable(model);
            dlg.dispose();
        });
        btnCancel.addActionListener(e -> dlg.dispose());
        dlg.add(btnSave);
        dlg.add(btnCancel);
        dlg.setLocationRelativeTo(null);
        dlg.setVisible(true);
    }

    // ============ KARYAWAN MENU ===============
    static void showKaryawanMenu() {
        JFrame frame = new JFrame("Karyawan Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new FlowLayout());
        JButton btnForm = new JButton("Form Absensi");
        JButton btnHist = new JButton("Riwayat Absensi");
        JButton btnLogout = new JButton("Logout");
        btnForm.addActionListener(e -> {
            frame.dispose();
            openKaryawanForm();
        });
        btnHist.addActionListener(e -> {
            frame.dispose();
            showKaryawanHist();
        });
        btnLogout.addActionListener(e -> {
            frame.dispose();
            loginForm();
        });
        frame.add(btnForm);
        frame.add(btnHist);
        frame.add(btnLogout);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void openKaryawanForm() {
        JFrame frame = new JFrame("Absensi Karyawan");
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());
        boolean[] hasIn = new boolean[] { false };
        for (Absensi a : service.getByUser(currentUser.id))
            if (a.tanggal.equals(LocalDate.now().format(dateFmt)))
                hasIn[0] = true;
        JButton btnIn = new JButton("Check In");
        JButton btnOut = new JButton("Check Out");
        btnIn.setEnabled(!hasIn[0]);
        btnOut.setEnabled(hasIn[0]);
        btnIn.addActionListener(e -> {
            LocalDate d = LocalDate.now();
            LocalTime t = LocalTime.now();
            String stat = t.isAfter(LocalTime.of(9, 0)) ? "Tidak Hadir" : "";
            String date = d.format(dateFmt);
            Absensi a = new Absensi(currentUser.id, date, t, LocalTime.of(0, 0), stat);
            service.tambahAbsensi(a);
            JOptionPane.showMessageDialog(frame, "Check In: " + t.format(DateTimeFormatter.ofPattern("HH:mm")));
            frame.dispose();
            showKaryawanMenu();
        });
        btnOut.addActionListener(e -> {
            LocalDate d = LocalDate.now();
            LocalTime t = LocalTime.now();
            Absensi a = null;
            int idx = -1;
            for (int i = 0; i < service.getSemua().size(); i++) {
                Absensi x = service.getSemua().get(i);
                if (x.userId.equals(currentUser.id) && x.tanggal.equals(LocalDate.now().format(dateFmt))) {
                    a = x;
                    idx = i;
                    break;
                }
            }
            if (a != null) {
                a.jamKeluar = t;
                // status logic
                if (a.jamMasuk.isBefore(LocalTime.of(9, 0)) && t.isAfter(LocalTime.of(12, 0)))
                    a.status = "Hadir";
                else if (t.isBefore(LocalTime.of(12, 0)))
                    a.status = "Izin";
                else
                    a.status = a.status.isEmpty() ? "Hadir" : "";
                service.updateAbsensi(idx, a);
                JOptionPane.showMessageDialog(frame, "Check Out: " + t.format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            frame.dispose();
            showKaryawanMenu();
        });
        frame.add(btnIn);
        frame.add(btnOut);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static void showKaryawanHist() {
        JFrame frame = new JFrame("Riwayat Absensi");
        frame.setSize(600, 400);
        String[] cols = { "Tanggal", "Jam Masuk", "Jam Keluar", "Status" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Absensi a : service.getByUser(currentUser.id)) {
            model.addRow(new Object[] { a.tanggal, a.jamMasuk, a.jamKeluar, a.status });
        }
        JTable table = new JTable(model);
        frame.add(new JScrollPane(table));
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> {
            frame.dispose();
            showKaryawanMenu();
        });
        frame.add(btnBack, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
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
            java.util.List<Lembur> daftar = isAdmin ? lemburService.getSemua()
                    : lemburService.getByUser(currentUser.id);
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

                input = new Object[] {
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
            } else {
                input = new Object[] {
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
            java.util.List<Lembur> daftar = isAdmin ? lemburService.getSemua()
                    : lemburService.getByUser(currentUser.id);
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

    // ============ CUTI PAGE (Admin dan Karyawan) ===============
    static void showCutiPage(boolean isAdmin) {
        JFrame frame = new JFrame("Cuti");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(textArea);
        JButton btnBack = new JButton("Back");

        JPanel panelTop = new JPanel();

        // Tombol Ajukan Cuti Karyawan
        JButton btnAjukan = new JButton("Ajukan Cuti");
        panelTop.add(btnAjukan);

        Runnable refreshCutiList = () -> {
            textArea.setText("");
            java.util.List<Cuti> daftar = isAdmin ? cutiService.getSemua()
                    : cutiService.getByUser(currentUser.id);
            for (int i = 0; i < daftar.size(); i++) {
                textArea.append("[" + i + "] " + daftar.get(i).toString() + "\n");
            }
        };

        btnAjukan.addActionListener(e -> {
            JTextField txtTglMulai = new JTextField();
            JTextField txtTglBerakhir = new JTextField();
            JTextField txtJenisCuti = new JTextField();
            JComboBox<String> pilihan = new JComboBox<>();
            for (String u : choices.values()) {
                pilihan.addItem(u);
            }

            JTextField txtUser = new JTextField(); // hanya untuk admin
            Object[] input;

            if (isAdmin) {
                JComboBox<String> comboUser = new JComboBox<>();
                for (User u : users.values()) {
                    if (u.role.equals("karyawan")) {
                        comboUser.addItem(u.id + " - " + u.nama);
                    }
                }
                

                input = new Object[] {
                        "Pilih Karyawan:", comboUser,
                        "TanggalMulai (YYYY-MM-DD):", txtTglMulai,
                        "TanggalBerakhir (YYYY-MM-DD):", txtTglBerakhir,
                        "Jenis Cuti:", txtJenisCuti,
                        "Status:", pilihan
                };

                int res = JOptionPane.showConfirmDialog(null, input, "Ajukan Cuti", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {                    
                    String selected = (String) comboUser.getSelectedItem();
                    String userId = selected.split(" - ")[0];

                    cutiService.tambahCuti(new Cuti(userId, txtTglMulai.getText(), txtTglBerakhir.getText(), txtJenisCuti.getText(), pilihan.getSelectedItem().toString()));
                    JOptionPane.showMessageDialog(null, "Pengajuan cuti berhasil!");
                    refreshCutiList.run(); // update langsung
                }
            } else {
                input = new Object[] {
                        "TanggalMulai (YYYY-MM-DD):", txtTglMulai,
                        "TanggalBerakhir(YYYY-MM-DD):", txtTglBerakhir,
                        "Jenis Cuti:", txtJenisCuti,
                        "Status:", pilihan
                };
            }

            int res = JOptionPane.showConfirmDialog(null, input, "Ajukan Cuti", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                String userId = isAdmin ? txtUser.getText() : currentUser.id;

                cutiService.tambahCuti(new Cuti(userId, txtTglMulai.getText(),txtTglBerakhir.getText(), txtJenisCuti.getText(),pilihan.getSelectedItem().toString()));
                JOptionPane.showMessageDialog(null, "Pengajuan cuti berhasil!");
            }
        });

        // Tombol untuk menyetujui/menolak cuti (hanya admin)
        if (isAdmin) {
            JButton btnSetuju = new JButton("Setujui");
            JButton btnTolak = new JButton("Tolak");
            panelTop.add(btnSetuju);
            panelTop.add(btnTolak);

            btnSetuju.addActionListener(e -> {
                String selectedId = JOptionPane.showInputDialog("Masukkan nomor cuti (0,1,2,..) untuk disetujui:");
                try {
                    int index = Integer.parseInt(selectedId);
                    cutiService.ubahStatus(cutiService.getSemua().get(index), "setuju");
                    JOptionPane.showMessageDialog(null, "Cuti disetujui!");
                    refreshCutiList.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Input tidak valid.");
                }
            });

            btnTolak.addActionListener(e -> {
                String selectedId = JOptionPane.showInputDialog("Masukkan nomor cuti (0,1,2,..) untuk ditolak:");
                try {
                    int index = Integer.parseInt(selectedId);
                    cutiService.ubahStatus(cutiService.getSemua().get(index), "tolak");
                    JOptionPane.showMessageDialog(null, "Cuti ditolak.");
                    refreshCutiList.run();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Input tidak valid.");
                }
            });
        }

        JButton btnLihat = new JButton("Lihat Data Cuti");
        panelTop.add(btnLihat);
        panelTop.add(btnBack);

        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);

        btnLihat.addActionListener(e -> {
            textArea.setText("");
            java.util.List<Cuti> daftar = isAdmin ? cutiService.getSemua()
                    : cutiService.getByUser(currentUser.id);
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

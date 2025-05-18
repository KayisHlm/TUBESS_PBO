import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AplikasiCuti extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel formPanel, listPanel;
    private JTextField tfIdUser, tfJenisCuti, tfTanggalMulai, tfTanggalBerakhir;
    private JComboBox<String> cbStatus;
    private DefaultTableModel tableModel;
    private JTable table;
    private java.util.List<DataCuti> listCuti = new ArrayList<>();

    public AplikasiCuti() {
        setTitle("Aplikasi Pengajuan Cuti");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel Form
        formPanel = new JPanel(new GridLayout(6, 2));
        tfIdUser = new JTextField();
        tfJenisCuti = new JTextField();
        tfTanggalMulai = new JTextField();
        tfTanggalBerakhir = new JTextField();
        cbStatus = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});

        formPanel.add(new JLabel("ID User:"));
        formPanel.add(tfIdUser);
        formPanel.add(new JLabel("Jenis Cuti:"));
        formPanel.add(tfJenisCuti);
        formPanel.add(new JLabel("Tanggal Mulai (yyyy-mm-dd):"));
        formPanel.add(tfTanggalMulai);
        formPanel.add(new JLabel("Tanggal Berakhir (yyyy-mm-dd):"));
        formPanel.add(tfTanggalBerakhir);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(cbStatus);

        JButton btnSubmit = new JButton("Ajukan Cuti");
        JButton btnLihatDaftar = new JButton("Lihat Daftar Cuti");

        formPanel.add(btnSubmit);
        formPanel.add(btnLihatDaftar);

        // Panel List
        listPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"ID User", "Jenis Cuti", "Tgl Mulai", "Tgl Berakhir", "Status"}, 0);
        table = new JTable(tableModel);
        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel listButtonPanel = new JPanel();
        JButton btnKembali = new JButton("Kembali");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        listButtonPanel.add(btnEdit);
        listButtonPanel.add(btnDelete);
        listButtonPanel.add(btnKembali);
        listPanel.add(listButtonPanel, BorderLayout.SOUTH);

        mainPanel.add(formPanel, "Form");
        mainPanel.add(listPanel, "List");

        add(mainPanel);
        cardLayout.show(mainPanel, "Form");

        // Action Listeners
        btnSubmit.addActionListener(e -> tambahCuti());
        btnLihatDaftar.addActionListener(e -> cardLayout.show(mainPanel, "List"));
        btnKembali.addActionListener(e -> cardLayout.show(mainPanel, "Form"));

        btnEdit.addActionListener(e -> editCuti());
        btnDelete.addActionListener(e -> hapusCuti());

        setVisible(true);
    }

    private void tambahCuti() {
        DataCuti cuti = new DataCuti(
            tfIdUser.getText(),
            tfJenisCuti.getText(),
            tfTanggalMulai.getText(),
            tfTanggalBerakhir.getText(),
            cbStatus.getSelectedItem().toString()
        );
        listCuti.add(cuti);
        tableModel.addRow(new Object[]{cuti.idUser, cuti.jenisCuti, cuti.tanggalMulai, cuti.tanggalBerakhir, cuti.status});
        clearInput();
        JOptionPane.showMessageDialog(this, "Pengajuan cuti berhasil.");
    }

    private void editCuti() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            DataCuti cuti = listCuti.get(selectedRow);
            String newStatus = JOptionPane.showInputDialog(this, "Masukkan status baru (Pending/Approved/Rejected):", cuti.status);
            if (newStatus != null && !newStatus.isEmpty()) {
                cuti.status = newStatus;
                tableModel.setValueAt(newStatus, selectedRow, 4);
            }
        }
    }

    private void hapusCuti() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            listCuti.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        }
    }

    private void clearInput() {
        tfIdUser.setText("");
        tfJenisCuti.setText("");
        tfTanggalMulai.setText("");
        tfTanggalBerakhir.setText("");
        cbStatus.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AplikasiCuti::new);
    }
}

class DataCuti {
    String idUser;
    String jenisCuti;
    String tanggalMulai;
    String tanggalBerakhir;
    String status;

    public DataCuti(String idUser, String jenisCuti, String tanggalMulai, String tanggalBerakhir, String status) {
        this.idUser = idUser;
        this.jenisCuti = jenisCuti;
        this.tanggalMulai = tanggalMulai;
        this.tanggalBerakhir = tanggalBerakhir;
        this.status = status;
    }
}

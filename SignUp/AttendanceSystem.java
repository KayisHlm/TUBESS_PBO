import java.util.*;

public class AttendanceSystem {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<User> users = new ArrayList<>();
    static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Sistem Kehadiran Karyawan ===");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1: signUp(); break;
                case 2: logIn(); break;
                case 3: System.exit(0);
                default: System.out.println("Opsi tidak valid!");
            }
        }
    }

    static void signUp() {
        System.out.print("ID: "); String id = scanner.nextLine();
        System.out.print("Nama Lengkap: "); String name = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();
        System.out.print("Posisi: "); String pos = scanner.nextLine();
        System.out.print("Role (Admin/Karyawan): "); String role = scanner.nextLine();

        users.add(new User(id, name, email, pass, pos, role));
        System.out.println("Registrasi berhasil!\n");
    }

    static void logIn() {
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Password: "); String pass = scanner.nextLine();

        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().equals(pass)) {
                currentUser = u;
                System.out.println("Login berhasil sebagai " + u.getRole() + "\n");
                if (u.getRole().equalsIgnoreCase("Admin")) {
                    adminMenu();
                } else {
                    employeeMenu();
                }
                return;
            }
        }
        System.out.println("Login gagal! Cek email atau password.");
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n--- Menu Admin ---");
            System.out.println("1. Lihat Semua Karyawan");
            System.out.println("2. Logout");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1:
                    for (User u : users) {
                        u.displayInfo();
                        System.out.println("-----");
                    }
                    break;
                case 2: return;
                default: System.out.println("Opsi tidak valid!");
            }
        }
    }

    static void employeeMenu() {
        while (true) {
            System.out.println("\n--- Menu Karyawan ---");
            System.out.println("1. Lihat Data Saya");
            System.out.println("2. Logout");
            System.out.print("Pilih opsi: ");
            int choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1:
                    currentUser.displayInfo(); break;
                case 2: return;
                default: System.out.println("Opsi tidak valid!");
            }
        }
    }
}

package org.uas;

import org.uas.data.User;
import org.uas.repository.*;
import org.uas.util.DBConnectionManager;
import org.uas.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UASApplication {
    UserRepository userRepository;
    private boolean isLogin = false;

    public UASApplication() {
        userRepository = new UserRepository(DBConnectionManager.getConnection());
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            isLogin = SessionManager.getInstance().isLoggedIn();
            System.out.println("UAS");
            System.out.println("0. Exit");
            if (isLogin) {
                System.out.println("2. Tampilkan Semua User");
                System.out.println("3. Tambah User ");
                System.out.println("4. Ubah User");
                System.out.println("5. Hapus User");
                System.out.println("6. Logout");
            } else {
                System.out.println("1. Login");
            }
            System.out.print("Tentukan Pilihan: ");

            int choice = 99;
            try {
                choice = Integer.parseInt(scanner.next());
                if (!isLogin) {
                    if (choice > 1) {
                        choice = -99;
                    }
                }
            } catch (NumberFormatException | NullPointerException e) {
                System.out.print("Pilihan harus berupa angka!\n");
            }
            switch (choice) {
                case 0:
                    exitApps();
                    break;
                case 1:
                    login(scanner);
                    break;
                case 2:
                    tampilkanSemuaUser();
                    break;
                case 3:
                    insertUser(scanner);
                    break;
                case 4:
                    updateUser(scanner);
                    break;
                case 5:
                    deleteUser(scanner);
                    break;
                case 6:
                    logout();
                    break;

                case -99:
                    System.out.println("Anda Belum Login.");
                default:
                    System.out.println("Pilihan tidak sesuai. Coba lagi.");
            }
        }
    }

    private void logout() {
        SessionManager.getInstance().logout();
    }

    private void deleteUser(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Masukan email user yang akan dihapus: ");
        String email = scanner.nextLine();

    }

    private void updateUser(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Masukan email user yang akan diupdate: ");
        String email = scanner.nextLine();
        System.out.print("Masukan username baru: ");
        String username = scanner.nextLine();
        System.out.print("Masukan password baru: ");
        String password = scanner.nextLine();

        if (userRepository.updateUser(email, username, password)) {
            tampilkanSemuaUser();
        }
    }

    private void tampilkanSemuaUser() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            System.out.println("- " + user.getUsername());
        }

        System.out.println("\n");
    }

    private void exitApps() {
        System.out.println("Keluar aplikasi. Goodbye!");
        System.exit(0);
    }

    private void login(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userRepository.authenticateUser(username, password)) {
            SessionManager.getInstance().login();
        } else {
            System.out.println("Login failed!! Please check again.");
        }
    }

    private void insertUser(Scanner scanner) {
        scanner.skip("\\R?");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
    }

    public static void main(String[] args) {
        UASApplication uasApplication = new UASApplication();
        uasApplication.start();
    }
}
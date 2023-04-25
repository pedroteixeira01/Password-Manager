package app;

import java.util.Scanner;

import model.Password;
import model.exceptions.PasswordException;

public class Program {
    public static void main(String[] args) {

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        Ui.clearConsole();
        Ui.intro();
        Password.checkDependencies();

        while (choice != -1) {
            Ui.mainMenu();

            try {
                choice = sc.nextInt();
                System.out.println();
                switch (choice) {
                    case -1:
                        System.out.println();
                        System.out.println("See you later !");
                        System.out.println();

                        sc.close();
                        break;
                    case 1:
                        System.out.print("Enter the length of your password: ");

                        try {
                            String pass = Password.generate(sc.nextInt()).toString();
                            System.out.print(ANSI_YELLOW + "\nGenerated password: " + pass + ANSI_RESET);
                            System.out.println();

                            System.out.println();
                            System.out.print("Do you want to save (Y/N)? ");
                            char save = sc.next().charAt(0);
                            System.out.println();
                            sc.nextLine();

                            if ((save == 'y') || (save == 'Y')) {
                                System.out.print("Enter your master password: ");
                                String master = sc.nextLine();
                                System.out.println();

                                try {
                                    Password.checkMaster(master.trim());

                                    System.out.print("Enter the nickname for your password: ");
                                    String alias = sc.nextLine();
                                    System.out.println();

                                    Password.register(alias, pass);
                                    Password.setLog("Register", "repository");

                                    System.out.println("Press enter to continue");
                                    sc.nextLine();
                                } catch (PasswordException e) {
                                    System.out.println(e.getMessage());
                                    System.out.println();
                                    System.out.println("Press enter to continue");
                                    sc.nextLine();
                                }
                            }

                        } catch (PasswordException e) {
                            System.out.println(e.getMessage());
                            System.out.println();
                            System.out.println("Press enter to continue");
                            sc.nextLine();
                            sc.nextLine();
                        }
                        break;
                    case 2:
                        sc.nextLine();
                        System.out.print("Enter Your master password: ");
                        String master = sc.nextLine();

                        try {
                            Password.checkMaster(master.trim());
                            System.out.println();

                            System.out.print("Enter the nickname for your password: ");
                            String alias = sc.nextLine();

                            System.out.print("Enter the password: ");
                            String password = sc.nextLine();
                            System.out.println();

                            Password.register(alias, password);
                            Password.setLog("Register", "repository");

                            System.out.println();
                            System.out.println(ANSI_YELLOW + "Password registered." + ANSI_RESET);
                            System.out.println();

                            System.out.println("Press enter to continue");
                            sc.nextLine();
                        } catch (PasswordException e) {
                            System.out.println(e.getMessage());
                            System.out.println();
                            System.out.println("Press enter to continue");
                            sc.nextLine();
                        }
                        break;
                    case 3:
                        sc.nextLine();
                        System.out.print("Enter Your master password: ");
                        master = sc.nextLine();
                        System.out.println();

                        try {
                            Password.checkMaster(master);
                            System.out.print("Enter the password's nickname: ");
                            String alias = sc.nextLine();
                            System.out.println();

                            Password.remove(alias);
                            Password.setLog("Remove", "repository");

                            System.out.println(ANSI_YELLOW + "Password deleted." + ANSI_RESET);
                            System.out.println();

                            System.out.println("Press enter to continue");
                            sc.nextLine();
                        } catch (PasswordException e) {
                            System.out.println(e.getMessage());
                            System.out.println();
                            System.out.println("Press enter to continue");
                            sc.nextLine();
                        }
                        break;
                    case 4:
                        sc.nextLine();
                        System.out.print("Enter Your master password: ");
                        master = sc.nextLine();
                        System.out.println();

                        try {
                            Password.checkMaster(master);

                            Ui.showPasswords();
                            Password.setLog("List", "repository");

                            System.out.println();
                            System.out.println("Press enter to continue");
                            sc.nextLine();
                        } catch (PasswordException e) {
                            System.out.println(e.getMessage());
                            System.out.println();
                            System.out.println("Press enter to continue");
                            sc.nextLine();
                        }
                        break;
                    case 5:
                        int opc = 0;

                        while (opc != -1) {
                            Ui.clearConsole();
                            Ui.configMenu();
                            opc = sc.nextInt();

                            switch (opc) {
                                case -1:
                                    break;
                                case 1:
                                    sc.nextLine();
                                    System.out.println();
                                    System.out.print("Enter the old master password(Press enter if you don't have): ");
                                    String oldPass = sc.nextLine();

                                    try {
                                        Password.checkMaster(oldPass.trim());

                                        System.out.println();
                                        System.out.print("Enter the new master password: ");
                                        String newPass = sc.nextLine();

                                        Password.setMaster(newPass);
                                        Password.setLog("Change master", "repository");

                                        System.out.println();
                                        System.out.println(ANSI_YELLOW + "Master password saved !" + ANSI_RESET);
                                        System.out.println();
                                        System.out.println("Press enter to continue");
                                        sc.nextLine();

                                    } catch (PasswordException e) {
                                        System.out.println(e.getMessage());
                                        System.out.println();
                                        System.out.println("Press enter to continue.");
                                        sc.nextLine();
                                    }
                                    break;
                                case 2:
                                    sc.nextLine();
                                    System.out.print("Enter Your master password: ");
                                    master = sc.nextLine();

                                    try {
                                        Password.checkMaster(master);
                                        Password.getLog();
                                        Password.setLog("List", "Logs");
                                    } catch (PasswordException e) {
                                        System.out.println(e.getMessage());
                                        System.out.println();
                                        System.out.println("Press enter to continue.");
                                        sc.nextLine();
                                    }

                                    break;
                                default:
                                    System.out.println("This option does not exists");
                            }
                            break;
                        }
                    default:
                        System.out.println("This option does not exists");
                        break;
                }
            } catch (PasswordException e) {
                System.out.println(e.getMessage());

            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.getStackTrace();
            }
            Ui.clearConsole();
        }
    }
}
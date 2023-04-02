package app;

import java.util.Scanner;

import model.Password;
import model.exceptions.PasswordException;

public class Program {
    public static void main(String[] args){

        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_YELLOW = "\u001B[33m";

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        Ui.clearConsole();
        Ui.intro();
        
        while(choice != -1){
            try{

                Ui.menu();

                choice = sc.nextInt();
                System.out.println();
                switch(choice){
                    case -1:
                        break;
                    case 1:
                        sc.nextLine();
                        System.out.print("Enter the old master password(Press enter if you don't have): ");
                        String old = sc.nextLine();
                        Password.checkMaster(old);
                        System.out.println();
                        System.out.print("Enter the new master password: ");
                        String newPass = sc.nextLine();
                        System.out.println();
                        Password.setMaster(newPass);
                        System.out.println();
                        System.out.println(ANSI_YELLOW + "Master password saved !" + ANSI_RESET);
                        System.out.println();
                        System.out.println("Press enter to continue");
                        sc.nextLine();
                        break;
                    case 2:
                        System.out.print("Enter the length of your password: ");
                        System.out.println(ANSI_YELLOW + "\nGenerated password: " + Password.generate(sc.nextInt()) + ANSI_RESET);
                        System.out.println();
                        System.out.println("Press enter to continue");
                        sc.nextLine();
                        sc.nextLine();
                        break;
                    case 3:

                        break;
                    case 4:
                        
                        break;
                    default:
                        System.out.println("This option does not exists");
                        break;
                }
                
                Ui.clearConsole();
            }catch(PasswordException e){
                System.out.println(e.getMessage());

            }catch(Exception e){
                System.out.println(e.getMessage());
                e.getStackTrace();
            }
        }
        System.out.println();
        System.out.println("See you later !");
        System.out.println();
        sc.close();
    }
}
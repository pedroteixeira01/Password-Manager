package app;

import java.util.Iterator;

import org.json.simple.JSONObject;

import model.Password;

@SuppressWarnings("unchecked")
public class Ui {

    public static void intro() {
        System.out.println(
                "                                ╔╗                                 " + "      .-\"\"-. \n"
                        + "                                ║║                                   " + "   / .--. \\ \n"
                        + "╔══╗╔══╗ ╔══╗╔══╗╔╗╔╗╔╗╔══╗╔═╗╔═╝║    ╔╗╔╗╔══╗ ╔═╗ ╔══╗ ╔══╗╔══╗╔═╗  " + "  / /    \\ \\ \n"
                        + "║╔╗║╚ ╗║ ║══╣║══╣║╚╝╚╝║║╔╗║║╔╝║╔╗║    ║╚╝║╚ ╗║ ║╔╗╗╚ ╗║ ║╔╗║║╔╗║║╔╝  " + "  | |    | | \n"
                        + "║╚╝║║╚╝╚╗╠══║╠══║╚╗╔╗╔╝║╚╝║║║ ║╚╝║    ║║║║║╚╝╚╗║║║║║╚╝╚╗║╚╝║║║═╣║║   " + "  | |.-\"\"-.| \n"
                        + "║╔═╝╚═══╝╚══╝╚══╝ ╚╝╚╝ ╚══╝╚╝ ╚══╝    ╚╩╩╝╚═══╝╚╝╚╝╚═══╝╚═╗║╚══╝╚╝   " + " ///`.::::.`\\ \n"
                        + "║║                                                      ╔═╝║         " + "||| ::/  \\:: ; \n"
                        + "╚╝                                                      ╚══╝         " + "||; ::\\__/:: ; \n"
                        + "                                                                     "
                        + " \\\\\\ '::::' / \n"
                        + "                                                                     " + "  `=':-..-'` \n");
    }

    public static void mainMenu() {
        System.out.print(
                "      Choose the option:       \n"
                        + "-------------------------------\n"
                        + "|1 - Generate a new password   |\n"
                        + "-------------------------------\n"
                        + "|2 - Register password         |\n"
                        + "-------------------------------\n"
                        + "|3 - Remove registered password|\n"
                        + "-------------------------------\n"
                        + "|4 - List registered passwords |\n"
                        + "-------------------------------\n"
                        + "|5 - Manager Configurations    |\n"
                        + "-------------------------------\n"
                        + "|-1 - Exit                     |\n"
                        + "-------------------------------\n"
                        + "-> ");
    }

    public static void configMenu() {
        System.out.print(
                "      Choose the option:       \n"
                        + "-------------------------------\n"
                        + "|1 - Change master password    |\n"
                        + "-------------------------------\n"
                        + "|2 - Show Logs                 |\n"
                        + "-------------------------------\n"
                        + "|-1 - Back                     |\n"
                        + "-------------------------------\n"
                        + "-> ");
    }

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void showPasswords() {
        JSONObject obj = Password.getPasswords();
        Iterator<String> iterator = obj.keySet().iterator();

        System.out.println(
                "          Password List        \n"
                        + "+---------------+-------------+\n"
                        + "|    Nickname   |   Password  |\n"
                        + "+---------------+-------------+");

        while (iterator.hasNext()) {
            String value = iterator.next();
            System.out.printf("#  %s    ->  %s\n", value, obj.get(value));
            System.out.println("+---------------+");
        }
    }
}
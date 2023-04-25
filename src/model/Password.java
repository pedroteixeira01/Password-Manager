package model;

import model.exceptions.PasswordException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Random;
import java.util.Properties;
import java.util.Date;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.StringBuilder;

@SuppressWarnings("unchecked")
public class Password {

    private static HashMap<Integer, String> possibilities = new HashMap<>();
    private static Random rd = new Random();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    private static Properties getProps() {
        try (FileInputStream fi = new FileInputStream(getConfPath())) {
            Properties props = new Properties();
            props.load(fi);

            return props;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new PasswordException(ANSI_RED + "Unexpected error in properties." + ANSI_RESET);
        }
    }

    private static String getLogPath() {
        Properties props = getProps();
        return props.getProperty("log-path");
    }

    private static String getRepoPath() {
        Properties props = getProps();
        return props.getProperty("repo-path");
    }

    private static String getConfPath() {

        if (System.getProperty("os.name").startsWith("Windows")) {
            return "C:\\Users\\" + System.getProperty("user.name") + "\\mg.properties";
        } else {
            return "/home/" + System.getProperty("user.name") + "/.mg.properties";
        }
    }

    private static void write(String path, boolean append, String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, append))) {

            bw.write(text);
            bw.newLine();

        } catch (IOException e) {
            System.out.println();
            throw new PasswordException(ANSI_RED + "Unexpected error writing the file." + ANSI_RESET);
        }
    }

    private static void createConf() {
        String repoPath;
        String logPath;
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                repoPath = "C:\\Users\\" + System.getProperty("user.name") + "\\mg.json";
                logPath = "C:\\Users\\" + System.getProperty("user.name") + "\\mg.log";
            } else {
                repoPath = "/home/" + System.getProperty("user.name") + "/.mg.json";
                logPath = "/home/" + System.getProperty("user.name") + "/.mg.log";
            }

            File conf = new File(getConfPath());
            if (!conf.exists()) {
                conf.createNewFile();
            }

            File log = new File(logPath);
            if (!log.exists()) {
                log.createNewFile();
            }

            write(getConfPath(), true, "repo-path=" + repoPath);
            write(getConfPath(), true, "log-path=" + logPath);
        } catch (IOException e) {
            System.out.println();
            throw new PasswordException(ANSI_RED + "Unexpected error in file creation." + ANSI_RESET);
        }
    }

    private static void createRepo() {
        try {
            File repo = new File(getRepoPath());
            repo.createNewFile();
        } catch (IOException e) {
            System.out.println();
            throw new PasswordException(ANSI_RED + "Unexpected error in file creation." + ANSI_RESET);
        }
    }

    private static boolean masterExists() {
        JSONParser parser = new JSONParser();

        try (FileReader fr = new FileReader(getRepoPath())) {
            JSONObject obj = (JSONObject) parser.parse(fr);

            if (obj.containsKey("master")) {
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (ParseException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static void checkDependencies() {
        if (!(new File(getConfPath()).exists())) {
            createConf();
        }
        if (!(new File(getRepoPath()).exists())) {
            createRepo();
        }
        if (!masterExists()) {
            String pass = generate(8).toString();
            System.out.println(
                    ANSI_YELLOW
                            + "Your random master password is: " + pass + "\n"
                            + "You can change later if you want."
                            + ANSI_RESET);

            System.out.println();

            setMaster(pass);
        }
    }

    public static StringBuilder generate(int passLen) {

        if (passLen < 8) {
            System.out.println();
            throw new PasswordException(
                    ANSI_RED + "The password have few digits. Use more than or equal 8 digits.\n" + ANSI_RESET);
        }

        StringBuilder pass = new StringBuilder();
        pass.setLength(passLen);

        possibilities.put(0, "abcdefghijklmnopqrstuvwxyz");
        possibilities.put(1, "abcdefghijklmnopqrstuvwxyz".toUpperCase());
        possibilities.put(2, "0123456789");
        possibilities.put(3, "#$@&*?!()-=+_");

        for (int i = 0; i < passLen; i++) {
            int type = rd.nextInt(4);

            switch (type) {
                case 0:
                    pass.append(possibilities.get(0).charAt(rd.nextInt(26)));
                    break;
                case 1:
                    pass.append(possibilities.get(1).charAt(rd.nextInt(26)));
                    break;
                case 2:
                    pass.append(possibilities.get(2).charAt(rd.nextInt(10)));
                    break;
                case 3:
                    pass.append(possibilities.get(3).charAt(rd.nextInt(13)));
                    break;
                default:
                    System.out.println("Something went wrong");
                    break;
            }
        }

        return pass;
    }

    public static void checkMaster(String master) {
        JSONParser parser = new JSONParser();

        try (FileReader fr = new FileReader(getRepoPath())) {
            JSONObject obj = (JSONObject) parser.parse(fr);

            String pass = (String) obj.get("master");
            if (!(pass.equals(master.trim()))) {
                System.out.println();
                throw new PasswordException(ANSI_RED + "The master password is incorrect." + ANSI_RESET);
            }
        } catch (FileNotFoundException e) {
            System.out.println(ANSI_RED + "The file does not exist." + ANSI_RESET);
        } catch (ParseException e) {
            e.getMessage();
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Unexpected error in file." + ANSI_RESET);
        }
    }

    public static void setMaster(String newPass) {
        if (newPass.trim().length() < 8) {
            System.out.println();
            throw new PasswordException(
                    ANSI_RED + "The password have few digits. Use more than or equal 8 digits.\n" + ANSI_RESET);
        }

        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            obj = (JSONObject) parser.parse(new FileReader(getRepoPath()));

            if (!masterExists()) {

                obj.put("master", newPass);
                write(getRepoPath(), true, obj.toJSONString());
            } else {

                obj.replace("master", newPass);
                write(getRepoPath(), false, obj.toJSONString());
            }
        } catch (FileNotFoundException e) {
            System.out.println(ANSI_RED + "The file does not exist." + ANSI_RESET);
        } catch (ParseException e) {
            e.getMessage();
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Unexpected error in file." + ANSI_RESET);
        }
    }

    public static void register(String alias, String password) {
        if (alias.equals("master")) {
            throw new PasswordException(ANSI_RED + "You can not use this nickname." + ANSI_RESET);
        }
        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(getRepoPath()));

            if (obj.containsKey(alias)) {
                throw new PasswordException("This nickname already exists.");
            }

            obj.put(alias.trim(), password.trim());

            write(getRepoPath(), false, obj.toJSONString());

        } catch (FileNotFoundException e) {
            System.out.println(ANSI_RED + "The file does not exist." + ANSI_RESET);
        } catch (ParseException e) {
            e.getMessage();
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Unexpected error in file." + ANSI_RESET);
        }
    }

    public static void remove(String alias) {
        if (alias.equals("master")) {
            throw new PasswordException(ANSI_RED + "You can not remove the master password." + ANSI_RESET);
        }
        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(getRepoPath()));

            if (!obj.containsKey(alias)) {
                throw new PasswordException("This nickname does not exist.");
            }

            obj.remove(alias);

            write(getRepoPath(), false, obj.toJSONString());

        } catch (FileNotFoundException e) {
            System.out.println(ANSI_RED + "The file does not exist." + ANSI_RESET);
        } catch (ParseException e) {
            e.getMessage();
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Unexpected error in file." + ANSI_RESET);
        }
    }

    public static JSONObject getPasswords() {
        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(getRepoPath()));
            obj.remove("master");

            return obj;
        } catch (FileNotFoundException e) {
            System.out.println(ANSI_RED + "The file does not exist." + ANSI_RESET);
            return null;
        } catch (ParseException e) {
            e.getMessage();
            return null;
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Unexpected error in file." + ANSI_RESET);
            return null;
        }
    }

    public static void setLog(String operation, String source) {
        write(getLogPath(), false, "#[ " + new Date() + " ] " + operation + "@" + source);
    }

    public static void getLog() {
        try (BufferedReader br = new BufferedReader(new FileReader(getLogPath()))) {
            String line = br.readLine();

            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

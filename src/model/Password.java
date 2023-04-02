package model;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.lang.StringBuilder;

import model.exceptions.PasswordException;

public class Password{

    private static HashMap<Integer, String> possibilities = new HashMap<>();
    private static Random rd = new Random();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    private static String getPath(){

        if(System.getProperty("os.name").startsWith("Windows")){
            return "C:\\Users\\" + System.getProperty("user.name") + "\\mg.csv";
        }else{
            return "/home/" + System.getProperty("user.name") + "/.mg.csv";
        }
    }

    private static void write(String path, String text){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(path))){

            bw.write(text);
            bw.newLine();

        }catch(IOException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static StringBuilder generate(int passLen){

        if(passLen < 8){
            System.out.println();
            throw new PasswordException(ANSI_RED + "The password have few digits. Use more than or equal 8 digits.\n" + ANSI_RESET);
        }

        StringBuilder pass = new StringBuilder();
        pass.setLength(passLen);

        possibilities.put(0, "abcdefghijklmnopqrstuvwxyz");
        possibilities.put(1, "abcdefghijklmnopqrstuvwxyz".toUpperCase());
        possibilities.put(2, "0123456789");
        possibilities.put(3, "#$@&*?!()-=+");
        
        for(int i=0; i<passLen; i++){
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
                    pass.append(possibilities.get(3).charAt(rd.nextInt(12)));
                    break;
                default:
                    System.out.println("Something went wrong");
                    break;
            }
        }

        return pass;
    }

    public static void checkMaster(String oldPass){
        File f = new File(getPath());

        try(Scanner sc = new Scanner(f)){
            String[] tuple = sc.nextLine().split(",");

            if(!oldPass.equals(tuple[1])){
                System.out.println();
                throw new PasswordException(ANSI_RED + "Wrong master password\n" + ANSI_RESET);
            }
        }catch(IOException e){
            e.getMessage();
        }
    }

    public static void setMaster(String newPass){
        if(newPass.length()<8){
            System.out.println();
            throw new PasswordException(ANSI_RED + "The password have few digits. Use more than or equal 8 digits.\n" + ANSI_RESET);
        }

        File f = new File(getPath());

        if(f.exists()){
            String tuple = "Master," + newPass;
            write(getPath(), tuple);
            
        }else{
            String tuple = "Master," + newPass;
            write(getPath(), tuple);
        }
    }

    public static void register(String master, String alias, String password){}

    public static void getPasswords(String master){}
}
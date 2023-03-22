package model;

import java.util.HashMap;
import java.util.Random;

import java.lang.StringBuilder;

import model.exceptions.PasswordException;

public class Password{

    private static HashMap<Integer, String> possibilities = new HashMap<>();
    private static Random rd = new Random();

    public static StringBuilder generate(int passLen){

        if(passLen < 8){
            throw new PasswordException("The password have few digits. Use more than or equal 8 digits.");
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
}
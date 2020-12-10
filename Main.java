import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("   ___      _   _      __ _           _ _             ");
        System.out.println("  / _ \\__ _| |_| |__  / _(_)_ __   __| (_)_ __   __ _ ");
        System.out.println(" / /_)/ _` | __| '_ \\| |_| | '_ \\ / _` | | '_ \\ / _` |");
        System.out.println("/ ___/ (_| | |_| | | |  _| | | | | (_| | | | | | (_| |");
        System.out.println("\\/    \\__,_|\\__|_| |_|_| |_|_| |_|\\__,_|_|_| |_|\\__, |");
        System.out.println("                                                |___/ \n");
        System.out.println("By Allan Dao, Aditya Nair, and Connor Lynch");

        // Allow user to access different parts of the project (the two different pathfinding
        // methods).
        boolean menuOptionSelected = false;
        while (!menuOptionSelected) {
            System.out.println("ArrayMap (A), TileMap (T), or Exit (E)?");
            String input = scanner.next();
            switch (input.substring(0, 1).toUpperCase()) {
                case "A":
                    menuOptionSelected = true;
                    ArrayMap.main(args); 
                case "T":
                    menuOptionSelected = true;
                    TileMap.main(args);
                case "E":
                    menuOptionSelected = true;
                    System.exit(0);
            }
        }
    }
}

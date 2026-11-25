package ui;

import java.util.Scanner;

// Reads data from the keyboard. It only checks the format (a number is a
// number, a text is not empty) and asks again until the input is valid.
// Business rules (price greater than zero, enough units...) stay in the domain.
public class Console {

    private final Scanner scanner;

    // Only one Console (and one Scanner) exists: Main creates it
    // and passes it to every menu.
    public Console(){
        scanner = new Scanner(System.in);
    }

    // A text that is not blank. message is the question shown to the user.
    public String readText(String message){
        while(true){
            System.out.print(message);
            String text = scanner.nextLine().trim();
            if(!text.isBlank()){
                return text;
            }
            System.out.println("El valor no puede estar vacío.");
        }
    }

    // Any whole number. The domain decides if the value makes sense.
    public int readInt(String message){
        while(true){
            System.out.print(message);
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.println("Ingrese un número entero.");
            }
        }
    }

    // A whole number between min and max, both included.
    // Used for menu options and list positions.
    public int readInt(String message, int min, int max){
        while(true){
            int number = readInt(message);
            if(number >= min && number <= max){
                return number;
            }
            System.out.println("Ingrese un número entre " + min + " y " + max + ".");
        }
    }

    // A whole number for money values.
    public long readLong(String message){
        while(true){
            System.out.print(message);
            try{
                return Long.parseLong(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.println("Ingrese un número entero, sin puntos ni comas.");
            }
        }
    }

    // A yes or no answer: s / n. Used to ask whether to add another item to a sale.
    public boolean readYesNo(String message){
        while(true){
            String answer = readText(message + " (s/n): ").toLowerCase();
            if(answer.equals("s")){
                return true;
            }
            if(answer.equals("n")){
                return false;
            }
            System.out.println("Responda s o n.");
        }
    }
}

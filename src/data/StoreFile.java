package data;

import domain.ClothingStore;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Saves and loads the whole store as a single object in a binary file,
// using object serialization. The store keeps every garment, item and sale,
// so one object holds all the data and its references.
public class StoreFile {

    // Relative path: the file is created in the folder the program runs from.
    private static final String FILE_NAME = "tienda.dat";

    // Writes the whole store to the file, replacing the previous content.
    // ObjectOutputStream turns the store into bytes and FileOutputStream
    // writes them to the file. try-with-resources closes both at the end.
    public static void save(ClothingStore store){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))){
            out.writeObject(store);
            System.out.println("Datos guardados.");
        }catch(IOException e){
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    // Reads the store from the file. readObject returns an Object, so it is
    // cast to ClothingStore. Returns null if the file does not exist or
    // cannot be read; then Main starts with a new store.
    public static ClothingStore load(){
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))){
            ClothingStore store = (ClothingStore) in.readObject();
            System.out.println("Datos cargados.");
            return store;
        }catch(IOException | ClassNotFoundException e){
            System.out.println("No se encontraron datos guardados. Se inicia una tienda nueva.");
            return null;
        }
    }
}
